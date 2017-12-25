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
/**
 * 输入tag标签，tag标签之前的candidate commit
 * @author huangkaifeng
 *
 */
public class GroundTruthFinder {
	
	public GroundTruthFinder(){
		tagCmd = new JGitTagCommand(
				ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
				+ RepoConstants.platform_frameworks_base_ + ".git");
		this.commitAndTagMap = new HashMap<RevCommit,String>();
	}
	
	JGitTagCommand tagCmd;
	Map<RevCommit,String> commitAndTagMap;
	
	public static void main(String args[]){
		GroundTruthFinder run = new GroundTruthFinder();
		run.addReleaseCommitByTagName("android-4.0.3_r1");
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
		this.commitAndTagMap.put(revCommit, tagName);
	}
	
	private List<RevCommit> chooseCandidateTag(int commitTime){
		List<RevCommit> candidateCommit = new ArrayList<RevCommit>();
		for(RevCommit commit:this.commitAndTagMap.keySet()){
			if(commit.getCommitTime()<commitTime){
				candidateCommit.add(commit);
			}
		}
		return candidateCommit;
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
			System.out.println(line);
			int commitTime = tagCmd.readCommitTime(line);
			List<RevCommit> candidate = this.chooseCandidateTag(commitTime);
			if(candidate.size()==0){
				System.out.println("early skip");
				continue;
			}
			CommitCodeInfo mCCI = tagCmd.getCommitEditSummary(line);
			Map<RevCommit,List<FileChangeEditList>> mMap = mCCI.getFileDiffEntryMap();
			for(Entry<RevCommit,List<FileChangeEditList>> item :mMap.entrySet()){
				RevCommit parent = item.getKey();
				List<FileChangeEditList> list = item.getValue();
				System.out.println("\tParent");
				List<String> tagNameListAll =null;
				for(FileChangeEditList item2 :list){
					//每一个文件 都去比较一次
					String oldPath = item2.getOldFilePath();
					String fileName = getClassNameFromPath(oldPath);
					//commit change的文件
					System.out.println(oldPath);
					Map<String,List<MethodDeclaration>> codeFromRelease = this.mappingTagStrToMethodDeclarationList(oldPath, fileName,candidate);
					
					InputStream fileInputStream = tagCmd.extractAndReturnInputStream(oldPath, parent.getName());
					List<MethodDeclaration> codePrev = JavaParserFactory.parseInputStreamGetOverlapMethodDeclarationList(fileInputStream, fileName, item2);
					List<String> tagNameList = this.compareTwoSet(codeFromRelease,codePrev);
					if(tagNameList==null){
						tagNameListAll = tagNameList;
					}else{
						tagNameListAll.retainAll(tagNameList);
					}
				}
				if(tagNameListAll!=null){
					for(String tmp:tagNameListAll){
						System.out.println(tmp);
					}
				}else{
					System.out.println("No Tag commit matched");
				}
				
			}
			
		}
		br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getClassNameFromPath(String path){
		String fileName = path.substring(path.lastIndexOf("/")+1);
		return fileName.substring(0, fileName.length()-5);
	}
	
	public List<String> compareTwoSet(Map<String,List<MethodDeclaration>> releasedTagMethod,List<MethodDeclaration> mList){
		List<String> matchedTagList = new ArrayList<String>();
		for(Entry<String,List<MethodDeclaration>> item: releasedTagMethod.entrySet()){
			String tagName = item.getKey();
			List<MethodDeclaration> mList2 = item.getValue();
			boolean isTagCommitMatch = true;
			for(MethodDeclaration changeCode:mList){
				boolean isMatch = false;
				for(MethodDeclaration tagRelease:mList2){
					if(tagRelease.getDeclarationAsString().equals(changeCode.getDeclarationAsString()) 
							&& tagRelease.getBody().get().toString().equals(changeCode.getBody().get().toString())){
							System.out.println("mapped");
							isMatch = true;
							break;
					}
				}
				// isMatch false没找到对应
				if(!isMatch){
					isTagCommitMatch = false;
					break;
				}
				// true 找到对应的
			}
			if(isTagCommitMatch){
				matchedTagList.add(tagName);
			}
		}
		return matchedTagList;
	}
	
	public Map<String,List<MethodDeclaration>> mappingTagStrToMethodDeclarationList(String oldPath,String className,List<RevCommit> candidate){
		Map<String,List<MethodDeclaration>> result = new HashMap<String,List<MethodDeclaration>>();
		
		for(int i=0;i<candidate.size();i++){
			InputStream fileInputStream  = tagCmd.extractAndReturnInputStream(oldPath, candidate.get(i).getName());
			List<MethodDeclaration> mList = JavaParserFactory.parseFileGetAllMethodDeclaration(fileInputStream, className);
			result.put(this.commitAndTagMap.get(candidate.get(i)), mList);
		}
		return result;
	}

	

}
