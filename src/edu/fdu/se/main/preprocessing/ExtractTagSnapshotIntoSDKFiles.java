package edu.fdu.se.main.preprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.git.RepositoryHelper;
import edu.fdu.se.git.SDKFileToRepoFilePath;
/**
 * 把sdk 的文件，抽取某个tag版本的内容，到指定目录，统计same error文件数量。
 * @author huangkaifeng
 *
 */
public class ExtractTagSnapshotIntoSDKFiles {
	
	public static void main(String args[]){
		String newTagFilePathRoot = "D:/Workspace/Android_Diff/SDK_Files_15-26";
		
		String tagStr = "android-4.4_r1";
		int versionNum = 19;
		String rootFilePath = newTagFilePathRoot+"/android-"+versionNum;
		File rootFile = new File(rootFilePath);
		rootFile.mkdirs();
		AndroidTag tag = AndroidTagDAO.selectTagByShortNameAndProjName(tagStr, RepoConstants.platform_frameworks_base_);
		List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectAllFileBySDKVersion(versionNum);
		List<String> wrongedFile = new ArrayList<String>();
		List<String> diffFile = new ArrayList<String>();
		List<String> testFile = new ArrayList<String>();
		JGitTagCommand jCmd = RepositoryHelper.getInstance1().myCmd;
		RevCommit rc = jCmd.revCommitOfTag(tag.getTagShaId());
		SDKFileToRepoFilePath ins = new SDKFileToRepoFilePath(rc.getName(),jCmd);
		for (AndroidSDKJavaFile file : mList) {
			if(file.getFileName().endsWith("Test.java")||file.getSubSubCategoryPath().startsWith("\\android\\test\\")){
				testFile.add(file.getSubSubCategoryPath());
				continue;
			}
			String res = ins.checkFileInRepo(rootFilePath,file,tagStr);
//			System.out.println(res);
			if("YES".equals(res)){
				continue;
			}else if("NO".equals(res)){
				diffFile.add(file.getSubSubCategoryPath());
			}else if("ERROR".equals(res)){
				wrongedFile.add(file.getSubSubCategoryPath());
			}
		}
		System.out.println("-----------------------------------------");
		System.out.println("Sum:"+mList.size());
		System.out.println("Path not correct:"+wrongedFile.size());
		System.out.println("CommitFile Diffrent："+diffFile.size());
		System.out.println("Test CommitFile:"+testFile.size());
	}

}
