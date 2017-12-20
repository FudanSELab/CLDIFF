package edu.fdu.se.gitrepo;

import java.util.Map;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitTagCommand;

public class Test {
	public static void main(String args[]){
		JGitTagCommand correctCmd = JGitRepositoryManager.getCommand(RepoConstants.platform_frameworks_ml_);
		String filePath = "\\android\\bordeaux\\services\\TimeStatsAggregator.java";
		String tagId = "3c6d4648f6687c7fedf260fe72a8de26e868bbd0";
		RevCommit rev = correctCmd.revCommitOfTag(tagId);
		System.out.println(rev);
	}

}
