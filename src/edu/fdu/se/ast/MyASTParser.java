package edu.fdu.se.ast;

import java.io.IOException;

import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

public class MyASTParser {
	
	public static void main(String args[]){
		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\DiffDirByVersion\\25-24\\curr\\AbsListView.java";
		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\DiffDirByVersion\\25-24\\prev\\AbsListView.java";
		Run.initGenerators();
		ITree src=null;
		ITree dst=null;
		try {
			src = Generators.getInstance().getTree(file1).getRoot();
			dst = Generators.getInstance().getTree(file2).getRoot();
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
		m.match();
		m.getMappings(); // return the mapping stor
		System.out.println("aa");
		
	}

}
