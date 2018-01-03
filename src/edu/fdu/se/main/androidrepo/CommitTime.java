package edu.fdu.se.main.androidrepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.bean.AndroidBranch;
import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.dao.AndroidBranchDAO;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.fileutil.FileUtil;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepoConstants;
import edu.fdu.se.git.RepositoryHelper;
/**
 * commit time
 * @author huangkaifeng
 *
 */
public class CommitTime {

	public static void main(String args[]) {
		// run();
		tagsBranchDate();
	}
	
	public static void tagsDate(){
		String[] tagNames = {"android-8.0.0_r1","android-7.1.0_r1","android-7.0.0_r1",
				"android-6.0.0_r1","android-5.1.0_r1","android-5.0.0_r1"};
		JGitTagCommand tagCmd = (JGitTagCommand)RepositoryHelper.getInstance1().myCmd;
		for(String name:tagNames){
			System.out.print(name);
			AndroidTag at = AndroidTagDAO.selectTagByShortNameAndProjName(name, RepoConstants.platform_frameworks_base_);
			RevCommit rc = tagCmd.revCommitOfTag(at.getTagShaId());
			System.out.println(" "+tagCmd.stampToDate(rc));
		}
	}
	/**
	 * tag所在的branch date
	 */
	public static void tagsBranchDate(){
		String[] tagNames = {"android-8.0.0_r1","android-7.1.0_r1","android-7.0.0_r1",
				"android-6.0.0_r1","android-5.1.0_r1","android-5.0.0_r1"};
		String path = "D:/sort_tag_branch/which_branch_tag_in/";
		JGitTagCommand tagCmd = (JGitTagCommand)RepositoryHelper.getInstance1().myCmd;
		for(String name:tagNames){
			String fullPath = path+name+".txt";
			File f = new File(fullPath);
			System.out.println(name);
			if(f.exists()){
				List<String> lines = FileUtil.getLines(f);
				for(String tmp:lines){
					AndroidBranch ab = AndroidBranchDAO.selectBranchByShortNameAndProjName(tmp, RepoConstants.platform_frameworks_base_);
					RevCommit rc = tagCmd.revCommitOfCommitId(ab.getBranchCommitSha());
					System.out.println(" "+tagCmd.stampToDate(rc));
				}
			}
			System.out.println("\n\n\n");
		}
	}

	public static void sortTagRun() {
		try {
			JGitTagCommand tagCmd = (JGitTagCommand)RepositoryHelper.getInstance1().myCmd;
			FileInputStream fis = new FileInputStream(
					new File("C:/Users/huangkaifeng/Desktop/12-14-Android-Repo/android_tags_v1_filter_wear_cts.txt"));
			Scanner sc = new Scanner(fis);
			Map<String, Integer> tagStringMap = new HashMap<String, Integer>();

			while (sc.hasNext()) {
				String line = sc.nextLine();
				line = line.trim();
				String[] data = line.split("/");
				String tagName = data[data.length - 1];
				AndroidTag mTag = AndroidTagDAO.selectTagByShortNameAndProjName(tagName,
						RepoConstants.platform_frameworks_base_);
				String tagId = mTag.getTagShaId();
				RevCommit revCommit = tagCmd.revCommitOfTag(tagId);
				Integer mTime = revCommit.getCommitTime();
				tagStringMap.put(mTag.getTagNameShort(), mTime);
			}
			sc.close();
			List<Entry<String, Integer>> tmpList = new ArrayList<Entry<String, Integer>>(tagStringMap.entrySet());
			tmpList.sort(new Comparator<Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
					return arg0.getValue() - arg1.getValue();
				}

			});
			for (Entry<String, Integer> item : tmpList) {
				System.out.println(item.getKey());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}



}
