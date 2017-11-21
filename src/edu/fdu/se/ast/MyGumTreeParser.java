package edu.fdu.se.ast;

import java.util.List;

import com.github.gumtreediff.actions.ActionUtil;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.TreeContext;

import cn.edu.fudan.se.apiChangeExtractor.gumtreeParser.GumTreeDiffParser;
import edu.fdu.se.fileutil.FileUtil;

public class MyGumTreeParser {
	//TODO check
	public void checkIfAppliedActionOnSrcSameAsDst(){
		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\curr\\GTExample2.java";
		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\prev\\GTExample2.java";
		GumTreeDiffParser diff = new GumTreeDiffParser(file1,file2);
		diff.init();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/srcTree.txt",diff.getPrettyOldTreeString());
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstTree.txt",diff.getPrettyNewTreeString());
		System.out.println("----------------------Actions----------------------------------");
		diff.printActions(diff.getActions());
		TreeContext temp = ActionUtil.apply(diff.srcTC, diff.getActions());
		diff.srcTC.validate();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstnewTree.txt",diff.getPrettyOldTreeString());
//		diff.listStartNodes();
	}
	public MappingStore mMapping;
	public List<Action> mActions;
	
	public void userGTIntermediate(){
		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\prev\\GTExample2.java";
		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\curr\\GTExample2.java";
		GumTreeDiffParser his = new GumTreeDiffParser(file1,file2);
		his.init();
		mMapping = his.getMapping();
		mActions = his.getActions();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/srcTree.txt",his.getPrettyOldTreeString());
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstTree.txt",his.getPrettyNewTreeString());
		System.out.println("----------------------------Actions----------------------------------");
		his.printActions(his.getActions());
//		diff.listStartNodes();
		
	}
	public static void main(String args[]){
		MyGumTreeParser i = new MyGumTreeParser();
		i.userGTIntermediate();
	}

}
