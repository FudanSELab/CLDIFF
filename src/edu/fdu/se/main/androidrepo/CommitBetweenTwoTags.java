package edu.fdu.se.main.androidrepo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidRepoCommitDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitRepositoryManager;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.main.groundtruth.MatchBugFixingRules;

/**
 * 给两个tag id，判断是否是一条开发线，返回两个tag 之间的commit
 * 
 * @author huangkaifeng
 *
 */
public class CommitBetweenTwoTags {

	public static RevCommit[] revCommitOfTagStr(String prev, String curr, JGitTagCommand cmd, String repoName) {
		RevCommit[] res = new RevCommit[2];
		AndroidTag tagInsCurr = AndroidTagDAO.selectTagByShortNameAndProjName(curr, repoName);
		RevCommit commitCurr = cmd.revCommitOfTag(tagInsCurr.getTagShaId());
		res[1] = commitCurr;

		AndroidTag tagInsPrev = AndroidTagDAO.selectTagByShortNameAndProjName(prev, repoName);
		RevCommit commitPrev = cmd.revCommitOfTag(tagInsPrev.getTagShaId());
		res[0] = commitPrev;
		return res;
	}
	public static int count =0;
	public static int count2=0;
	public static void run(String tagPrev,String tagCurr,boolean flag){
		String repoName = RepoConstants.platform_frameworks_base_;
		JGitTagCommand cmd = JGitRepositoryManager.getCommand(repoName);
		RevCommit[] mList = revCommitOfTagStr(tagPrev, tagCurr, cmd, repoName);
		RevCommit commitCurr = mList[1];
		RevCommit commitPrev = mList[0];
		
		List<RevCommit> commitsInBetweenTags = new ArrayList<RevCommit>();
		boolean result = cmd.walkRepoBackwardsStartWithCommitId(commitCurr, commitPrev, commitsInBetweenTags);
		
		if(flag == true){
			count++;
			if(result==true && commitsInBetweenTags.size()>2){
				System.out.println(count+" "+commitsInBetweenTags.size());
				count++;
				System.out.println(tagPrev+" <---> "+tagCurr);
				System.out.println("CurrentCommit:"+ commitCurr.getName());
				System.out.println("PrevCommit:"+ commitPrev.getName());
				System.out.println(result);
				for(RevCommit item: commitsInBetweenTags){
					if(MatchBugFixingRules.bugOrNot(item.getShortMessage(),item.getFullMessage())){
						//TODO
						AndroidRepoCommitWithBLOBs  a = AndroidRepoCommitDAO.selectWithCommitSHA(item.getName());
						if(a!=null){
							if(a.getIssdkfile()!=0)
								System.out.println(item.getName()+" "+item.getShortMessage());
						}else{
							System.err.println("EEEE");
						}
					}else{
						System.out.println(item.getName());
					}
					
				}
				System.out.println("\n");
			}
		}else{
			//false
			count2++;
		}
		
		
		
	}	
	

	
	public static void runTagPair(){
		run("android-7.1.0_r1","android-7.1.0_r5",true);
	}
	public static void main(String args[]) {
		runTagPair();
		//runTagList();
	}
}
