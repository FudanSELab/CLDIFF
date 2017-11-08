package edu.fdu.se.androidapiclasses;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;

public class GenerateDiffCmd {
	public static void main(String args[]){
		try {
			FileInputStream fis = new FileInputStream("C:/Users/huangkaifeng/Desktop/NTU-Summer/10-1/gt100.txt");
			FileOutputStream fos = new FileOutputStream("C:/Users/huangkaifeng/Desktop/NTU-Summer/10-1/gt100_gitdiff_cmds.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			int cnt=1;
			while((line = br.readLine())!=null){
				String[] data = line.split(" ");
				String[] data2 = data[0].split("\\.");
				String fileName = data2[data2.length-1]+".java";
				System.out.println(fileName);
				List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectByFileName(fileName);
				if(mList.size()==0||mList.size()==1) 
					continue;
				fos.write(String.valueOf(cnt).getBytes());
				fos.write("\n".getBytes());
				cnt++;
				for(int i=0;i<mList.size()-1;i++){
					String cur = mList.get(i).getFileFullPath();
					String next = mList.get(i+1).getFileFullPath();
					String out = "git diff ";
					String result = out + cur.replace('\\', '/') +" "+ next.replace('\\', '/')+'\n';
					fos.write(result.getBytes());
				}
				fos.write("\n".getBytes());
				
				
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
