package edu.fdu.se.gitrepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;

import edu.fdu.se.bean.AndroidSDKJavaFile;
import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.dao.AndroidSDKJavaFileDAO;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.git.RepositoryHelper;

public class CheckoutTaggedCommit {

	public static void run() {
		JGitTagCommand cmd = new JGitTagCommand(
				ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH) + "/.git");
		String commitHash = cmd.commitIdOfTag("b1b26a80d388371e2745caafc368fd0fe6a7866f");
		System.out.println(commitHash);
		List<AndroidSDKJavaFile> mList = AndroidSDKJavaFileDAO.selectAllFileBySDKVersion(25);
		List<String> wrongedFile = new ArrayList<String>();
		int cnt = 0;
		int cnt2 = 0;
		for (AndroidSDKJavaFile file : mList) {
			String filePath = file.getFileFullPath();
			File localFile = new File(filePath);
			long length = localFile.length();
			String gitPath = SDKFileToRepoFilePath.v26PathMapping(file);
			boolean flag = true;
			byte[] gitFile = null;
			try {
				gitFile = cmd.extract(gitPath, commitHash);
				if (length != gitFile.length) {
					System.out.println("Error");
					System.out.println("Src:" + length);
					System.out.println("Dst:" + gitFile.length);
					// flag = false;
					cnt++;
				}
			} catch (Exception e) {
				wrongedFile.add(gitPath);
				cnt2++;
				System.err.println("ERR0R Wrong path");
			}
			// if(flag==false){
			// FileOutputStream fos;
			// try {
			// fos = new FileOutputStream(new File("D:/"+file.getFileName()));
			// fos.write(gitFile);
			// fos.close();
			// } catch ( IOException e) {
			// e.printStackTrace();
			// }
			// break;
			// }

		}
		System.out.println("-----------------------------------------");
		for (String a : wrongedFile) {
			System.out.println(a);
		}
		System.out.println(cnt);
		System.out.println(cnt2);
	}

	public static void main(String args[]) {
		run();
		// test();

	}

	public static void test() {
		JGitCommand cmd = new JGitCommand("C:/Users/huangkaifeng/Desktop/testgit/.git");
		String s = "ba399911ef469a3585e123a3a317846219fc071c";
		System.out.println(s);
		byte[] gitFile = cmd.extract("out.txt", s);
		System.out.println(gitFile == null);
		// List<AndroidSDKJavaFile> mList =
		// AndroidSDKJavaFileDAO.selectAllFileBySDKVersion(26);
		// for(AndroidSDKJavaFile file:mList){
		//// // src file
		// String filePath = file.getFileFullPath();
		// File localFile = new File(filePath);
		// long length = localFile.length();
		// String subFilePath = file.getSubSubCategoryPath();
		// String gitPath = "core/java"+subFilePath.replace('\\','/');
		// System.out.println(gitPath);
		// try{
		//
		// System.out.println("Src:"+length);
		// System.out.println("Dst:"+gitFile.length);
		// int cnt =0;
		// if(length != gitFile.length){
		// System.out.println("Error");
		// cnt ++;
		// }
		// }catch(Exception e){
		//// e.printStackTrace();
		// System.err.println("ERR0R Wrong path");
		// }
		//
		// FileOutputStream fos;
		// try {
		// fos = new FileOutputStream(new File("D:/aaa.java"));
		// fos.write(gitFile);
		// fos.close();
		// } catch ( IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
