package edu.fdu.se.extractor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommitFilter {
	public String commitFilePath;
	public String filteredCommits;
	public CommitFilter(String commitListPath,String filteredCommits){
		commitFilePath=commitListPath;
		this.filteredCommits=filteredCommits;
	}
	
	public void output(){
		try {
			FileInputStream fis=new FileInputStream(this.commitFilePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis)); 
			FileOutputStream fos=new FileOutputStream(this.filteredCommits);
			String str = null;  
			String msg = null;
	        while((str = bufferedReader.readLine()) != null)  
	        {
	        	
	            msg = bufferedReader.readLine();
	            int  flag = this.filter(msg);
	            if(flag==1){
	            	fos.write(str.getBytes());
	            	fos.write("\n".getBytes());
	            	fos.write(msg.getBytes());
	            	fos.write("\n".getBytes());
	            }
	        }  
	        fis.close();  
	        bufferedReader.close();  
	        fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int filter(String msg){
		String lowerCase = msg.toLowerCase();
		if(lowerCase.contains("delete")||lowerCase.contains("remove")){
			return 1;
		}
		return 4;
	}
	public static void main(String args[]){
		String src = "C:/Users/huangkaifeng/Desktop/10-20_Commits/android_sdk_commits.txt";
		String dst = "C:/Users/huangkaifeng/Desktop/10-20_Commits/android_sdk_commits_commit_msg_contains_delete_remove.txt";
		CommitFilter cf=new CommitFilter(src,dst);
		cf.output();
	}

}
