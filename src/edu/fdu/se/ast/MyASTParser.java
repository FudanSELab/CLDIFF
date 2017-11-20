package edu.fdu.se.ast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.gumtreediff.actions.ActionClusterFinder;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.RootAndLeavesClassifier;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

public class MyASTParser {
	
	public static void main(String args[]){
		
		String b="bbb";
		List<String> aa=new ArrayList<String>();
		aa.add(b);
		System.out.println(aa);
//		System.out.println(System.getProperty("gt.stm.mh", "1"));
//		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\curr\\RefactorMoveMethod.java";
//		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\prev\\RefactorMoveMethod.java";
//		Run.initGenerators();
//		ITree src=null;
//		ITree dst=null;
//		TreeContext tsrc=null;
//		TreeContext tdst=null;
//		try {
//			tsrc = new JdtTreeGenerator().generateFromFile(new File(file1));
//			tdst = new JdtTreeGenerator().generateFromFile(new File(file2));
//			src = tsrc.getRoot();
//			dst = tdst.getRoot();
//		} catch (UnsupportedOperationException | IOException e) {
//			e.printStackTrace();
//		}
//		Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
//		m.match();
//		MappingStore mappings = m.getMappings();
//		RootAndLeavesClassifier ral = new RootAndLeavesClassifier(tsrc,tdst,m);
//		ActionGenerator g = new ActionGenerator(src, dst, mappings);
//		g.generate();
//		List<Action> actions = g.getActions();
//		ActionClusterFinder finder = new ActionClusterFinder(tsrc, tdst, actions);
//		finder.show();

//		Map<ITree,ITree> mm = mappings.getDsts();
//		for(Entry<ITree,ITree> item:mm.entrySet()){
//			System.out.println(item.getKey().toShortString()+"    "+item.getValue().toShortString());
//		}
		
		
		
	}
	
	
	

	

}
