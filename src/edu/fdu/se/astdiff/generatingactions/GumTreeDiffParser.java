package edu.fdu.se.astdiff.generatingactions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;



import com.github.gumtreediff.actions.ActionClusterFinder;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

public class GumTreeDiffParser {

	public TreeContext srcTC;
	public TreeContext dstTC;
	public ITree src;
	public ITree dst;
	public MappingStore mapping;

	public GumTreeDiffParser(File prevFile, File currFile){
		File oldFile = prevFile;
		File newFile = currFile;

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public GumTreeDiffParser(String prevContent,String currContent){
		Run.initGenerators();
		try {
			JdtTreeGenerator parser1 = new JdtTreeGenerator();
			srcTC = parser1.generateFromString(prevContent);
			src = srcTC.getRoot();
			JdtTreeGenerator parser2 = new JdtTreeGenerator();
			dstTC = parser2.generateFromString(currContent);
			dst = dstTC.getRoot();
			Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
			m.match();
			mapping = m.getMappings();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}




	public MappingStore getMapping(){
		return mapping;
	}
	
	public String getPrettyOldTreeString() {
		return ConsolePrint.getPrettyTreeString(srcTC, src);
	}
	public String getPrettyNewTreeString(){
		return ConsolePrint.getPrettyTreeString(dstTC, dst);
	}

}
