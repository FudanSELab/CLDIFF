package edu.fdu.se.main.androidrepo;

import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.JGitRepositoryCommand;
import edu.fdu.se.main.gitgui.JGitRepoManager;

public class ValidateGroundTruthFinder {
	
	public static void main(String args[]){
		String filePath = "services/core/java/com/android/server/ConnectivityService.java";
		String commitId = "964c76b368889e82b820493f140aa91f66f76a92";
		String tagCommitId = "0ac6a8c648fb4ff5060c438f4f437aa9d3db49a0";
		String a = JGitRepoManager.getInstance().getFileContent(commitId, filePath);
		String b = JGitRepoManager.getInstance().getFileContent(tagCommitId, filePath);
		FileWriter.writeInAll("D:/a", a);
		FileWriter.writeInAll("D:/b", b);
	}

}
