package edu.fdu.se.main.groundtruth;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.JGitRepositoryCommand;
import edu.fdu.se.git.JGitRepositoryManager;
import edu.fdu.se.main.gitgui.RepoDataHelper;

public class ValidateGroundTruthFinder {
	
	public static void main(String args[]){
		String filePath = "services/core/java/com/android/server/ConnectivityService.java";
		String commitId = "964c76b368889e82b820493f140aa91f66f76a92";
		String tagCommitId = "0ac6a8c648fb4ff5060c438f4f437aa9d3db49a0";
		String a = RepoDataHelper.getInstance().getFileContent(commitId, filePath);
		String b = RepoDataHelper.getInstance().getFileContent(tagCommitId, filePath);
		FileWriter.writeInAll("D:/a", a);
		FileWriter.writeInAll("D:/b", b);
//		List<RevCommit> commitList = new ArrayList<RevCommit>();
//		JGitRepositoryManager.getBaseCommand().walkRepoToTagFromBranchName("", tagName, commitList);
	}

}
