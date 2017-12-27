package edu.fdu.se.main.gitgui;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.commitcodeinfo.CommitCodeInfo;
import edu.fdu.se.git.commitcodeinfo.FileChangeEditList;
import edu.fdu.se.gitrepo.RepoConstants;

public class JGitRepoManager {
	
	private static JGitRepoManager jgitRepoManager;
	
	public static JGitRepoManager getInstance(){
		if(jgitRepoManager == null){
			jgitRepoManager = new JGitRepoManager();
		}
		return jgitRepoManager;
	}
	public JGitCommand myCmd;
	public JGitRepoManager(){
		myCmd = new JGitTagCommand("D:/Workspace/Android_Diff/android_framework_projects/platform/frameworks/base/.git");
	}
	public void parserCommit(String commitId){
		this.cci = myCmd.getCommitFileEditSummary(commitId, JGitCommand.JAVA_FILE);
		
	}
	public String getFileContent(String commitId,String fileName){
		byte[] b = myCmd.extract(fileName, commitId);
		String s = new String(b);
		return s;
	}
	
	private CommitCodeInfo cci;
	public Map<String,String> entryMap;
	public List<Integer> updateDataModel(DefaultListModel<String> listModel){
		listModel.clear();
		String str = null;
		int i=0;
		entryMap = new HashMap<String,String>();
		List<Integer> result = new ArrayList<Integer>();
		for(Entry<RevCommit,List<FileChangeEditList>> item:cci.getFileDiffEntryMap().entrySet()){
			RevCommit commit = item.getKey();
			str = commit.getName();
			listModel.addElement(str);
			result.add(i);
			i++;
			List<FileChangeEditList> mList = item.getValue();
			for(FileChangeEditList item2 : mList){
				listModel.addElement(item2.getOldFilePath());
				entryMap.put(item2.getOldFilePath(), commit.getName());
				i++;
			}
		}
		
		return result;
	}
	
	public String commitInfoSummary(){
		StringBuffer sb = new StringBuffer();
		RevCommit commit = cci.getmCommit();
		sb.append("Short message: ");
		sb.append(commit.getShortMessage());
		sb.append("\n");
		sb.append("Commiter: ");
		sb.append(commit.getCommitterIdent().getName()+" "+commit.getCommitterIdent().getEmailAddress()+"/n");
		sb.append("Author: ");
		sb.append(commit.getAuthorIdent().getName()+" "+commit.getAuthorIdent().getEmailAddress()+"/n");
		sb.append("Commit hash: ");
		sb.append(commit.getName()+"\n");
		sb.append("Date: ");
		int t = commit.getCommitTime();
		Long time = new Long(t*1000L);
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD hh:mm:ss");
		String timeStr = sdf.format(date);
		sb.append(timeStr+"\n");
		sb.append("Full message: ");
		sb.append(commit.getFullMessage());
		sb.append("\n");
		return sb.toString();
		
		
	}
	
	
	public String readFile(String filePath){
		FileChangeEditList m = null;
		boolean isFind = false;
		for(Entry<RevCommit,List<FileChangeEditList>> item:cci.getFileDiffEntryMap().entrySet()){
			List<FileChangeEditList> mList = item.getValue();
			for(FileChangeEditList item2 : mList){
				if(filePath.equals(item2.getOldFilePath())){
					m = item2;
					isFind = true;
					break;
				}
			}
			if(isFind){
				break;
			}
		}
		
		return m.getPatchScript();
	}
	
	public void writePrevCurrFiles(String filePath){
		byte[] curr = this.myCmd.extract(filePath, this.cci.getmCommit().getName());
		
		byte[] prev = this.myCmd.extract(filePath,this.entryMap.get(filePath));
		FileWriter.writeInAll("D:/commit_prev", prev);
		FileWriter.writeInAll("D:/commit_curr", curr);
		
	}
	
	
	
	

}
