package edu.fdu.se.main.androidrepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitCommand;
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
		List<AndroidTag> mTagList = AndroidTagDAO.selectTags(tagStr);
		Map<String,String> tagMap = new HashMap<String,String>();
		for(AndroidTag item :mTagList){
			tagMap.put(item.getProjectName(), item.getTagShaId());
		}
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
//		for (String a : wrongedFile) {
//			System.out.println(a);
//		}
		System.out.println(mList.size());
		System.out.println(wrongedFile.size());
		System.out.println(diffFile.size());
		
	}

	public static void main(String args[]) {
		run();

	}

	public static void test() {
		JGitCommand cmd = new JGitCommand("C:/Users/huangkaifeng/Desktop/testgit/.git");
		String s = "ba399911ef469a3585e123a3a317846219fc071c";
		System.out.println(s);
		byte[] gitFile = cmd.extract("out.txt", s);
		System.out.println(gitFile == null);
		// List<AndroidSDKJavaFile> mList =
		// AndroidSDKJavaFileDAO.selectAllFileBySDKVersion(26);
		// for(AndroidSDKJavaFile file:mList){
		//// // src file
		// String filePath = file.getFileFullPath();
		// File localFile = new File(filePath);
		// long length = localFile.length();
		// String subFilePath = file.getSubSubCategoryPath();
		// String gitPath = "core/java"+subFilePath.replace('\\','/');
		// System.out.println(gitPath);
		// try{
		//
		// System.out.println("Src:"+length);
		// System.out.println("Dst:"+gitFile.length);
		// int cnt =0;
		// if(length != gitFile.length){
		// System.out.println("Error");
		// cnt ++;
		// }
		// }catch(Exception e){
		//// e.printStackTrace();
		// System.err.println("ERR0R Wrong path");
		// }
		//
		// FileOutputStream fos;
		// try {
		// fos = new FileOutputStream(new File("D:/aaa.java"));
		// fos.write(gitFile);
		// fos.close();
		// } catch ( IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
