package edu.fdu.se.gitrepo;

import java.io.File;
import java.util.Map;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitTagCommand;
/**
 * SDKFile Path in, repository file out
 * @author huangkaifeng
 *
 */
public class SDKFileToRepoFilePath {

	public static Map<String,String> tagMap;
	public static String correctKey;

	public static String getCorrectKey(AndroidSDKJavaFile file){
		String key = null;
		String subsubPath = file.getSubSubCategoryPath();
		if(subsubPath.startsWith("\\android\\bluetooth\\client")){
			key = RepoConstants.platform_frameworks_opt_bluetooth_;
		}
		if(subsubPath.startsWith("\\android\\bordeaux")){
			key = RepoConstants.platform_frameworks_ml_;
		}
		if(subsubPath.startsWith("\\android\\databinding")){
			key = RepoConstants.platform_frameworks_data_binding_;
		}
		//TODO
		switch(file.getSubSubCategoryPath()){
		default:
			key = RepoConstants.platform_frameworks_base_;
			break;
		}
		return key;
	}

	
	public static String checkFileInRepo(AndroidSDKJavaFile file){
		//choose one exact cmd
		String filePath = file.getFileFullPath();
		// fileP ->  cmd key -> tagStr 
		//	           cmd Key -> full path
		correctKey = getCorrectKey(file);
		JGitTagCommand correctCmd = JGitRepositoryManager.getCommand(correctKey);
		String tagStr = null;
		if(tagMap.containsKey(correctKey)){
			tagStr = tagMap.get(correctKey);
		}else{
			System.err.println("ERROR no repo of revision");
			return "ERROR";
		}
		String commitHash = correctCmd.commitIdOfTag(tagStr);
		String path = getCorrectPath(file);
		File localFile = new File(filePath);
		long length = localFile.length();
		byte[] gitFile = null;
		try{
			gitFile = correctCmd.extract(path, commitHash);
		}catch(Exception e){
			return "ERROR";
		}
		if(gitFile.length==length){
			return "YES";
		}else{
			return "NO";
		}
	}
	
	public static String getCorrectPath(AndroidSDKJavaFile file){
		String gitPath = null;
		String subFilePath = file.getSubSubCategoryPath();
		switch(file.getSubSubCategory()){
		case "telephony":
			gitPath = "telephony/java"+ subFilePath.replace('\\', '/');break;
		case "opengl":
			gitPath = "opengl/java"+subFilePath.replace('\\', '/');break;
		case "net":
			gitPath = "wifi/java"+subFilePath.replace('\\', '/');break;
		case "drm":
			gitPath = "drm/java"+subFilePath.replace('\\', '/');break;
		case "location":
			gitPath = "location/java"+subFilePath.replace('\\', '/');break;
		case "telecom":
			gitPath = "telecomm/java"+subFilePath.replace('\\', '/');break;
		case "sax":
			gitPath = "sax/java" +subFilePath.replace('\\', '/');break;
		case "graphics":
			gitPath = "graphics/java" +subFilePath.replace('\\', '/');break;
		case "media":
			gitPath = "media/java" +subFilePath.replace('\\', '/');break;
		case "security":
			gitPath = "keystore/java"+subFilePath.replace('\\', '/');break;
		case "renderscript":
			gitPath = "rs/java"+subFilePath.replace('\\', '/');break;
		default:
			gitPath = "core/java"+subFilePath.replace('\\','/');
		}
		return gitPath;
	}

}
