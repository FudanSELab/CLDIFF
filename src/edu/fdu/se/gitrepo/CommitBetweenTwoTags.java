package edu.fdu.se.gitrepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;

/**
 * 给两个tag id，判断是否是一条开发线，返回两个tag 之间的commit
 * 
 * @author huangkaifeng
 *
 */
public class CommitBetweenTwoTags {

	public static RevCommit[] revCommitOfTagStr(String prev, String curr, JGitTagCommand cmd, String repoName) {
		RevCommit[] res = new RevCommit[2];
		List<AndroidTag> mList = AndroidTagDAO.selectTagByShortNameAndProjName(curr, repoName);
		if (mList.size() != 1) {
			System.err.println("Err");
		}
		AndroidTag tagInsCurr = mList.get(0);
		RevCommit commitCurr = cmd.revCommitOfTag(tagInsCurr.getTagShaId());
		res[1] = commitCurr;

		mList = AndroidTagDAO.selectTagByShortNameAndProjName(prev, repoName);
		if (mList.size() != 1) {
			System.err.println("Err");
		}
		AndroidTag tagInsPrev = mList.get(0);
		RevCommit commitPrev = cmd.revCommitOfTag(tagInsPrev.getTagShaId());
		res[0] = commitPrev;
		return res;
	}

	public static void main(String args[]) {
//		String tagPrev = "android-7.1.1_r1";
//		String tagCurr = "android-8.0.0_r1";
		
		String tagPrev = "android-4.4w_r1";
		String tagCurr = "android-5.0.1_r1";
		String repoName = RepoConstants.platform_frameworks_base_;
		JGitTagCommand cmd = JGitRepositoryManager.getCommand(repoName);
		RevCommit[] mList = revCommitOfTagStr(tagPrev, tagCurr, cmd, repoName);
		RevCommit commitCurr = mList[1];
		RevCommit commitPrev = mList[0];
		System.out.println("CurrentCommit:"+ commitCurr.getName());
		System.out.println("PrevCommit:"+ commitPrev.getName());
		List<RevCommit> commitsInBetweenTags = new ArrayList<RevCommit>();
		boolean result = cmd.walkRepoBackwardsStartWithCommitId(commitCurr, commitPrev, commitsInBetweenTags);
		System.out.println(result);
	
	}
	// Ref peeledRef = cmd.repository.peel(ref);
	// if(peeledRef.getPeeledObjectId() != null) {
	// log.add(peeledRef.getPeeledObjectId());
}
