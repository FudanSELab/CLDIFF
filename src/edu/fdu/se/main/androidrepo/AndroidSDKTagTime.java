package edu.fdu.se.main.androidrepo;

import java.util.Scanner;

import edu.fdu.se.git.CommitTagVisitor;
import edu.fdu.se.git.JGitCommand;
/**
 * browse Android SDK tag and get its time
 * input: revision id
 * output: first android release tag that newer than it 
 * @author huangkaifeng
 *
 */
public class AndroidSDKTagTime {
	
	private JGitCommand cmd;
	CommitTagVisitor ctv;
	public static void main(String args[]){
		AndroidSDKTagTime astt = new AndroidSDKTagTime();
		astt.walkTags();
		astt.comparTimePrompt();
	}
	public void walkTags(){
		cmd = new JGitCommand("D:\\Workspace\\Android_Diff\\Android_Official_Framework_Repo\\base\\.git");
		ctv =new CommitTagVisitor("C:/Users/huangkaifeng/Desktop/10-20_Commits/android_sdk_tag_info.txt");
		cmd.walkAllTags(ctv);
		
	}
	public void comparTimePrompt(){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			String line = sc.nextLine();
			int time = cmd.readCommitTime(line);
			ctv.compareCommitTime(time);
		}
		sc.close();
		
	}

}
