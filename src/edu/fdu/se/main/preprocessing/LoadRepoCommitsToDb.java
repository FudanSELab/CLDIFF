package edu.fdu.se.main.preprocessing;


import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitRepositoryCommand;
import edu.fdu.se.git.RepoConstants;

/**
 * browser git repo, filter commits with android sdk file, write to output dir.
 * 1.commit输出到文本文件  2.tag输出到文本文件
 * @author huangkaifeng
 *
 */
public class LoadRepoCommitsToDb {
	
	public static void main(String[] args) {
//		loadCommitsToDB();
	}	
	/**
	 * 
	 */
	private static void loadCommitsToDB(){
		JGitRepositoryCommand cmd = new JGitRepositoryCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)+RepoConstants.platform_frameworks_base_+"/.git");
		cmd.walkRepoFromBackwardsToDB();
	}
	
}
