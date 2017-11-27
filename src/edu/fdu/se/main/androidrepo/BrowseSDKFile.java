package edu.fdu.se.main.androidrepo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
/**
 * Load Android SDK into database table: android_sdk_java_file
 * 把sdk的文件录入数据库
 * @author huangkaifeng
 *
 */
public class BrowseSDKFile {
	static List<AndroidSDKJavaFile> insertationList = new ArrayList<AndroidSDKJavaFile>();
	static String rootDir;
	static int sdkVersion;
	public static void filePathToDb(String absPath){
		String[] data = absPath.split(rootDir);
		System.out.println(absPath);
		String tail = data[1];
		String[] dirs =tail.split("\\\\");
		AndroidSDKJavaFile asjf = new AndroidSDKJavaFile();
		asjf.setSdkVersion(sdkVersion);
		asjf.setFileFullPath(absPath);
		if(dirs.length<=2){
			asjf.setFileName(dirs[1]);
		}else if(dirs.length==3){
			asjf.setSubCategory(dirs[1]);
			asjf.setFileName(dirs[2]);
			
		}else if(dirs.length>3){
			asjf.setFileName(dirs[dirs.length-1]);
			asjf.setSubCategory(dirs[1]);
			asjf.setSubSubCategory(dirs[2]);
			asjf.setSubSubCategoryPath(tail);
		}
		insertationList.add(asjf);
		
	}
	
	public static void browse(File f){
		if(f.isDirectory()){
			File[] fileList = f.listFiles();
			for(File f2:fileList){
				browse(f2);
			}
		}else{
			filePathToDb(f.getAbsolutePath());
		}
	}
	/**
	 * browse android sdk dir and put all the entry into database
	 * @param args
	 */
	public static void main(String args[]){
		String path = ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_SDK_PATH)+"sources\\android-";
		int i=3;
		for(i=3;i<=25;i++){
			sdkVersion = i;
			rootDir = "android-"+String.valueOf(i);
			String browsePath = path+String.valueOf(i);
			File ff = new File(browsePath);
			browse(ff);
		}
		List<AndroidSDKJavaFile> tempList=new ArrayList<AndroidSDKJavaFile>();
		for(AndroidSDKJavaFile asjf:insertationList){
			
			tempList.add(asjf);
			if(tempList.size()>1000){
				AndroidSDKJavaFileDAO.insertBatch(tempList);
				tempList.clear();
			}
			
			
		}
		AndroidSDKJavaFileDAO.insertBatch(tempList);
		
	}

}
