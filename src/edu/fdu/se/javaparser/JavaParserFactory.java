package edu.fdu.se.javaparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.commitcodeinfo.CommitCodeInfo;
import edu.fdu.se.git.commitcodeinfo.FileChangeEditList;
import edu.fdu.se.main.gitgui.JGitRepoManager;

public class JavaParserFactory {
	/**
	 * commit prev的代码 + edit list 
	 * output： edit list所在的method
	 * @param is
	 * @param className
	 * @param fileChangeEditList
	 * @return
	 */
	public static Set<MethodDeclaration> parseInputStreamGetOverlapMethodDeclarationList(InputStream is,
			String className, FileChangeEditList fileChangeEditList) {
		Set<MethodDeclaration> changedMethod = new HashSet<MethodDeclaration>();
		EditList editList = fileChangeEditList.getEditList();
		CompilationUnit compilationUnit = JavaParser.parse(is);
		Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName(className);
		if (!classA.isPresent()) {
			return null;
		}
		ClassOrInterfaceDeclaration classAA = classA.get();
		List<MethodDeclaration> mDeclaration = classAA.getMethods();
		for(Edit e:editList){
			int beginA = e.getBeginA();
			int beginB = e.getBeginB();
			int endA = e.getEndA();
			int endB = e.getEndB();
			if(beginA == beginB){
				
			}else if(beginB == endB){
			
			}else{
				
			}
			for (MethodDeclaration item : mDeclaration) {
				int  methodBegin = item.getBegin().get().line;
				int methodEnd = item.getEnd().get().line;
				if(beginA>= methodBegin && endA< methodEnd){
					// in method item 表示在方法中
					
					changedMethod.add(item);
					break;
				}
			}
		}
		for(MethodDeclaration item:changedMethod){
			System.out.println("\t\tBuggy Method:"+item.getDeclarationAsString());
		}
		
		return changedMethod;
	}


	public static List<MethodDeclaration> parseFileGetAllMethodDeclaration(InputStream is, String className) {
		CompilationUnit compilationUnit = JavaParser.parse(is);
		Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName(className);
		if (classA.isPresent()) {
			ClassOrInterfaceDeclaration classAA = classA.get();
			List<MethodDeclaration> mDeclaration = classAA.getMethods();
			return mDeclaration;
		} else {
			return null;
		}
	}

	public static List<MethodDeclaration> parseFileGetAllMethodDeclaration(String filePath, String className) {
		CompilationUnit compilationUnit = null;
		try {
			compilationUnit = JavaParser.parse(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName(className);
		if (classA.isPresent()) {
			ClassOrInterfaceDeclaration classAA = classA.get();
			List<MethodDeclaration> mDeclaration = classAA.getMethods();
			return mDeclaration;
		} else {
			return null;
		}
	}

//	public static void main(String args[]) {
//		List<MethodDeclaration> contents = parseFileGetAllMethodDeclaration(
//				"D:/Workspace/Android_Diff/SDKTools/sources/2017-12-21/android-26/android/accessibilityservice/AccessibilityButtonController.java",
//				"AccessibilityButtonController");
//		for (MethodDeclaration item : contents) {
//			System.out.println(item.getDeclarationAsString());
//			System.out.println(item.getBody().get().toString());
//		}
//	}
	public static void main(String args[]){
		
		InputStream prev = JGitRepoManager.getInstance().
				myCmd.extractAndReturnInputStream("services/core/java/com/android/server/ConnectivityService.java", "964c76b368889e82b820493f140aa91f66f76a92");
		InputStream curr = JGitRepoManager.getInstance().
				myCmd.extractAndReturnInputStream("services/core/java/com/android/server/ConnectivityService.java", "a21d687c2431f6084e9eeaad8182c41c9ee3eb32");
//		FileWriter.writeInAll("D:/prev.java", prev);
		FileWriter.writeInAll("D:/curr.java", curr);
		FileChangeEditList a=null;
		CommitCodeInfo cci = JGitRepoManager.getInstance().myCmd.getCommitFileEditSummary("a21d687c2431f6084e9eeaad8182c41c9ee3eb32", JGitCommand.JAVA_FILE);
		for(Entry<RevCommit,List<FileChangeEditList>> item : cci.getFileDiffEntryMap().entrySet()){
			List<FileChangeEditList> mList = item.getValue();
			for(FileChangeEditList m :mList){
				if("services/core/java/com/android/server/ConnectivityService.java".equals(m.getOldFilePath())){
					EditList el = m.getEditList();
					a = m;
					
					for(Edit e:el){
						int beginA=e.getBeginA();
						int beginB=e.getBeginB();
						int endA=e.getEndA();
						int endB=e.getEndB();
						if(beginA == endA && beginB < endB ){
							System.out.println("insert startA 之后一行插入");
							System.out.println(e.getBeginA()+" "+e.getEndA());
						}else if(beginA < endA && beginB == endB){
							System.out.println("delete");
							System.out.println(e.getBeginA()+" "+e.getEndA());
						}else if(beginA < endA && beginB < endB){
							System.out.println("replace startA 之后一行 replace 到endA");
							System.out.println(e.getBeginA()+" "+e.getEndA());
						}

						
					}
					System.out.println(m.getPatchScript());
					
				}
			}
		}
		Set<MethodDeclaration> mmList = JavaParserFactory.parseInputStreamGetOverlapMethodDeclarationList(prev, "ConnectivityService", a);
		System.out.println("--------------------------------");
		for(MethodDeclaration item:mmList){
			System.out.println(item.getDeclarationAsString());
		}
		
	}
}
