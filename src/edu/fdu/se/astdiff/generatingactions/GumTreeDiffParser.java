package edu.fdu.se.astdiff.generatingactions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;


import com.github.gumtreediff.actions.ActionClusterFinder;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;

public class GumTreeDiffParser {
	File oldFile;
	File newFile;
	public TreeContext srcTC;
	public TreeContext dstTC;
	public ITree src;
	public ITree dst;
	public List<Action> actions;
	public MappingStore mapping;
	public ActionClusterFinder finder;
	
	public GumTreeDiffParser(String oldFileName, String newFileName){
		this.oldFile = new File(oldFileName);
		this.newFile = new File(newFileName);
	}
	public GumTreeDiffParser(File oldFile, File newFile){
		this.oldFile = oldFile;
		this.newFile = newFile;
	}
	
	public void init(){
		Run.initGenerators();
		try {
			JdtTreeGenerator parser1 = new JdtTreeGenerator(oldFile.getPath());
			srcTC = parser1.generateFromFile(oldFile);
			src = srcTC.getRoot();
			JdtTreeGenerator parser2 = new JdtTreeGenerator(newFile.getPath());
			dstTC = parser2.generateFromFile(newFile);
			dst = dstTC.getRoot();
			Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
			m.match();
			mapping = m.getMappings();
			ActionGenerator g = new ActionGenerator(src, dst, mapping);
			g.generate();
			actions = g.getActions();
//			finder = new ActionClusterFinder(srcTC, dstTC, actions);
//			finder.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void listStartNodes(){
		for(Action a : finder.getStartNodes()){
			System.out.println(a.toString());
		}
	}
	public List<Set<Action>> getCluster(){
		return finder.getClusters();
	}
	public List<Action> getActions(){
		return actions;
	}
	public MappingStore getMapping(){
		return mapping;
	}
	
	public String getPrettyOldTreeString(){
		return ConsolePrint.getPrettyTreeString(srcTC, src);
	}
	public String getPrettyNewTreeString(){
		return ConsolePrint.getPrettyTreeString(dstTC, dst);
	}

}
