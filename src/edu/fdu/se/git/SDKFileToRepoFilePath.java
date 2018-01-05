package edu.fdu.se.git;

import java.io.File;
import java.util.Map;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.bean.AndroidSDKJavaFileTagSnapshot;
import edu.fdu.se.dao.AndroidSDKJavaFileTagSnapshotDAO;
import edu.fdu.se.fileutil.FileWriter;

/**
 * SDKFile Path in, repository file out
 * 
 * @author huangkaifeng
 *
 */
public class SDKFileToRepoFilePath {

	public static Map<String, String> tagMap;
	private String tagCommitSha;
	private JGitTagCommand defaultCommand;

	public SDKFileToRepoFilePath(String commit, JGitTagCommand cmd) {
		this.tagCommitSha = commit;
		this.defaultCommand = cmd;
	}

	public String getDefaultRepositoryNameKeyAndPath(AndroidSDKJavaFile file) {
		String gitPath = null;
		String subFilePath = file.getSubSubCategoryPath();
		String replaceStr = subFilePath.replace('\\', '/');
		switch (file.getSubSubCategory()) {
		case "telephony":
			gitPath = "telephony/java" + replaceStr;
			break;
		case "opengl":
			gitPath = "opengl/java" + replaceStr;
			break;
		case "net":
			gitPath = "wifi/java" + replaceStr;
			break;
		case "drm":
			gitPath = "drm/java" + replaceStr;
			break;
		case "location":
			gitPath = "location/java" + replaceStr;
			break;
		case "telecom":
			gitPath = "telecomm/java" + replaceStr;
			break;
		case "sax":
			gitPath = "sax/java" + replaceStr;
			break;
		case "graphics":
			gitPath = "graphics/java" + replaceStr;
			break;
		case "media":
			if(replaceStr.startsWith("/android/media/effect")){
				gitPath = "media/mca/effect/java" + replaceStr; 
			}else{
				gitPath = "media/java" +  replaceStr;
			}
			break;
		case "security":
			gitPath = "keystore/java" + replaceStr;
			break;
		case "renderscript":
			gitPath = "rs/java" + replaceStr;
			break;
		case "databinding":
			gitPath = null;
			break;
		case "bluetooth":
			if(replaceStr.startsWith("/android/bluetooth/client")){
				gitPath = null;
			}else{
				gitPath = "core/java" + replaceStr;
			}
			break;
		case "bordeaux":
			gitPath = null;
			break;
			
		default:
			gitPath = "core/java" + replaceStr;
		}
		return gitPath;
	}

	public String[] getCorrectRepositoryNameKeyAndPath(AndroidSDKJavaFile file) {
		String key = null;
		String gitPath = null;
		String subFilePath = file.getSubSubCategoryPath();
		String[] result = new String[2];
		String subsubPath = file.getSubSubCategoryPath();
		if (subsubPath.startsWith("\\android\\bluetooth\\client")) {
			key = RepoConstants.platform_frameworks_opt_bluetooth_;
			gitPath = "src" + subFilePath.replace('\\', '/');
		} else if (subsubPath.startsWith("\\android\\bordeaux")) {
			if (subsubPath.startsWith("\\android\\bordeaux\\services")) {
				gitPath = "bordeaux/service/src" + subFilePath.replace('\\', '/');
			} else if (subsubPath.startsWith("\\android\\bordeaux\\leanring")) {
				gitPath = subFilePath.replace('\\', '/');
			}
			key = RepoConstants.platform_frameworks_ml_;

		} else if (subsubPath.startsWith("\\android\\databinding")) {
			key = RepoConstants.platform_frameworks_data_binding_;
			gitPath = "data-binding/baseLibrary/src/main/java" + subFilePath.replace('\\', '/');
		} else {
			// TODO
			key = RepoConstants.platform_frameworks_base_;
			String replaceStr = subFilePath.replace('\\', '/');
			switch (file.getSubSubCategory()) {
			case "telephony":
				gitPath = "telephony/java" + replaceStr;
				break;
			case "opengl":
				gitPath = "opengl/java" + replaceStr;
				break;
			case "net":
				gitPath = "wifi/java" + replaceStr;
				break;
			case "drm":
				gitPath = "drm/java" + replaceStr;
				break;
			case "location":
				gitPath = "location/java" + replaceStr;
				break;
			case "telecom":
				gitPath = "telecomm/java" + replaceStr;
				break;
			case "sax":
				gitPath = "sax/java" + replaceStr;
				break;
			case "graphics":
				gitPath = "graphics/java" + replaceStr;
				break;
			case "media":
				if(replaceStr.startsWith("/android/media/effect")){
					gitPath = "media/mca/effect/java" + replaceStr; 
				}else{
					gitPath = "media/java" +  replaceStr;
				}
				break;
			case "security":
				gitPath = "keystore/java" + subFilePath.replace('\\', '/');
				break;
			case "renderscript":
				gitPath = "rs/java" + subFilePath.replace('\\', '/');
				break;
			default:
				gitPath = "core/java" + subFilePath.replace('\\', '/');
			}
		}
		result[0] = key;
		result[1] = gitPath;
		return result;
	}
	
	public String checkFileInRepo(String rootFilePath, AndroidSDKJavaFile file, String tagStr) {
		// check本地是否存在 如果存在 pass
		String replaceChar = file.getSubSubCategoryPath().replace('\\', '/');
		int index = replaceChar.lastIndexOf('/');
		File newFile = new File(rootFilePath + "/" + replaceChar.substring(0, index));
		if (!newFile.exists()) {
			// 意味着文件删除之后，数据库也要清空了
			// D:/Workspace/Android_Diff/SDK_Files_15-26/android-26/android/appwidgetAppWidgetHost.java
			newFile.mkdirs();
		}
		
		String writeFileStr = rootFilePath + replaceChar.substring(0, index) + "/" + file.getFileName();
		File writeFile = new File(writeFileStr);
		
		if(writeFile.exists()){
			return "YES";
		}
		
		String truePath = getDefaultRepositoryNameKeyAndPath(file);
		if(truePath == null){
			return "ERROR";
		}
		// System.out.println(truePath);
		File localFile = new File(file.getFileFullPath());
		long length = localFile.length();
		byte[] gitFile = null;
		try {
			gitFile = this.defaultCommand.extract(truePath, this.tagCommitSha);
		} catch (Exception e) {
			System.out.println("Path Incorrect：" + file.getSubSubCategoryPath() + "---------" + truePath);
			return "ERROR";
		}
		String flag = null;
		if (gitFile.length == length) {
			flag = "YES";
		} else {
			flag = "NO";
		}
		
		System.err.println("write new File");
		
		FileWriter.writeInAll(writeFile, gitFile);
		AndroidSDKJavaFileTagSnapshot a = new AndroidSDKJavaFileTagSnapshot(0, file, tagStr, flag);
		AndroidSDKJavaFileTagSnapshotDAO.insert(a);
		return flag;

	}

}
