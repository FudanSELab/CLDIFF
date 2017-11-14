package edu.fdu.se.ast;

import java.io.IOException;

import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.tree.ITree;

public class MyASTParser {
	
	public static void main(String args[]){
		//TODO
		String file = "myfile.java";
		try {
			ITree tree = new JdtTreeGenerator().generateFromString(file).getRoot();
			System.out.println("aa");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
