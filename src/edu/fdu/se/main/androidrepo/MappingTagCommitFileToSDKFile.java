package edu.fdu.se.main.androidrepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.bean.AndroidSDKJavaFileTagSnapshot;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
import edu.fdu.se.dao.AndroidSDKJavaFileTagSnapshotDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.git.SDKFileToRepoFilePath;
/**
 * sdk的文件 map到repo中
 * @author huangkaifeng
 *
 */
public class MappingTagCommitFileToSDKFile {
	/**
	 * 输入tag名 输出tag对应git和sdk文件的对应关系
	 */
	public static void run() {
		String tagStr = "android-8.0.0_r1";
		int versionNum = 26;
		AndroidTag mTagList = AndroidTagDAO.selectTagByShortNameAndProjName(tagStr, RepoConstants.platform_frameworks_base_);
		Map<String,String> tagMap = new HashMap<String,String>();
//		for(AndroidTag item :mTagList){
//			tagMap.put(item.getProjectName(), item.getTagShaId());
//		}
		SDKFileToRepoFilePath.tagMap = tagMap;
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

	public static void main(String args[]) {
//		run();
		run2();
	}
	
	public static void run2(){
//		List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectAllFileBySDKVersion(25);
//		for(AndroidSDKJavaFile item:mList){
//			AndroidSDKJavaFileTagSnapshot a = new AndroidSDKJavaFileTagSnapshot(0,item,"a","b");
//			AndroidSDKJavaFileTagSnapshotDAO.insert(a);
//			break;
//		}
		
//		List<AndroidSDKJavaFileTagSnapshot> list =  AndroidSDKJavaFileTagSnapshotDAO.selectAll();
//		for(AndroidSDKJavaFileTagSnapshot item :list){
//			System.out.println(item.getIsChecked()+" "+item.getTagStr());
//			System.out.println(item.getAndroidSDKJavaFile().getFileFullPath());
//		}
		
	}

}
