package edu.fdu.se.main;

import edu.fdu.se.git.CommitTagVisitor;
import edu.fdu.se.git.JGitCommand;

public class AndroidSDKTagTime {
	
	private JGitCommand cmd;
	public static void main(String args[]){
		AndroidSDKTagTime astt = new AndroidSDKTagTime();
		astt.walkTags();
	}
	public void walkTags(){
		cmd = new JGitCommand("D:\\Workspace\\Android_Diff\\Android_Official_Framework_Repo\\base\\.git");
		cmd.walkAllTags(new CommitTagVisitor("C:/Users/huangkaifeng/Desktop/10-20_Commits/android_sdk_tag_info.txt"));
	}

}
