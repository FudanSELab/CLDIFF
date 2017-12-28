package edu.fdu.se.main.androidrepo;

import java.util.Date;
import java.util.List;


import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidRepoCommitDAO;
import edu.fdu.se.git.JGitCommand;
/**
 * S
 * input: revision id
 * output: first android release tag that newer than it 
 * @author huangkaifeng
 *
 */
public class CompareCommitTime {
	
	private JGitCommand cmd;
	public static void main(String args[]){
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
	
	


}
