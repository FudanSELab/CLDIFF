package edu.fdu.se.main.androidrepo.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.git.SDKFileToRepoFilePath;

public class ExtractTagSnapshotIntoSDKFiles {
	
	public static void main(String args[]){
		String tagStr = "android-8.0.0_r1";
		int versionNum = 26;
		AndroidTag tag = AndroidTagDAO.selectTagByShortNameAndProjName(tagStr, RepoConstants.platform_frameworks_base_);
		List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectAllFileBySDKVersion(versionNum);
		List<String> wrongedFile = new ArrayList<String>();
		List<String> diffFile = new ArrayList<String>();
		for (AndroidSDKJavaFile file : mList) {
			String res = SDKFileToRepoFilePath.checkFileInRepo(file);
			if("YES".equals(res)){
				continue;
			}else if("NO".equals(res)){
				diffFile.add(file.getSubSubCategoryPath());
			}else if("ERROR".equals(res)){
				wrongedFile.add(file.getSubSubCategoryPath());
			}
		}
		System.out.println("-----------------------------------------");
		System.out.println(mList.size());
		System.out.println(wrongedFile.size());
		System.out.println(diffFile.size());
	}

}
