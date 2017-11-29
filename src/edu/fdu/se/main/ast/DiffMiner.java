package edu.fdu.se.main.ast;

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
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
/**
 * given two files, generate edit script.
 * @author huangkaifeng
 *
 */
public class DiffMiner {
	//1.TODO check
	public void checkIfAppliedActionOnSrcSameAsDst(){
		String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
		String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
		MyGumTreeParser diff = new MyGumTreeParser(file1,file2);
		diff.init();
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",diff.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",diff.getPrettyNewTreeString());
		System.out.println("----------------------Actions----------------------------------");
		diff.printActions(diff.getActions());
		TreeContext temp = ActionUtil.apply(diff.srcTC, diff.getActions());
		diff.srcTC.validate();
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstnewTree.txt",diff.getPrettyOldTreeString());
	}
	
	//2.Action Cluster
	public void userGTIntermediate(){
		String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
		String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
		MyGumTreeParser his = new MyGumTreeParser(file1,file2);
		his.init();
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",his.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",his.getPrettyNewTreeString());
		MyActionGenerator gen = new MyActionGenerator(his.src, his.dst, his.mapping);
		ActionGeneratorBean data = gen.generate();
		his.printMyActions(data.getDstTreeActions());
		his.printMyActions(data.getSrcTreeActions());
		MyActionClusterFinder finder = new MyActionClusterFinder(his.srcTC, his.dstTC,data);
		List<List<Action>> result = finder.clusteredActions();
		his.printClusteredActions(result);
	}
	public void test(){
		String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
		String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
		MyGumTreeParser his = new MyGumTreeParser(file1,file2);
		his.init();
		his.test();
	}

	public static void main(String args[]){
		DiffMiner i = new DiffMiner();
		i.userGTIntermediate();
	}

}
