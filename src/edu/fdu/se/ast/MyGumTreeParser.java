package edu.fdu.se.ast;

import com.github.gumtreediff.actions.ActionUtil;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.ast.GumTreeDiffParser;
import edu.fdu.se.fileutil.FileUtil;

public class MyGumTreeParser {
	public static void main(String args[]){
		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\curr\\GTExample3.java";
		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\prev\\GTExample3.java";
		GumTreeDiffParser diff = new GumTreeDiffParser(file1,file2);
		diff.init();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/srcTree.txt",diff.getPrettyOldTreeString());
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstTree.txt",diff.getPrettyNewTreeString());
		System.out.println("----------------------Actions----------------------------------");
		diff.printActions(diff.getActions());
		TreeContext temp = ActionUtil.apply(diff.srcTC, diff.actions);
		diff.srcTC.validate();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstnewTree.txt",diff.getPrettyOldTreeString());
		
//		diff.listStartNodes();
	}

}
