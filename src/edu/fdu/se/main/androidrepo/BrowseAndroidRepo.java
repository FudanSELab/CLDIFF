package edu.fdu.se.main.androidrepo;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.CommitTagVisitor;
import edu.fdu.se.git.CommitVisitor;
import edu.fdu.se.git.JGitCommand;

/**
 * browser git repo, filter commits with android sdk file, write to output dir.
 * 1.commit输出到文本文件  2.tag输出到文本文件
 * @author huangkaifeng
 *
 */
public class BrowseAndroidRepo {
	
	public static void main(String[] args) {
		walkRepoFromBackwards();
		walkAllTags();
	}
	private static void walkRepoFromBackwards(){
		JGitCommand cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		CommitVisitor cv = new CommitVisitor(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+"/android_sdk_commits2.txt");
		cmd.walkRepoFromBackwards(cv);
		cv.close();
	}
	private static void walkAllTags(){
		JGitCommand cmd = new JGitCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		CommitTagVisitor cv = new CommitTagVisitor(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+"/android_sdk_commits2.txt");
		cmd.walkAllTags(cv);
	}
	private static void walkRepoFromBackwardsAndSortOutBranches(){
		//TODO
	}
	
}
