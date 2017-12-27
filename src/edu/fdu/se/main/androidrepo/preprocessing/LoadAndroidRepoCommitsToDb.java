package edu.fdu.se.main.androidrepo.preprocessing;

import java.io.IOException;

import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.CommitTagVisitor;
import edu.fdu.se.git.CommitVisitorDB;
import edu.fdu.se.git.CommitVisitorLog;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitRepositoryCommand;
import edu.fdu.se.gitrepo.RepoConstants;

/**
 * browser git repo, filter commits with android sdk file, write to output dir.
 * 1.commit输出到文本文件  2.tag输出到文本文件
 * @author huangkaifeng
 *
 */
public class LoadAndroidRepoCommitsToDb {
	
	public static void main(String[] args) {
//		writeCommitsToDB();
	}
	/**
	 * 1
	 */
	private static void writeSDKCommitsToFile(){
		JGitRepositoryCommand cmd = new JGitRepositoryCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
		CommitVisitorLog cv = new CommitVisitorLog(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+"/android_sdk_commits3.txt");
		cmd.walkRepoFromBackwards(cv);
	}
	
	/**
	 * 
	 */
	private static void writeCommitsToDB(){
		JGitRepositoryCommand cmd = new JGitRepositoryCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)+RepoConstants.platform_frameworks_base_+"/.git");
		CommitVisitorDB cv = new CommitVisitorDB();
		cmd.walkRepoFromBackwardsToDB(cv);
//		cmd.readCommitTime("8906e78c463045d0f982404749d9555bbebf06e1");
//		ObjectId commitId = ObjectId.fromString("8231fcf71a5b55d7afab5efefb495612ebe32c2b");
//		RevObject object;
//		try {
//			object = cmd.revWalk.parseAny(commitId);
//			if (object instanceof RevTag) {
//				System.out.println("aaabb");
//			} else if (object instanceof RevCommit) {
//				System.out.println("lightweight");
//				RevCommit commit = (RevCommit) object;
//				for(RevCommit tiem:commit.getParents()){
//					System.out.println(tiem.getName());
//				}
//			} else {
////				// invalid
//				System.out.println(object.getClass().getName());
//				System.out.println("invalid");
//			}
//		} catch (MissingObjectException e) {
////			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
////			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
//	private static void walkAllTags(){
//		JGitRepositoryCommand cmd = new JGitRepositoryCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
//		CommitTagVisitor cv = new CommitTagVisitor(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_OUTPUT_DIR)+"/android_sdk_commits2.txt");
//		cmd.walkAllTags(cv);
//	}
	private static void walkRepoFromBackwardsAndSortOutBranches(){
		JGitRepositoryCommand cmd = new JGitRepositoryCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git");
//		CommitVisitorDB cv = new CommitVisitorDB();
//		cmd.walkRepoBackwardDividedByBranch(cv);
		
	}
	
	
}
