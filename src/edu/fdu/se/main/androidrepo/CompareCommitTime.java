package edu.fdu.se.main.androidrepo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.annotations.Property;

import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidRepoCommitDAO;
import edu.fdu.se.git.CommitTagVisitor;
import edu.fdu.se.git.JGitCommand;
/**
 * browse Android SDK tag and get its time
 * input: revision id
 * output: first android release tag that newer than it 
 * @author huangkaifeng
 *
 */
public class CompareCommitTime {
	
	private JGitCommand cmd;
	CommitTagVisitor ctv;
	public static void main(String args[]){
//		CompareCommitTime astt = new CompareCommitTime();
//		astt.walkTags();
//		comparTimePrompt();
		String commitId = "718f403b507fe141fed9a031735fb09aa2ebcf19";
		compareTime(commitId);
	}
	public static void compareTime(String commitId){
		JGitCommand cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		int commitTime = cmd.readCommitTime(commitId);
		Long time = new Long(commitTime*1000L);
		Date date = new Date(time);
		List<AndroidRepoCommit> mList =AndroidRepoCommitDAO.selectBranchHeads(date);
		for(AndroidRepoCommit item:mList){
			System.out.println(item.getBranchName());
		}
		
	}
	
	
//	public void walkTags(){
		
//		
//		ctv =new CommitTagVisitor(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+ "/android_sdk_tag_info.txt");
//		cmd.walkAllTags(ctv);
		
//	}
//	public void comparTimePrompt(){
//			int time = cmd.readCommitTime(line);
//			ctv.compareCommitTime(time);
//		
//	}
	// 

}
