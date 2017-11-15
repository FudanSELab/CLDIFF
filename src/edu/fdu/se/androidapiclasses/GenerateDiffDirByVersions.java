package edu.fdu.se.androidapiclasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.fdu.se.bean.AndroidSDKJavaFile;
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
			String dir = "C:/Users/huangkaifeng/Desktop/11-8/DiffDirByVersion";
			makeDir(dir);
			FileInputStream fis = new FileInputStream("C:/Users/huangkaifeng/Desktop/NTU-Summer/10-1/gt100.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			while((line = br.readLine())!=null){
				String[] data = line.split(" ");
				String[] data2 = data[0].split("\\.");
				String fileName = data2[data2.length-1]+".java";
				System.out.println(fileName);
				List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectByFileNameAndFilterSupportPackage(fileName);
				if(mList.size()==0||mList.size()==1) 
					continue;
				for(int i=0;i<mList.size()-1;i++){
					String cur = mList.get(i).getFileFullPath();
					String next = mList.get(i+1).getFileFullPath();
					if(isSameSize(cur,next)){
						continue;
					}
					copyIntoDir(dir,mList.get(i),mList.get(i+1));
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
