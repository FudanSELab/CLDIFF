package edu.fdu.se.main.ast;


import com.github.gumtreediff.actions.ActionUtil;
import com.github.gumtreediff.tree.TreeContext;

import com.github.javaparser.ast.comments.Comment;
import edu.fdu.se.astdiff.generatingactions.ActionGeneratorBean;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.generatingactions.GumTreeDiffParser;
import edu.fdu.se.astdiff.generatingactions.MyActionGenerator;
import edu.fdu.se.astdiff.miningactions.FindPattern;
import edu.fdu.se.astdiff.miningactions.MiningActionBean;
import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.bean.AndroidSDKJavaFileTagSnapshot;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
import edu.fdu.se.dao.AndroidSDKJavaFileTagSnapshotDAO;
import edu.fdu.se.fileutil.FileWriter;

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
		for(String subPath: filePathList){
			cnt++;
			if(cnt <= 0){
				continue;
			}
			System.out.println(subPath);
			String subPath2 = subPath.replace("\\","/");
			String outputDirName = subPath.replace("\\","_").substring(1);
			String fileFullPathPrev = fileRootPathPrev + subPath2;
			String fileFullPathCurr = fileRootPathCurr + subPath2;
			PreprocessingSDKClass psc =	new PreprocessingSDKClass().compareTwoSDKFile3(fileFullPathPrev,fileFullPathCurr,outputDirName);
			GumTreeDiffParser his = new GumTreeDiffParser(psc.getPreCu().toString(),psc.getCurCu().toString());
			FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",his.getPrettyOldTreeString());
			FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",his.getPrettyNewTreeString());
			MyActionGenerator gen = new MyActionGenerator(his.src, his.dst, his.mapping);
			ActionGeneratorBean data = gen.generate();
			ConsolePrint.printMyActions(data.getAllActions(),his.dstTC,his.srcTC);
			System.out.println("Step2 Begin to find Pattern:-------------------");
			MiningActionBean bean = new MiningActionBean(data,his.srcTC,his.dstTC,his.mapping);
			FindPattern fp = new FindPattern(bean);
			fp.find();
			break;
		}

	}

	/**
	 * test 单个文件
	 */
	public void run(){
		System.out.println("Step1 Generating Diff Actions:----------------------");
		String file1 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_PREV_FILE);
		String file2 = ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_CURR_FILE);
		GumTreeDiffParser his = new GumTreeDiffParser(new File(file1),new File(file2));
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/srcTree.txt",his.getPrettyOldTreeString());
		FileWriter.writeInAll(ProjectProperties.getInstance().getValue(PropertyKeys.AST_PARSER_OUTPUT_DIR)+"/dstTree.txt",his.getPrettyNewTreeString());
		MyActionGenerator gen = new MyActionGenerator(his.src, his.dst, his.mapping);
		ActionGeneratorBean data = gen.generate();
		ConsolePrint.printMyActions(data.getAllActions(),his.dstTC,his.srcTC);
		System.out.println("Step2 Begin to find Pattern:-------------------");
		MiningActionBean bean = new MiningActionBean(data,his.srcTC,his.dstTC,his.mapping);
		FindPattern fp = new FindPattern(bean);
		fp.find();

	}


	public static void main(String args[]){
		DiffMiner i = new DiffMiner();
//		i.run();
		i.runBatch();
	}
	

}
