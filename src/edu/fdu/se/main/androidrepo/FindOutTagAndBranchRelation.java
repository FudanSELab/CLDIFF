package edu.fdu.se.main.androidrepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidBranch;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidBranchDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.fileutil.FileUtil;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.gitrepo.JGitRepositoryManager;
import edu.fdu.se.groundtruth.MatchBugFixingRules;
import edu.fdu.se.manager.AndroidTagManager;
/**
 * tag contains 命令输出tag所在的branch，文件的处理
 * @author huangkaifeng
 *
 */
public class FindOutTagAndBranchRelation {
	
	
	public static void main(String args[]){
//		run3();
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
	 * 分析which_branch_tag_in2的文件，分析前后commit关系。
	 */
	public static void run2(){
		File f = new File("D:/sort_tag_branch/which_branch_tag_in2");
		File[] files = f.listFiles();
//		File newFile = new File("D:/tag_relation.txt");
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
		List<Entry<String,Integer>> mmLit = new ArrayList(mList.entrySet());
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
	 */
	public static void run3(){
		File f = new File("D:/sort_tag_branch/which_branch_tag_in");
		String tagStrP = "android-4.4_r1";
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
		List<AndroidTag> at = AndroidTagDAO.selectTagByShortNameAndProjName(tagStrP, RepoConstants.platform_frameworks_base_);
		if(at.size()!=1){
			System.err.println("Err");
		}
		AndroidTag at2 = at.get(0);
		RevCommit tagRev = JGitRepositoryManager.getBaseCommand().revCommitOfTag(at2.getTagShaId());
		// branch list往前追溯到
		for(String tmp:uniqueBranchList){
			List<AndroidBranch> mm = AndroidBranchDAO.selectTagByShortNameAndProjName(tmp, RepoConstants.platform_frameworks_base_);
			if(mm.size()==1){
				AndroidBranch ab = mm.get(0);
//				System.out.println(ab.getBranchNameFull());
				List<RevCommit> tmpList = new ArrayList<RevCommit>();
				RevCommit branchC = JGitRepositoryManager.getBaseCommand().revCommitOfCommitId(ab.getBranchCommitSha());
				boolean flag = JGitRepositoryManager.getBaseCommand().walkRepoBackwardsStartWithCommitId(branchC, tagRev, tmpList);
//				System.out.println(flag);
				mSet.addAll(tmpList);
			}else{
				System.err.println("aa");
			}
		}
		System.out.print("("+mSet.size());
		int cnt =0 ;
		for(RevCommit a:mSet){
			if(MatchBugFixingRules.bugOrNot3(a.getShortMessage(), a.getFullMessage())){
				cnt++;
			}
		}
		System.out.println(","+cnt+")\n");
	}
	
	public static void run4(){
		
	}
	
	public static int listCommitsNum(String tagName,String branchName){
		
		List<AndroidTag> tags = AndroidTagDAO.selectTagByShortNameAndProjName(tagName, RepoConstants.platform_frameworks_base_);
		if(tags.size()!=1){
			System.out.println("Error");
			return 0;
		}
		AndroidTag t = tags.get(0);
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
