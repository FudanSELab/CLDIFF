package edu.fdu.se.main.androidrepo;

import java.util.Scanner;

import org.apache.ibatis.annotations.Property;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
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
		cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		ctv =new CommitTagVisitor(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+ "/android_sdk_tag_info.txt");
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
