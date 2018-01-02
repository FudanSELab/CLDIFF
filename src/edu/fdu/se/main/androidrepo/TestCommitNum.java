package edu.fdu.se.main.androidrepo;

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.git.RepositoryHelper;

public class TestCommitNum {
	
	public static void main(String args[]){
		JGitTagCommand cmd = (JGitTagCommand) RepositoryHelper.getInstance1().myCmd;
		AndroidTag at = AndroidTagDAO.selectTagByShortNameAndProjName("android-4.0.3_r1", RepoConstants.platform_frameworks_base_);
		String sha = at.getTagShaId();
		cmd.walkRepoFromBackwardsStartFromTag(sha);
	}

}
