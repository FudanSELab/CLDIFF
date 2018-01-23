package edu.fdu.se.main.astdiff;


import com.github.javaparser.ast.CompilationUnit;
import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.generatingactions.GumTreeDiffParser;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.miningactions.ClusterActions;
import edu.fdu.se.astdiff.miningactions.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.MiningOperation;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessingData;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessingSDKClass;
import edu.fdu.se.astdiff.treegenerator.JavaParserTreeGenerator;
import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
import edu.fdu.se.fileutil.FileWriter;
import org.eclipse.jdt.core.dom.ASTNode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * given two files, generate edit script.
 * @author huangkaifeng
 *
 */
public class DiffMiner {
	
	public List<String> readCompareList(int version,String prevPath,String currPath){
		prevList = new ArrayList<>();
		currList = new ArrayList<>();
		List<String> fileSubPathList = new ArrayList<>();
		currList = AndroidSDKJavaFileDAO.selectAllTagSnapshotFileBySDKVersion(version);
		Map<String,Integer> fileNameMap = new HashMap<String,Integer>();
		for(AndroidSDKJavaFile item:currList){
			fileNameMap.put(item.getSubSubCategoryPath(),1);
		}
		prevList = AndroidSDKJavaFileDAO.selectAllTagSnapshotFileBySDKVersion(version-1);
		for(AndroidSDKJavaFile item:prevList){
			String filePath = item.getSubSubCategoryPath();
			if(fileNameMap.containsKey(filePath)){
				File filePrevFull = new File(prevPath + filePath.replace("\\","/"));
				File fileCurrFull = new File(currPath + filePath.replace("\\","/"));
				if(filePrevFull.length() != fileCurrFull.length()){
					fileSubPathList.add(filePath);
				}
			}
		}
		return fileSubPathList;


	}
	private List<AndroidSDKJavaFile> prevList;
	private List<AndroidSDKJavaFile> currList;


	public void runBatch(){
		int version = 26;
		String fileRootPathPrev
				= ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_NEW_SDK_DIR)+"/android-"+String.valueOf(version);
		String fileRootPathCurr
				= ProjectProperties.getInstance().getValue(PropertyKeys.DIFF_MINER_NEW_SDK_DIR)+"/android-"+String.valueOf(version-1);
		List<String> filePathList = readCompareList(version,fileRootPathPrev,fileRootPathCurr);
		int cnt = 0;
		int candidateIndex = 5;
		for(String subPath: filePathList){
			if(cnt < candidateIndex){
			cnt++;
			continue;
		}

		System.out.println(subPath);
		String subPath2 = subPath.replace("\\","/");
		String outputDirName = subPath.replace("\\","_").substring(1);
		String fileFullPathPrev = fileRootPathPrev + subPath2;
		String fileFullPathCurr = fileRootPathCurr + subPath2;
		PreprocessingSDKClass psc =	new PreprocessingSDKClass().compareTwoFile(fileFullPathPrev,fileFullPathCurr,outputDirName);
		PreprocessingData pData = psc.getPreprocessingData();
		//todo toString 变成CompilationUnit直接输入
		GumTreeDiffParser his = new GumTreeDiffParser(pData.getPreviousCu().toString(),pData.getCurrentCu().toString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",his.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",his.getPrettyNewTreeString());
		MyActionGenerator gen = new MyActionGenerator(his.src, his.dst, his.mapping);
		GeneratingActionsData data = gen.generate();
		ConsolePrint.printMyActions(data.getAllActions(),his.dstTC,his.srcTC);
		MiningActionData mMiningActionData = new MiningActionData(data,his.srcTC,his.dstTC,his.mapping);
		ClusterActions.doCluster(mMiningActionData);
		MiningOperation mo = new MiningOperation(pData);
		mo.printHighLevelOperationBeanList(mMiningActionData);
			break;
		}
	}

	/**
	 * test 单个文件
	 */
	public void runGumTree(){
		System.out.println("Step1 Generating Diff Actions:----------------------");
		String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
		String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
		GumTreeDiffParser his = new GumTreeDiffParser(new File(file1),new File(file2));
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",his.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",his.getPrettyNewTreeString());
		// package 1
		MyActionGenerator gen = new MyActionGenerator(his.src, his.dst, his.mapping);
		GeneratingActionsData data = gen.generate();

		ConsolePrint.printMyActions(data.getAllActions(),his.dstTC,his.srcTC);
		// package 2
		System.out.println("Step2 Begin to cluster actions:-------------------");
		MiningActionData mMiningActionData = new MiningActionData(data,his.srcTC,his.dstTC,his.mapping);

		ClusterActions.doCluster(mMiningActionData);
		// package 3
		new MiningOperation().printHighLevelOperationBeanList(mMiningActionData);

	}
	public void run2(){
		System.out.println("Step1 Generating Diff Actions:----------------------");
		String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
		String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
		JavaParserTreeGenerator jtg = new JavaParserTreeGenerator(new File(file1),new File(file2));
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",jtg.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",jtg.getPrettyNewTreeString());
		// package 1
		MyActionGenerator gen = new MyActionGenerator(jtg.src, jtg.dst, jtg.mapping);
		GeneratingActionsData data = gen.generate();
		ConsolePrint.printMyActions(data.getAllActions(),jtg.dstTC,jtg.srcTC);
		// package 2
		System.out.println("Step2 Begin to cluster actions:-------------------");
		MiningActionData mMiningActionData = new MiningActionData(data,jtg.srcTC,jtg.dstTC,jtg.mapping);

		ClusterActions.doCluster(mMiningActionData);
		// package 3
		new MiningOperation().printHighLevelOperationBeanList(mMiningActionData);

	}



	public static void main(String args[]){
		DiffMiner i = new DiffMiner();
		i.run2();
//		i.runGumTree();
//		i.runBatch();

	}

	//todo preprocessing 和gumtree的ast处理统一
	// 二次compile目的是整理line number，设法不二次compile整理
	

}
