package edu.fdu.se.main.popularsdkclasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;

public class GenerateDiffDirByVersions {
	public static boolean isSameSize(String a,String b){
		File fa = new File(a);
		File fb = new File(b);
		return fa.length()==fb.length();
	}
	public static boolean copy(String src,String dst){
		try {
			FileInputStream fis = new FileInputStream(src);
			FileOutputStream fos = new FileOutputStream(dst);
			byte[] buffer =new byte[1024];
			int n;
			while((n=fis.read(buffer))!=-1){
				fos.write(buffer,0,n);
			}
			fis.close();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean makeDir(String path){
		File rootPath =new File(path);
		if(!rootPath.exists())
			rootPath.mkdirs();
		for(int i=3;i<=24;i++){
			String subDir = String.valueOf(i+1)+"-"+String.valueOf(i);
			File subDirPathPrev = new File(rootPath+"/"+subDir+"/prev");
			File subDirPathCurr = new File(rootPath+"/"+subDir+"/curr");
			if(!subDirPathPrev.exists())
				subDirPathPrev.mkdirs();
			if(!subDirPathCurr.exists())
				subDirPathCurr.mkdirs();
		}
		return true;
	}
	public static void copyIntoDir(String fileNamePath,AndroidSDKJavaFile comparePrev,AndroidSDKJavaFile compareCurr){
		String subDir = "/" + String.valueOf(compareCurr.getSdkVersion())+"-"+String.valueOf(comparePrev.getSdkVersion());
		String prevPath = fileNamePath+subDir +  "/prev";
		String currPath = fileNamePath+subDir + "/curr";
		if(!new File(prevPath).exists())
			return;
		if(!new File(currPath).exists())
			return;
		copy(comparePrev.getFileFullPath(),prevPath+"/"+comparePrev.getFileName());
		copy(compareCurr.getFileFullPath(),currPath+"/"+compareCurr.getFileName());
	}
	public static void main(String args[]){
		try {
			String dir = ProjectProperties.getInstance().getValue(PropertyKeys.GENERATE_DIFF_CMD_OUTPUT_DIR);
			makeDir(dir);
			FileInputStream fis = new FileInputStream(ProjectProperties.getInstance().getValue(PropertyKeys.GENERATE_DIFF_CMD_OUTPUT_DIR)+"/gt100.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			while((line = br.readLine())!=null){
				String[] data = line.split(" ");
				String[] data2 = data[0].split("\\.");
				String fileName = data2[data2.length-1]+".java";
				System.out.println(fileName);
				List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectByFileNameOnAndroidSubDirectoryAndFilterSupportPackage(fileName);
				if(mList.size()==0||mList.size()==1) 
					continue;
				Map<String,List<AndroidSDKJavaFile>> mMap = new HashMap<String,List<AndroidSDKJavaFile>>();
				for(AndroidSDKJavaFile item:mList){
					if(mMap.containsKey(item.getSubSubCategory())){
						mMap.get(item.getSubSubCategory()).add(item);
					}else{
						List<AndroidSDKJavaFile> tmpList = new ArrayList<AndroidSDKJavaFile>();
						tmpList.add(item);
						mMap.put(item.getSubSubCategory(), tmpList);
					}
				}
				for(String subsub:mMap.keySet()){
					List<AndroidSDKJavaFile> tmpList = mMap.get(subsub);
					tmpList.sort(new Comparator<AndroidSDKJavaFile>(){
						@Override
						public int compare(AndroidSDKJavaFile arg0, AndroidSDKJavaFile arg1) {
							return arg0.getSdkVersion()-arg1.getSdkVersion();
						}
					});
					for(int i=0;i<tmpList.size()-1;i++){
						String cur = tmpList.get(i).getFileFullPath();
						String next = tmpList.get(i+1).getFileFullPath();
						if(isSameSize(cur,next)){
							continue;
						}
						copyIntoDir(dir,tmpList.get(i),tmpList.get(i+1));
					}
				}
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
