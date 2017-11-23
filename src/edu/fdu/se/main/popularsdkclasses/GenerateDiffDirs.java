package edu.fdu.se.main.popularsdkclasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;

public class GenerateDiffDirs {
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
		File newDir = new File(path);
		if(!newDir.exists()) newDir.mkdirs();
		File subDir1 = new File(path+"/prev");
		File subDir2 = new File(path+"/curr");
		if(!subDir1.exists())
			subDir1.mkdirs();
		if(!subDir2.exists())
			subDir2.mkdirs();
		return true;
	}
	public static void copyIntoDir(String fileNamePath,AndroidSDKJavaFile comparePrev,AndroidSDKJavaFile compareCurr){
		String cur = compareCurr.getFileFullPath();
		String prev = comparePrev.getFileFullPath();
		String prevPath = fileNamePath+"/prev";
		String currPath = fileNamePath+"/curr";
		String dstFileName = String.valueOf(compareCurr.getSdkVersion())+"-"+String.valueOf(comparePrev.getSdkVersion())+".txt";
		String dstFilePath = prevPath+"/"+dstFileName;
		copy(prev,dstFilePath);
		dstFilePath = currPath+"/"+dstFileName;
		copy(cur,dstFilePath);
		
	}
	public static void main(String args[]){
		try {
			String dir = ProjectProperties.getInstance().getValue(PropertyKeys.GENERATE_DIFF_CMD_OUTPUT_DIR)+"/DiffDir";
			FileInputStream fis = new FileInputStream(ProjectProperties.getInstance().getValue(PropertyKeys.GENERATE_DIFF_CMD_OUTPUT_DIR)+"gt100.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			int cnt=1;
			FileOutputStream fos = new FileOutputStream(dir+"/beyond_compare_path.txt");
			while((line = br.readLine())!=null){
				String[] data = line.split(" ");
				String[] data2 = data[0].split("\\.");
				String fileName = data2[data2.length-1]+".java";
				System.out.println(fileName);
				makeDir(dir+"/"+data2[data2.length-1]);
				List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectByFileName(fileName);
				if(mList.size()==0||mList.size()==1) 
					continue;
				fos.write(String.valueOf(cnt).getBytes());cnt++;
				fos.write(".\n".getBytes());
				String tmp1 = dir+"/"+data2[data2.length-1]+"/curr\n";
				String tmp2 = dir+"/"+data2[data2.length-1]+"/prev\n\n";
				fos.write(tmp1.getBytes());
				fos.write(tmp2.getBytes());
				for(int i=0;i<mList.size()-1;i++){
					String cur = mList.get(i).getFileFullPath();
					String next = mList.get(i+1).getFileFullPath();
					if(isSameSize(cur,next)){
						continue;
					}
					copyIntoDir(dir+"/"+data2[data2.length-1],mList.get(i),mList.get(i+1));
				}
			}
			br.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
