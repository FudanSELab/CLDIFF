package edu.fdu.se.gitrepo;

import edu.fdu.se.bean.AndroidSDKJavaFile;
/**
 * SDKFile Path in, repository file out
 * @author huangkaifeng
 *
 */
public class SDKFileToRepoFilePath {
	public SDKFileToRepoFilePath(){
		
	}
	
	public static String v26PathMapping(AndroidSDKJavaFile file){
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
		case "bluetooth":
//			System.err.println("Bluetooth Dir: android_repo_path2/");
			// bluetooth
			break;
		case "databinding":
			//databingding 
			break;
		case "system":
			// android / platform / libcore 
			break;
		case "renderscript":
			gitPath = "rs/java"+subFilePath.replace('\\', '/');break;
		default:
			gitPath = "core/java"+subFilePath.replace('\\','/');
		}
		return gitPath;
	}

}
