package edu.fdu.se.main.androidrepo;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.CommitTagVisitor;
import edu.fdu.se.git.CommitVisitorDB;
import edu.fdu.se.git.CommitVisitorLog;
import edu.fdu.se.git.JGitCommand;

/**
 * browser git repo, filter commits with android sdk file, write to output dir.
 * 1.commit输出到文本文件  2.tag输出到文本文件
 * @author huangkaifeng
 *
 */
public class BrowseAndroidRepo {
	
	public static void main(String[] args) {
		writeCommitsToDB();
	}
	/**
	 * 1
	 */
	private static void writeSDKCommitsToFile(){
		JGitCommand cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		CommitVisitorLog cv = new CommitVisitorLog(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+"/android_sdk_commits3.txt");
		cmd.walkRepoFromBackwards(cv);
	}
	/**
	 * 
	 */
	private static void writeCommitsToDB(){
		JGitCommand cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		CommitVisitorDB cv = new CommitVisitorDB();
		cmd.walkRepoFromBackwardsToDB(cv);
	}
	private static void walkAllTags(){
		JGitCommand cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		CommitTagVisitor cv = new CommitTagVisitor(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+"/android_sdk_commits2.txt");
		cmd.walkAllTags(cv);
	}
	private static void walkRepoFromBackwardsAndSortOutBranches(){
		JGitCommand cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		CommitVisitorDB cv = new CommitVisitorDB();
		cmd.walkRepoBackwardDividedByBranch(cv);
		
	}
	
}
