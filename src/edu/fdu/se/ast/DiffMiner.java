package edu.fdu.se.ast;

import java.util.List;

import com.github.gumtreediff.actions.ActionUtil;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import cn.edu.fudan.se.apiChangeExtractor.gumtreeParser.GumTreeDiffParser;
import edu.fdu.se.fileutil.FileUtil;

public class DiffMiner {
	//1.TODO check
	public void checkIfAppliedActionOnSrcSameAsDst(){
		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\curr\\GTExample2.java";
		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\prev\\GTExample2.java";
		MyGumTreeParser diff = new MyGumTreeParser(file1,file2);
		diff.init();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/srcTree.txt",diff.getPrettyOldTreeString());
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstTree.txt",diff.getPrettyNewTreeString());
		System.out.println("----------------------Actions----------------------------------");
		diff.printActions(diff.getActions());
		TreeContext temp = ActionUtil.apply(diff.srcTC, diff.getActions());
		diff.srcTC.validate();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstnewTree.txt",diff.getPrettyOldTreeString());
	}
	//2.Action Cluster
	public void userGTIntermediate(){
		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\prev\\ClusterAction1.java";
		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\curr\\ClusterAction1.java";
		MyGumTreeParser his = new MyGumTreeParser(file1,file2);
		his.init();
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/srcTree.txt",his.getPrettyOldTreeString());
		FileUtil.writeInAll("C:/Users/huangkaifeng/Desktop/11-8/dstTree.txt",his.getPrettyNewTreeString());
		System.out.println("----------------------------Actions----------------------------------");
		MyActionGenerator g = new MyActionGenerator(his.src, his.dst, his.mapping);
		his.actions = g.generate();
		his.printActions(his.getActions());
		MyActionClusterFinder finder = new MyActionClusterFinder(his.srcTC, his.dstTC,his.actions,g.layeredActionIndexList,g.layeredLastChildrenIndexList);
		List<List<Action>> result = finder.clusteredActions();
		his.printClusteredActions(result);
	}
	public void test(){
		String file1 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\prev\\ClusterAction1.java";
		String file2 = "C:\\Users\\huangkaifeng\\Desktop\\11-8\\test\\curr\\ClusterAction1.java";
		MyGumTreeParser his = new MyGumTreeParser(file1,file2);
		his.init();
		his.test();
	}

	public static void main(String args[]){
		DiffMiner i = new DiffMiner();
		i.userGTIntermediate();
//		i.test();
	}

}
