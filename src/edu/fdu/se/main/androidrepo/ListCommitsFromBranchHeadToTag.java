package edu.fdu.se.main.androidrepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.gitrepo.JGitRepositoryManager;

public class ListCommitsFromBranchHeadToTag {
	
	public static void main(String args[]){
//		JGitTagCommand cmd = JGitRepositoryManager.getBaseCommand();
//		List<Ref> bList = cmd.getAllBranches();
//		for(Ref i :bList){
//			RevCommit c = cmd.revCommitOfBranchRef(i);
//			
//		}
		listCommits();
	}
	public static void listCommits(){
		String tagName = "android-8.1.0_r1";
		String[] branchName = {"oreo-m2-release","oreo-mr1-cts-release","oreo-mr1-release"};
		List<AndroidTag> tags = AndroidTagDAO.selectTagByShortNameAndProjName(tagName, RepoConstants.platform_frameworks_base_);
		if(tags.size()!=1){
			return;
		}
		AndroidTag t = tags.get(0);
		JGitTagCommand cmd = JGitRepositoryManager.getBaseCommand();
		RevCommit tagCommit = cmd.revCommitOfTag(t.getTagShaId());
		
		List<Ref> branches = cmd.getBranchesByShortNames(branchName);
		Set<RevCommit> mSet = new HashSet<RevCommit>();
		System.out.println("Tag Commit id:"+tagCommit.getName());
		for(Ref a: branches){
			System.out.print("Head:");
			RevCommit c = cmd.revCommitOfBranchRef(a);
			List<RevCommit> list = new ArrayList<RevCommit>();
			System.out.println(c.getName());
			boolean flag = cmd.walkRepoBackwardsStartWithCommitId(c, tagCommit, list);
			mSet.addAll(list);
			System.out.println(flag+" "+list.size());
		}
		System.out.println("all："+ mSet.size());
		for(RevCommit rev:mSet){
			System.out.println(rev.getName());
		}
	}
	
	public static void listTags(){
		JGitTagCommand cmd = JGitRepositoryManager.getBaseCommand();
		List<AndroidTag> mList = AndroidTagDAO.selectTagAll(RepoConstants.platform_frameworks_base_);
		for(AndroidTag m: mList){
			RevCommit c = cmd.revCommitOfTag(m.getTagShaId());
			System.out.println(m.getTagNameShort()+" "+c.getName());
		}
	}

}
