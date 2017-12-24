package edu.fdu.se.main.androidrepo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Optional;
import java.util.Scanner;

import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import edu.fdu.se.bean.AndroidTag;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidTagDAO;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.commitcodeinfo.CommitCodeInfo;
import edu.fdu.se.git.commitcodeinfo.FileChangeEditList;
import edu.fdu.se.gitrepo.RepoConstants;
import edu.fdu.se.javaparser.JavaParserFactory;

public class GroundTruthFinder {
	
	public GroundTruthFinder(){
		tagCmd = new JGitTagCommand(
				ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
				+ RepoConstants.platform_frameworks_base_ + ".git");
		this.releaseCommitList = new ArrayList<RevCommit>();
		this.tagList = new ArrayList<String>();
		this.timeStart = 0;
		this.timeEnd =0;
	}
	
	JGitTagCommand tagCmd;
	List<RevCommit> releaseCommitList;
	List<String> tagList;
	int timeStart;
	int timeEnd;
	public static void main(String args[]){
		GroundTruthFinder run = new GroundTruthFinder();
		run.addReleaseCommitByTagName("android-4.0.3_r1");
		run.timeStart = run.releaseCommitList.get(0).getCommitTime();
		run.addReleaseCommitByTagName("android-4.1.1_r1");
		run.addReleaseCommitByTagName("android-4.2_r1");
		run.addReleaseCommitByTagName("android-4.3_r1");
		run.addReleaseCommitByTagName("android-4.4_r1");
		run.addReleaseCommitByTagName("android-5.0.0_r1");
		run.addReleaseCommitByTagName("android-5.1.0_r1");
		run.addReleaseCommitByTagName("android-6.0.0_r1");
		run.addReleaseCommitByTagName("android-7.0.0_r1");
		run.addReleaseCommitByTagName("android-7.1.0_r1");
		run.addReleaseCommitByTagName("android-8.0.0_r1");
		run.timeStart = run.releaseCommitList.get(run.releaseCommitList.size()-1).getCommitTime();
		run.run();
	}
	private void addReleaseCommitByTagName(String tagName){
		List<AndroidTag> tagList = AndroidTagDAO.selectTagByShortNameAndProjName(tagName,RepoConstants.platform_frameworks_base_);
		if(tagList.size()!=1){
			return;
		}
		AndroidTag mTag = tagList.get(0);
		JGitTagCommand tagCmd = new JGitTagCommand(
				ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
				+ RepoConstants.platform_frameworks_base_ + ".git");
		RevCommit revCommit = tagCmd.revCommitOfTag(mTag.getTagShaId());
		this.releaseCommitList.add(revCommit);
		this.tagList.add(tagName);
	}
	
	
	
	/**
	 * 
	 */
	public void run(){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(ProjectProperties.getInstance().getValue(PropertyKeys.LOCAL_DESKTOP)+"/test2.txt");
		
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br =new BufferedReader(isr);
		String line ;
		while((line = br.readLine())!=null){
			line = line.trim();
			int commitTime = tagCmd.readCommitTime(line);
			if(commitTime<mTime){
				System.err.println("early ignore");
			}
			CommitCodeInfo mCCI = tagCmd.getCommitEditSummary(line);
			Map<RevCommit,List<FileChangeEditList>> mMap = mCCI.getFileDiffEntryMap();
			for(Entry<RevCommit,List<FileChangeEditList>> item :mMap.entrySet()){
				RevCommit parent = item.getKey();
				List<FileChangeEditList> list = item.getValue();
				for(FileChangeEditList item2 :list){
					//每一个文件 都去比较一次
					String oldPath = item2.getOldFilePath();
					String fileName = getClassNameFromPath(oldPath);
					System.out.println(oldPath);
					Map<String,List<MethodDeclaration>> codeFromRelease = this.mappingTagStrToMethodDeclarationList(oldPath, fileName);
					
					InputStream fileInputStream = tagCmd.extractAndReturnInputStream(oldPath, parent.getName());
					List<MethodDeclaration> codePrev = JavaParserFactory.parseInputStreamGetOverlapMethodDeclarationList(fileInputStream, fileName, item2);
					this.compareTwoSet(codeFromRelease,codePrev);
				}
			}
			
		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getClassNameFromPath(String path){
		String fileName = path.substring(path.lastIndexOf("/")+1);
		return fileName.substring(0, fileName.length()-5);
	}
	
	public void compareTwoSet(Map<String,List<MethodDeclaration>> releasedTagMethod,List<MethodDeclaration> mList){
		for(Entry<String,List<MethodDeclaration>> item: releasedTagMethod.entrySet()){
			String tagName = item.getKey();
			List<MethodDeclaration> mList2 = item.getValue();
			for(MethodDeclaration tagRelease:mList2){
				for(MethodDeclaration changeCode:mList){
					if(tagRelease.getDeclarationAsString().equals(changeCode.getDeclarationAsString())){
						if(tagRelease.getBody().get().toString().equals(changeCode.getBody().get().toString())){
							System.out.println("mapped");
						}
					}
				}
			}
		}
	}
	
	public Map<String,List<MethodDeclaration>> mappingTagStrToMethodDeclarationList(String oldPath,String className){
		Map<String,List<MethodDeclaration>> result = new HashMap<String,List<MethodDeclaration>>();
		
		for(int i=0;i<this.tagList.size();i++){
			InputStream fileInputStream  = tagCmd.extractAndReturnInputStream(oldPath, this.releaseCommitList.get(i).getName());
			List<MethodDeclaration> mList = JavaParserFactory.parseFileGetAllMethodDeclaration(fileInputStream, className);
			result.put(this.tagList.get(i), mList);
		}
		return result;
	}

	

}
