package edu.fdu.se.main.androidrepo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import edu.fdu.se.git.JGitCommand;
/**
 * generate git diff command that facilitates viewing diff
 * @author huangkaifeng
 *
 */
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
		String out = "C:/Users/huangkaifeng/Desktop/10-20_Commits/android_sdk_commits_commit_msg_contains_add_null_check-fast peer.txt";
		FastPeerCommit fpc = new FastPeerCommit(file,out);
		fpc.output();
	}
	public void output(){
		cmd = new JGitCommand("D:\\Workspace\\Android_Diff\\Android_Official_Framework_Repo\\base\\.git");
		try {
			FileInputStream fis=new FileInputStream(this.commitFilePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis)); 
			FileOutputStream fos=new FileOutputStream(this.filteredCommits);
			int count=1;
			String str = null;  
			String msg = null;
	        while((str = bufferedReader.readLine()) != null)  
	        {
	        	msg = bufferedReader.readLine();
	        	fos.write(String.valueOf(count).getBytes());
	        	count++;
	        	fos.write("\n".getBytes());
	        	fos.write(str.getBytes());
            	fos.write("\n".getBytes());
            	fos.write(msg.getBytes());
            	fos.write("\n".getBytes());
            	Map<String,Map<String,List<String>>> data = cmd.getCommitParentMappedFileList(str); 

	            String[] parentCommitsString = cmd.getCommitParents(str);
	            for(String tmp : parentCommitsString){
	            	Map<String,List<String>> parentChangedList = data.get(tmp);
	            	List<String> changedFileList = parentChangedList.get("modifiedFiles");
	            	for(String s:changedFileList){
	            		if(s.startsWith("core/java/android")&&s.endsWith("java")){
	            			String line = "git diff " + tmp + " "+str + " -- "+ s + "\n";
	    	            	fos.write(line.getBytes());
	            		}
	            	}
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
