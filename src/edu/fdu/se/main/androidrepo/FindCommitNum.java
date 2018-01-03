package edu.fdu.se.main.androidrepo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidBranch;
import edu.fdu.se.bean.AndroidCacheCommit;
import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidBranchDAO;
import edu.fdu.se.dao.AndroidCacheCommitDAO;
import edu.fdu.se.dao.AndroidRepoCommitDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.git.RepositoryHelper;

public class FindCommitNum {
	
	public static void main(String args[]){
		testMasterCommitNum();
//		testForward();
//		viewCommit();
	}
	
	public static void testForward(){
		JGitTagCommand cmd = (JGitTagCommand) RepositoryHelper.getInstance1().myCmd;
		AndroidTag at = AndroidTagDAO.selectTagByShortNameAndProjName("android-4.0.3_r1", RepoConstants.platform_frameworks_base_);
		String sha = at.getTagShaId();
		cmd.walkRepoBackwardsStartFromTag(sha);
	}
	public static void testMasterCommitNum(){
		JGitTagCommand cmd = (JGitTagCommand) RepositoryHelper.getInstance1().myCmd;
		AndroidBranch ab = AndroidBranchDAO.selectBranchByShortNameAndProjName("master", RepoConstants.platform_frameworks_base_);
//		AndroidTag at = AndroidTagDAO.selectTagByShortNameAndProjName("android-4.0.3_r1", RepoConstants.platform_frameworks_base_);
//		cmd.walkRepoBackwardsStartFromCommit(ab.getBranchCommitSha());
//		RevCommit rc2 = cmd.revCommitOfTag(at.getTagShaId());
		RevCommit rc1 = cmd.revCommitOfCommitId(ab.getBranchCommitSha());
//		List<RevCommit> r = new ArrayList<RevCommit>();
		cmd.walkRepoBackwardsStartFromCommit(rc1);
//		boolean flag = cmd.walkRepoBackwardsStartWithCommitId(rc1,rc2, r);
//		System.out.println(flag);
//		System.out.println(r.size());
	}
	public static void viewCommit(){
		List<AndroidCacheCommit> mList = AndroidCacheCommitDAO.selectByKey("android-8.0.0_r1");
		int count =0;
		for(AndroidCacheCommit item:mList){
			String a = item.getCommitId();
			AndroidRepoCommitWithBLOBs apc = AndroidRepoCommitDAO.selectWithCommitSHA(a);
			System.out.println(a);
			System.out.println(apc.getCommitLog());
			if(apc.getCommitLog().contains("cherrypicks")){
				count ++;
			}
		}
		System.out.println(mList.size()+" "+count);
	}

}
