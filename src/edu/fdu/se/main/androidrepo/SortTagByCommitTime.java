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

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.main.androidrepo.preprocessing.RepoConstants;

public class SortTagByCommitTime {
	
	public static void main(String args[]){
		try {
			JGitTagCommand tagCmd = new JGitTagCommand(
					ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
					+ RepoConstants.platform_frameworks_base_ + ".git");
			FileInputStream fis = new FileInputStream(new File("C:/Users/huangkaifeng/Desktop/12-14-Android-Repo/android_tags_v1_filter_wear_cts.txt"));
			Scanner sc = new Scanner(fis);
			Map<String,Integer> tagStringMap = new HashMap<String,Integer>();
			
			while(sc.hasNext()){
				String line = sc.nextLine();
				line = line.trim();
				String[] data = line.split("/");
				String tagName = data[data.length-1];
				List<AndroidTag> tag = AndroidTagDAO.selectTagByShortNameAndProjName(tagName, RepoConstants.platform_frameworks_base_);
				if(tag.size()!=1){
					System.err.println("ererer");
					continue;
				}
				AndroidTag mTag = tag.get(0);
				String tagId = mTag.getTagShaId();
				RevCommit revCommit  = tagCmd.revCommitOfTag(tagId);
				Integer mTime = revCommit.getCommitTime();
				tagStringMap.put(mTag.getTagNameShort(), mTime);
			}
			sc.close();
			List<Entry<String,Integer>> tmpList = new ArrayList<Entry<String,Integer>>(tagStringMap.entrySet());
			tmpList.sort(new Comparator<Entry<String,Integer>>(){

				@Override
				public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
					return arg0.getValue()- arg1.getValue();
				}
				
			});
			for(Entry<String,Integer> item:tmpList){
				System.out.println(item.getKey());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
