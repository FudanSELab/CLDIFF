package edu.fdu.se.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.fdu.se.git.JGitCommand;

public class FastPeerCommit {
	public FastPeerCommit(String path,String out){
		this.commitFilePath = path;
		this.filteredCommits=out;
	}
	private String commitFilePath;
	private String filteredCommits;
	private JGitCommand cmd;
	public static void main(String args[]){
		String file = "C:/Users/huangkaifeng/Desktop/10-20_Commits/android_sdk_commits_commit_msg_contains_add_null_check.txt";
		String out = "C:/Users/huangkaifeng/Desktop/10-20_Commits/android_sdk_commits_commit_msg_contains_add_null_check-add cmd.txt";
		FastPeerCommit fpc = new FastPeerCommit(file,out);
		fpc.output();
	}
	public void output(){
		cmd = new JGitCommand("D:\\Workspace\\Android_Diff\\Android_Official_Framework_Repo\\base\\.git");
		try {
			FileInputStream fis=new FileInputStream(this.commitFilePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis)); 
			FileOutputStream fos=new FileOutputStream(this.filteredCommits);
			String str = null;  
			String msg = null;
	        while((str = bufferedReader.readLine()) != null)  
	        {
	        	msg = bufferedReader.readLine();
            	
	        	fos.write(str.getBytes());
            	fos.write("\n".getBytes());
            	fos.write(msg.getBytes());
            	fos.write("\n".getBytes());
	        	
	            
	            String[] parentCommitsString = cmd.getCommitParents(str);
	            for(String tmp : parentCommitsString){
	            	String line = "git diff " + tmp + " "+str + "\n";
	            	fos.write(line.getBytes());
	            }

            	fos.write("\n".getBytes());
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

}
