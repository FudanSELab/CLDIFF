package edu.fdu.se.main.groundtruth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidBranch;
import edu.fdu.se.bean.AndroidCacheCommit;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidBranchDAO;
import edu.fdu.se.dao.AndroidCacheCommitDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.fileutil.FileUtil;
import edu.fdu.se.git.JGitRepositoryManager;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.manager.AndroidTagManager;
/**
 * tag contains 命令输出tag所在的branch，文件的处理
 * @author huangkaifeng
 *
 */
public class FindOutTagAndBranchRelation {
	
	
	public static void main(String args[]){
		run3();
//		run4();
	}
	/**
	 * which_branch_tag_in文件夹文本内容表示，tag contains, which_branch_tag_in2 输出，多了branch到该tag.txt
	 * 的commit 数量
	 * tag_in1（python） -> tag_in 2
	 */
	public static void run(){
		File f = new File("D:/which_branch_tag_in");
		File[] files = f.listFiles();
		for(File tmp:files){
			if(tmp.isDirectory()){
				continue;
			}
			File newFile = new File("D:/which_branch_tag_in2/"+tmp.getName());
			
			String fileName = tmp.getName();
			System.out.println(fileName);
			try {
				FileOutputStream fos = new FileOutputStream(newFile);
				FileInputStream is = new FileInputStream(tmp);
				Scanner sc = new Scanner(is);
				while(sc.hasNext()){
					String line = sc.next();
					if(line ==null){
						break;
					}
					line = line.trim();
					if(line.length()>2){
						if(line.equals("contains")){
							continue;
						}
						int num = listCommitsNum(fileName.substring(0,fileName.length()-4),line);
						if(num==0){
							fos.write(line.getBytes());
							fos.write(" tag\n".getBytes());
						}else{
							fos.write(line.getBytes());
							fos.write(" ".getBytes());
							fos.write(String.valueOf(num).getBytes());
							fos.write("\n".getBytes());
						}
					}
				}
				fos.flush();
				fos.close();
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	
	}
	
	/**
	 * 分析which_branch_tag_in2的文件，对于某一个大版本所有的revision，分析前后commit关系。
	 */
	public static void run2(){
		File f = new File("D:/sort_tag_branch/which_branch_tag_in2");
		File[] files = f.listFiles();
//		CommitFile newFile = new CommitFile("D:/tag_relation.txt");
//		String prefix = "android-8.1.0";
		Map<String,Integer> mList = new HashMap<String,Integer>();
		for(File tmp:files){
			if(tmp.isDirectory()){
				continue;
			}
			String tagName = tmp.getName().substring(0,tmp.getName().length()-4);
			if(tagName.startsWith("android-7.0.0")){
				String r = tagName.substring(15);
				int i = Integer.valueOf(r);
				mList.put(tagName, i);
				
			}
		}
		List<Entry<String,Integer>> mmLit = new ArrayList<Entry<String,Integer>>(mList.entrySet());
		mmLit.sort(new Comparator<Entry<String,Integer>>(){

			@Override
			public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
				
				return arg0.getValue()-arg1.getValue();
			}
		});
//		for(Entry<String,Integer> i :mmLit){
//			System.out.println(i.getKey());
//		}
		
		JGitTagCommand cmd = JGitRepositoryManager.getBaseCommand();
		for(int i=0;i<mmLit.size()-1;i++){
			String a = mmLit.get(i).getKey();
			String b = mmLit.get(i+1).getKey();
			AndroidTag aa = AndroidTagManager.tagEntryOfTagNameOnDefaultProj(a);
			AndroidTag bb =AndroidTagManager.tagEntryOfTagNameOnDefaultProj(b);
			RevCommit aac = cmd.revCommitOfTag(aa.getTagShaId());
			RevCommit bbc = cmd.revCommitOfTag(bb.getTagShaId());
			List<RevCommit> revList = new ArrayList<RevCommit>();
			boolean flag = cmd.walkRepoBackwardsStartWithCommitId(bbc,aac, revList);
			System.out.print(a+"->"+b+" :"+flag+" "+revList.size());
			System.out.println("");
		
		}
		
		
		
	}
	
	/**
	 * 筛选出所有大版本下的tag，收集所有的branch name，从branchname遍历到root的r1版本，收集commit
	 * 4.4以下的除外
	 * = GroundTruthFinder 
	 */
	public static void run3(){
		File f = new File("D:/sort_tag_branch/which_branch_tag_in");
		String tagStrP = "android-5.0.0_r1";
		String tagStr2 = tagStrP.substring(0,tagStrP.length()-3);
		File[] files = f.listFiles();
		Set<RevCommit> mSet = new HashSet<RevCommit>();
		Set<String> uniqueBranchList = new HashSet<String>();
		for(File tmp:files){
			if(tmp.isDirectory()){
				continue;
			}
			String tagName = tmp.getName().substring(0,tmp.getName().length()-4);
			if(tagName.equals("android-4.4w_r1")){
				continue;
			}
			if(tagName.startsWith(tagStr2)){
				List<String> branchList = FileUtil.getLines(tmp);
				for(String branch:branchList){
					if(branch.equals("contains")){
						continue;
					}
					branch = branch.trim();
					if(tagStrP.equals("android-4.4_r1") ){
						if(branch.startsWith("kitkat"))
							uniqueBranchList.add(branch);
					}else{
						uniqueBranchList.add(branch);
					}
				}
			}
		}
		AndroidTag at2 = AndroidTagDAO.selectTagByShortNameAndProjName(tagStrP, RepoConstants.platform_frameworks_base_);
		RevCommit tagRev = JGitRepositoryManager.getBaseCommand().revCommitOfTag(at2.getTagShaId());
		// branch list往前追溯到
		for(String tmp:uniqueBranchList){
			AndroidBranch ab = AndroidBranchDAO.selectBranchByShortNameAndProjName(tmp, RepoConstants.platform_frameworks_base_);
//				System.out.println(ab.getBranchNameFull());
				List<RevCommit> tmpList = new ArrayList<RevCommit>();
				RevCommit branchC = JGitRepositoryManager.getBaseCommand().revCommitOfCommitId(ab.getBranchCommitSha());
				boolean flag = JGitRepositoryManager.getBaseCommand().walkRepoBackwardsStartWithCommitId(branchC, tagRev, tmpList);
//				System.out.println(flag);
				mSet.addAll(tmpList);
		}
		System.out.print("("+mSet.size());
		int cnt =0 ;
		for(RevCommit a:mSet){
			AndroidCacheCommit acc = null;
			if(MatchBugFixingRules.bugOrNot3(a.getShortMessage(), a.getFullMessage())){
				cnt++;
				acc = new AndroidCacheCommit(0,tagStrP+"--bug",a.getName());
				AndroidCacheCommitDAO.insert(acc);
			}
			acc = new AndroidCacheCommit(0,tagStrP,a.getName());
			AndroidCacheCommitDAO.insert(acc);
		}
		System.out.println(","+cnt+")\n");
	}
	/**
	 * 弄清楚kitcat到后面几个版本之间的关系
	 */
	public static void run4(){
		String version4_4 = "android-4.0.3_r1.1";
		String version = "android-4.0.4_r2.1";
		AndroidTag at1 = AndroidTagManager.tagEntryOfTagNameOnDefaultProj(version4_4);
		AndroidTag at2 = AndroidTagManager.tagEntryOfTagNameOnDefaultProj(version);
		RevCommit rc1 = JGitRepositoryManager.getBaseCommand().revCommitOfTag(at1.getTagShaId());
		RevCommit rc2 = JGitRepositoryManager.getBaseCommand().revCommitOfTag(at2.getTagShaId());
		System.out.println(JGitRepositoryManager.getBaseCommand().stampToDate(rc1.getCommitTime()*1000L));
		System.out.println(JGitRepositoryManager.getBaseCommand().stampToDate(rc2.getCommitTime()*1000L));
		List<RevCommit> mList = new ArrayList<RevCommit>();
//		System.out.println("0.7:"+rc1.getName());
//		System.out.println("0.8:"+rc2.getName());
		boolean flag = JGitRepositoryManager.getBaseCommand().walkRepoBackwardsStartWithCommitId(rc2, rc1, mList);
		System.out.println(flag);
//		mList.sort(new Comparator<RevCommit>(){
//			@Override
//			public int compare(RevCommit arg0, RevCommit arg1) {
//				return arg0.getCommitTime()-arg1.getCommitTime();
//			}
//			
//		});
		System.out.println(mList.size());
//		for(RevCommit item:mList){
//			System.out.print(item.getName().substring(0,6));
//			if(item.getParentCount()==1){
//				System.out.println(" :"+item.getParent(0).getName().substring(0, 6));
//			}else{
//				System.out.println(" :"+item.getParent(0).getName().substring(0, 6));
//				System.out.println("      "+item.getParent(0).getName().substring(0, 6));
//			}
//			
//		}
	}
	
	public static int listCommitsNum(String tagName,String branchName){
		
		AndroidTag t = AndroidTagDAO.selectTagByShortNameAndProjName(tagName, RepoConstants.platform_frameworks_base_);
		JGitTagCommand cmd = JGitRepositoryManager.getBaseCommand();
		RevCommit tagCommit = cmd.revCommitOfTag(t.getTagShaId());
		Ref branch = cmd.getBranchByShortName(branchName);
		RevCommit c = cmd.revCommitOfBranchRef(branch);
		if(c.getName().equals(tagCommit.getName())){
			return 0;
		}
		List<RevCommit> list = new ArrayList<RevCommit>();
		cmd.walkRepoBackwardsStartWithCommitId(c, tagCommit, list);
		return list.size();
	}

}
