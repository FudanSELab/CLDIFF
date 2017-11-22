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
		
		String a1="AA";
		String a2="BB";
		List<String> mlist=new ArrayList<String>();
		t(mlist);
		System.out.println(mlist);
	}
	
	public static  void t(List<String> tes){
		tes.add("a");
		tes.add("b");
	}

}
