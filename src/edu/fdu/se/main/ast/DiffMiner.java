package edu.fdu.se.main.ast;


import com.github.gumtreediff.actions.ActionUtil;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionGeneratorBean;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.generatingactions.GumTreeDiffParser;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.miningactions.FindPattern;
import edu.fdu.se.astdiff.miningactions.MiningActionBean;
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
		GumTreeDiffParser diff = new GumTreeDiffParser(file1,file2);
		diff.init();
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",diff.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",diff.getPrettyNewTreeString());
		System.out.println(diff.getPrettyNewTreeString());
		System.out.println("----------------------Actions----------------------------------");
//		diff.printActions(diff.getActions());
		TreeContext temp = ActionUtil.apply(diff.srcTC, diff.getActions());
		diff.srcTC.validate();
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstnewTree.txt",diff.getPrettyOldTreeString());
	}
	
	//2.Action Cluster
	public void userGTIntermediate(){
		//
		System.out.println("Generating Diff Actions");
		String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
		String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
		GumTreeDiffParser his = new GumTreeDiffParser(file1,file2);
		his.init();
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",his.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",his.getPrettyNewTreeString());
		MyActionGenerator gen = new MyActionGenerator(his.src, his.dst, his.mapping);
		ActionGeneratorBean data = gen.generate();
		ConsolePrint.printMyActions(data.getDstTreeActions(),his.dstTC);
		ConsolePrint.printMyActions(data.getSrcTreeActions(),his.dstTC);
//		MyActionClusterFinder finder = new MyActionClusterFinder(his.srcTC, his.dstTC,data);
//		List<List<Action>> result = finder.clusteredActions();
		//step 2
		System.out.println("Begin to find Pattern:-------------------");
		MiningActionBean bean = new MiningActionBean(data,his.srcTC,his.dstTC,his.mapping);
		FindPattern fp = new FindPattern(bean);
		fp.find();
		
	}
	

	public static void main(String args[]){
		DiffMiner i = new DiffMiner();
		i.userGTIntermediate();
	}
	

}
