package edu.fdu.se.git;

import java.io.File;
import java.util.Map;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
/**
 * SDKFile Path in, repository file out
 * @author huangkaifeng
 *
 */
public class SDKFileToRepoFilePath {

	public static Map<String,String> tagMap;

	
	public static String[] getCorrectRepositoryNameKeyAndPath(AndroidSDKJavaFile file){
		String key = null;
		String gitPath = null;
		String subFilePath = file.getSubSubCategoryPath();
		String[] result = new String[2];
		String subsubPath = file.getSubSubCategoryPath();
		if(subsubPath.startsWith("\\android\\bluetooth\\client")){
			key = RepoConstants.platform_frameworks_opt_bluetooth_;
			gitPath = "src" + subFilePath.replace('\\','/');
		}
		else if(subsubPath.startsWith("\\android\\bordeaux")){
			if(subsubPath.startsWith("\\android\\bordeaux\\services")){
				gitPath = "bordeaux/service/src"+subFilePath.replace('\\','/');
			}else if(subsubPath.startsWith("\\android\\bordeaux\\leanring")){
				gitPath = subFilePath.replace('\\','/');
			}
			key = RepoConstants.platform_frameworks_ml_;
			
		}
		else if(subsubPath.startsWith("\\android\\databinding")){
			key = RepoConstants.platform_frameworks_data_binding_;
			gitPath = "data-binding/baseLibrary/src/main/java"+subFilePath.replace('\\','/');
		}else{
			//TODO
			key = RepoConstants.platform_frameworks_base_;
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
		}
		result[0] = key;
		result[1] = gitPath;
		return result;
	}

	
	public static String checkFileInRepo(AndroidSDKJavaFile file){
		System.out.println(file.getSubSubCategoryPath());
		String[] data = getCorrectRepositoryNameKeyAndPath(file);
		String correctKey = data[0];
		String truePath = data[1];
		JGitTagCommand correctCmd = JGitRepositoryManager.getBaseCommand();

		String tagStr = null;
		if(tagMap.containsKey(correctKey)){
			tagStr = tagMap.get(correctKey);
		}else{
			System.err.println("ERROR no repo of revision");
			return "ERROR";
		}
		RevCommit commit = correctCmd.revCommitOfTag(tagStr);
		File localFile = new File(file.getFileFullPath());
		long length = localFile.length();
		byte[] gitFile = null;
		try{
			gitFile = correctCmd.extract(truePath, commit.getName());
		}catch(Exception e){
			System.out.println("Path Incorrect："+file.getSubSubCategoryPath()+"---------"+truePath);
			return "ERROR";
		}
		if(gitFile.length==length){
			return "YES";
		}else{
//			System.out.println("Not Equal"+ file.getSubSubCategoryPath()+"-----------"+truePath);
			return "NO";
		}
	}
	
	

}
