package edu.fdu.se.main.gitgui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JTextPane;

import org.eclipse.jgit.revwalk.RevCommit;

import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepositoryHelper;
import edu.fdu.se.git.commitcodeinfo.CommitCodeInfo;
import edu.fdu.se.git.commitcodeinfo.FileChangeEditList;

public class RepoDataHelper {
	
	private static RepoDataHelper jgitRepoManager;
	
	public static RepoDataHelper getInstance(){
		if(jgitRepoManager == null){
			jgitRepoManager = new RepoDataHelper();
		}
		return jgitRepoManager;
	}
	public JGitCommand myCmd;
	public RepoDataHelper(){
//		myCmd = RepositoryHelper.getInstance1().myCmd;
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
	/**
	 * old path -> commitId
	 */
	public Map<String,RevCommit> entryMap;
	/**
	 * 如果一个father index 为0 如果两个father index为第二个father
	 */
	public int fatherIndex;
	public List<Integer> updateDataModel(DefaultListModel<String> listModel){
		listModel.clear();
		String str = null;
		int i=0;
		entryMap = new HashMap<String,RevCommit>();
		List<Integer> result = new ArrayList<Integer>();
		for(Entry<RevCommit,List<FileChangeEditList>> item:cci.getFileDiffEntryMap().entrySet()){
			RevCommit commit = item.getKey();
			str = commit.getName();
			listModel.addElement(str);
			result.add(i);
			fatherIndex = i;
			i++;
			List<FileChangeEditList> mList = item.getValue();
			for(FileChangeEditList item2 : mList){
				listModel.addElement(item2.getOldFilePath());
				entryMap.put(item2.getOldFilePath(), commit);
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
		byte[] prev = this.myCmd.extract(filePath,this.entryMap.get(filePath).getName());
		FileWriter.writeInAll("D:/commit_prev", prev);
		FileWriter.writeInAll("D:/commit_curr", curr);
	}
	
	private Map<Integer,List<String>> editScript;
	private Map<Integer,String> editType;
	
	private int[] parseFourInt(String line){
		line = line.substring(2);
		line = line.substring(0, line.length()-2);
		String[] data = line.split("+");
		String prev = data[0].trim();
		String curr = data[1].trim();
		
		//@@ -2882,0 +2911,4 @@
		//@@ -2805 +2833 @@
		line = "@@ -2805 +2833 @@";
		Pattern pat = Pattern.compile("@@ -\\d+ +\\d+ @@");
		return null;
	}
	
	public void parseOneCommitFileAndSetTwoMaps(String filePath){
		List<FileChangeEditList> tmpList = this.cci.getFileDiffEntryMap().get(this.entryMap.get(filePath));
		FileChangeEditList candidate = null;
		for(FileChangeEditList m :tmpList){
			if(m.getOldFilePath().equals(filePath)){
				candidate = m;
				break;
			}
		}
		String patch = candidate.getPatchScript();
		String[] lines = patch.split("\n");
		for(int i=3;i<lines.length;i++){
			String a = lines[i];
			if(a.startsWith("@@")){
				
			}
			
		}
		System.out.print(patch);
	}
	
	
	public void setColoredText(String filePath,JTextPane pane,JTextPane linePane){
		parseOneCommitFileAndSetTwoMaps(filePath);
//		StyledDocument doc = pane.getStyledDocument();
//		Style style = pane.addStyle("mStyle", null);
//		byte[] prev = this.myCmd.extract(filePath,this.entryMap.get(filePath));
//		List<String> lines = FileUtil.getLines(prev);
		Map<Integer,Integer> startIndexMap = null;
//		//
//		StyledDocument lineDoc = linePane.getStyledDocument();
//		Style style2 = lineDoc.addStyle("mStyle2", null);
//		
//		for(int i=0;i<lines.size();i++){
//			if(startIndexMap.containsKey(i)){
//				StyleConstants.setForeground(style, Color.red);
//				try {
//					doc.insertString(doc.getLength(), line, style);
//				} catch (BadLocationException e) {
//				}
//			}else{
//				try {
//					doc.insertString(doc.getLength(), lines.get(i), style);
//					lineDoc.insertString(doc.getLength(), String.valueOf(i + 1) + "\n", style2);
//				} catch (BadLocationException e) {
//				}
//			}
//		}
	}
	

	
	
	
	

}
