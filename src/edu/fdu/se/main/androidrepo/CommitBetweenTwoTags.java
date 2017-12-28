package edu.fdu.se.main.androidrepo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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

import edu.fdu.se.bean.AndroidRepoCommitWithBLOBs;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidRepoCommitDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.gitrepo.JGitRepositoryManager;

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
					if(bugOrNot(item)){
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
	
	public static boolean bugOrNot(RevCommit rev){
		String msg = rev.getShortMessage().toLowerCase();
		String msgFull = rev.getFullMessage().toLowerCase();
		boolean result = false;
		if(msg.contains("bug")|| msg.contains("fix")||msg.contains("patch")||msg.contains("fixing")){
			result = true;
		}
		if(msgFull.contains("bug")|| msgFull.contains("fix")||msgFull.contains("patch")||msgFull.contains("fixing")){
			result = true;
		}
		return result;
	}
	
	public static List<String> readTagList(){
		FileInputStream fos;
		List<String> mList = new ArrayList<String>();
		try {
			fos = new FileInputStream(new File("C:/Users/huangkaifeng/Desktop/12-14-Android-Repo/android_tags_filtered2"));
			InputStreamReader inputReader = new InputStreamReader(fos);
			BufferedReader br = new BufferedReader(inputReader);
			String str = null;
			while((str = br.readLine())!= null ){
				String tagStr = str.substring(10);
				mList.add(tagStr);
			}
			return mList;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void runTagList(){
		List<String> tagList = readTagList();
		for(int i =0;i<tagList.size()-1;i++){
			String tagPrev = tagList.get(i);
			String tagCurr = tagList.get(i+1);
			run(tagPrev,tagCurr,true);
			
		}
		System.out.println(count+":"+count2);
	}
	
	public static void runTagPair(){
		run("android-7.1.0_r1","android-7.1.0_r5",true);
	}
	public static void main(String args[]) {
		runTagPair();
		//runTagList();
	}
}
