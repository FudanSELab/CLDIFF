package edu.fdu.se.main.androidrepo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.fdu.se.bean.AndroidRepoCommit;
import edu.fdu.se.dao.AndroidRepoCommitDAO;
/**
 * commit intention category
 * @author huangkaifeng
 *
 */
public class CommitType {
	public static void main(String args[]) {
		commitKeyword();
	}
	public static void commitKeyword(){
		String fileName = "C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/commits_without_merge.txt";
		List<String> noMergeCommit = new ArrayList<String>();
		Map<String, String> noMergeCommitMap = new HashMap<String, String>();
		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader bis = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(bis);
			String str = "";
			while ((str = br.readLine()) != null) {
				String msg = br.readLine();
				System.out.println(str);
				noMergeCommit.add(str);
				noMergeCommitMap.put(str, msg);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
		FileOutputStream fos = new FileOutputStream("C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/unmerge_commits_keyword-fixing.txt");
		for (String item : noMergeCommit) {
			String msg = noMergeCommitMap.get(item);
			if(msg.contains("fix")||msg.contains("fixing")||msg.contains("fixes")){
				String output = item + "\n" + msg+"\n";
				fos.write(output.getBytes());
			}
		}	
		fos.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	public static void mergeAndUnmergeCommit() {
		String fileName = "C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/commits_with_merge_formatted.txt";
		String fileName2 = "C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/commits_without_merge.txt";
		List<String> mergeCommit = new ArrayList<String>();
		Map<String, String> mergeCommitMap = new HashMap<String, String>();

		List<String> noMergeCommit = new ArrayList<String>();
		Map<String, String> noMergeCommitMap = new HashMap<String, String>();

		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader bis = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(bis);
			String str = "";
			while ((str = br.readLine()) != null) {
//				String msg = br.readLine();
//				String tail = null;
//				while(true){
//					tail = br.readLine();
//					if(tail ==null){
//						break;
//					}
//				}
				System.out.println(str);
//				mergeCommit.add(str);
//				mergeCommitMap.put(str, msg);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileInputStream fis = new FileInputStream(fileName2);
			InputStreamReader bis = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(bis);
			String str = "";
			while ((str = br.readLine()) != null) {
				String msg = br.readLine();
				noMergeCommit.add(str);
				noMergeCommitMap.put(str, msg);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String item: mergeCommit){
			String msg = mergeCommitMap.get(item);
			if(noMergeCommitMap.containsValue(msg)){
				System.out.println("yes");
			}else{
				System.out.println("no");
			}
		}
//		try {
//			FileOutputStream fos = new FileOutputStream("C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/merge_commits_traceable.txt");
//			for (String item : mergeCommit) {
//				String msg = mergeCommitMap.get(item);
//				List<String> res = regexMatchMerge(msg);
//				String output = item+"\n";
//				for(String item2 :res){
//						output += item2+"\n";
//				}
//				output += "\n";
//				fos.write(output.getBytes());
//			}	
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

	public static void mergeCommit() {
		String fileName = "C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/commits_with_merge.txt";
//		String fileName2 = "C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/commits_without_merge.txt";
		List<String> mergeCommit = new ArrayList<String>();
		Map<String, String> mergeCommitMap = new HashMap<String, String>();

//		List<String> noMergeCommit = new ArrayList<String>();
//		Map<String, String> noMergeCommitMap = new HashMap<String, String>();

		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader bis = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(bis);
			String str = "";
			while ((str = br.readLine()) != null) {
				String msg = br.readLine();
				mergeCommit.add(str);
				mergeCommitMap.put(str, msg);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		try {
//			FileInputStream fis = new FileInputStream(fileName2);
//			InputStreamReader bis = new InputStreamReader(fis);
//			BufferedReader br = new BufferedReader(bis);
//			String str = "";
//			while ((str = br.readLine()) != null) {
//				String msg = br.readLine();
//				noMergeCommit.add(str);
//				noMergeCommitMap.put(str, msg);
//			}
//			br.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {
			FileOutputStream fos = new FileOutputStream("C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/commits_with_merge_formatted.txt");
			for (String item : mergeCommit) {
				String msg = mergeCommitMap.get(item);
				List<String> res = regexMatchMerge(msg);
				String output = item+"\n";
				for(String item2 :res){
						output += item2+"\n";
				}
				output += "\n";
				fos.write(output.getBytes());
			}	
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static List<String> regexMatchMerge(String msg) {
		Pattern pattern = Pattern.compile("^Merge \"(.*)\"(.*)");
		Pattern pattern2 = Pattern.compile("( into [a-zA-Z\\-]{0,})");
		Pattern pattern3 = Pattern.compile("( am: [a-zA-Z0-9]{1,})");
		Matcher matcher = pattern.matcher(msg);
		String tail = null;
		List<String> result = new ArrayList<String>();
		while (matcher.find()) {
			result.add(matcher.group(1));
			tail = matcher.group(2);
		}
		if(tail != null){
			Matcher matcher2 = pattern2.matcher(tail);
			while(matcher2.find()){
				result.add(matcher2.group(1));
			}
		}
		if(tail != null){
			Matcher matcher3 = pattern3.matcher(tail);
			while(matcher3.find()){
				result.add(matcher3.group(1));
			}
		}
		for(String item:result){
			System.out.println(item);
		}
		return result;
	}


	/**
	 * merge & no merge
	 */
	public static void commit() {
		String fileName = "C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/android_sdk_commits.txt";

		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader bis = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(bis);
			String str = "";

			FileOutputStream fos = new FileOutputStream(
					"C:/Users/huangkaifeng/Desktop/DiffMiner/12-4/commits_with_merge.txt");
			while ((str = br.readLine()) != null) {
				String msg = br.readLine();
				if (msg.startsWith("Merge")) {
					String writeContent = str + "\n" + msg + "\n";
					fos.write(writeContent.getBytes());
				}
			}
			br.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出数据库commit到file
	 */
	public static void dbToFile() {
		List<String> fileCommits = new ArrayList<String>();

		String fileName = "C:/Users/huangkaifeng/Desktop/DiffMiner/10-20-Android-Repo-Commits/android_sdk_commits.txt";
		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader bis = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(bis);
			String str = "";
			while ((str = br.readLine()) != null) {
				String msg = br.readLine();
				fileCommits.add(str);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<AndroidRepoCommit> commits = AndroidRepoCommitDAO.selectAll();
		Map<String, String> dbCommit = new HashMap<String, String>();
		for (AndroidRepoCommit item : commits) {
			String hash = item.getCommitId();
			dbCommit.put(hash, "1");
		}
		for (String item : fileCommits) {
			if (dbCommit.containsKey(item)) {
				continue;
			} else {
				System.out.println(item);
			}
		}

	}

}
