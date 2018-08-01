package edu.fdu.se.javaparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.commitcodeinfo.CommitCodeInfo;
import edu.fdu.se.git.commitcodeinfo.FileChangeEditList;

public class JavaParserFactory {
	
	public static CompilationUnit getCompilationUnit(InputStream is){
		CompilationUnit compilationUnit = JavaParser.parse(is);
		return compilationUnit;
	}
	
	public static CompilationUnit getCompilationUnit(String filePath){
		CompilationUnit compilationUnit = null;
		try {
			compilationUnit = JavaParser.parse(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return compilationUnit;
	}
	public static void removeCommentss(Node n) {
		List<Comment> mList = n.getAllContainedComments();
		List<Comment> mList2 = n.getOrphanComments();
		for (Comment m : mList) {
			m.remove();
		}
		for (Comment m : mList2) {
			m.remove();
		}
	}

	/**
	 * prev + curr
	 *
	 * @param cod class name
	 */
	public static void traverseClassOrInterfaceDeclarationRemoveComment(ClassOrInterfaceDeclaration cod) {
		cod.removeJavaDocComment();
		cod.removeComment();
		NodeList nodeList = cod.getMembers();
		for (int i = nodeList.size() - 1; i >= 0; i--) {
			Node node = nodeList.get(i);
			node.removeComment();
			removeCommentss(node);
			if (node instanceof ClassOrInterfaceDeclaration) {
				traverseClassOrInterfaceDeclarationRemoveComment((ClassOrInterfaceDeclaration) node);
			}
			if (node instanceof AnnotationDeclaration) {
//				preprocessedTempData.addToRemoveList((BodyDeclaration) node);
				node.remove();
			}
			if (node instanceof ConstructorDeclaration) {
				ConstructorDeclaration cd = (ConstructorDeclaration) node;
				cd.removeJavaDocComment();
			}
			if (node instanceof MethodDeclaration) {
				MethodDeclaration md = (MethodDeclaration) node;
				md.removeJavaDocComment();
			}
			if (node instanceof FieldDeclaration) {
				FieldDeclaration fd = (FieldDeclaration) node;
				fd.removeJavaDocComment();
			}
			if (node instanceof InitializerDeclaration) {
				InitializerDeclaration idd = (InitializerDeclaration) node;
				idd.removeJavaDocComment();
			}
		}
	}

	public static void removeAllCommentsOfCompilationUnit(CompilationUnit cu) {
		cu.removeComment();
		cu.removePackageDeclaration();
		NodeList imports = cu.getImports();
		for (int i = imports.size() - 1; i >= 0; i--) {
			Node n = imports.get(i);
			n.remove();
		}
		assert(cu.getTypes() != null);
		assert(cu.getTypes().size() == 1);
		TypeDeclaration mTypeCurr = cu.getType(0);
		ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) mTypeCurr;
		traverseClassOrInterfaceDeclarationRemoveComment(cod);
	}


	/**
	 * commit prev的代码 + edit list output： edit list所在的method
	 * get buggy methods
	 * @param is
	 * @param className
	 * @param fileChangeEditList
	 * @return
	 */
	public static Set<BodyDeclaration> parseInputStreamGetOverlapMethodDeclarationList(InputStream is,
			String className, FileChangeEditList fileChangeEditList) {
		Set<BodyDeclaration> changedMethod = new HashSet<BodyDeclaration>();
		EditList editList = fileChangeEditList.getEditList();
		List<BodyDeclaration> mDeclaration = parseCompilationUnitGetAllMethodDeclaration(getCompilationUnit(is));
		for (Edit e : editList) {
			int beginA = e.getBeginA();
			int beginB = e.getBeginB();
			int endA = e.getEndA();
			int endB = e.getEndB();
			if (beginA == beginB) {

			} else if (beginB == endB) {

			} else {

			}
			for (BodyDeclaration item : mDeclaration) {
//				int methodBegin = item.getBegin().get().line;
//				int methodEnd = item.getEnd().get().line;
//				if (beginA >= methodBegin && endA < methodEnd) {
//					// in method item 表示在方法中
//					changedMethod.add(item);
//					break;
//				}
			}
		}
		for (BodyDeclaration item : changedMethod) {
			if(item instanceof MethodDeclaration){
				MethodDeclaration md = (MethodDeclaration) item;
				System.out.println("\t\tBuggy Method:" + md.getDeclarationAsString());
				
			}else if(item instanceof ConstructorDeclaration){
				ConstructorDeclaration cd = (ConstructorDeclaration) item;
				System.out.println("\t\tBuggy Method:" + cd.getDeclarationAsString());
			}else{
				System.err.println("ERROR wrong");
			}
		}

		return changedMethod;
	}


	/**
	 * 方法声明，所有的，包括内部类
	 * @param compilationUnit
	 * @return
	 */
	public static List<BodyDeclaration> parseCompilationUnitGetAllMethodDeclaration(CompilationUnit compilationUnit){
		assert(compilationUnit.getTypes() != null);
		assert(compilationUnit.getTypes().size() == 1);
		TypeDeclaration mType = compilationUnit.getType(0);
		NodeList nodeList = mType.getMembers();
		List<BodyDeclaration> mMethodDeclarationList = new ArrayList<BodyDeclaration>();
		for(int i  = 0; i < nodeList.size();i++){
			Node node = nodeList.get(i);
			if(node instanceof MethodDeclaration){
				mMethodDeclarationList.add((MethodDeclaration)node);
			}
			if(node instanceof AnnotationDeclaration){
//				System.out.println(node.toString());
			}
			if(node instanceof ClassOrInterfaceDeclaration){
				ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration)node;
				List<MethodDeclaration> tmpMethod = innerClass.getMethods();
				List<ConstructorDeclaration> cList = innerClass.getConstructors();
				mMethodDeclarationList.addAll(tmpMethod);
				mMethodDeclarationList.addAll(cList);
			}
			if(node instanceof ConstructorDeclaration){
				ConstructorDeclaration cd = (ConstructorDeclaration) node;
				mMethodDeclarationList.add(cd);
				
			}
		}
		return mMethodDeclarationList;
	}
	
	public static List<BodyDeclaration> parseFileGetAllMethodDeclaration(String filePath) {
		return parseCompilationUnitGetAllMethodDeclaration(getCompilationUnit(filePath));
	}
	
	public static List<BodyDeclaration> parseFileGetAllMethodDeclaration(InputStream is) {
		return parseCompilationUnitGetAllMethodDeclaration(getCompilationUnit(is));
	}

	public static void main(String args[]) {
//		List<MethodDeclaration> contents = parseFileGetAllMethodDeclaration("D:/commit_curr",
//				"InputMethodManagerService");
		List<BodyDeclaration> contens = parseCompilationUnitGetAllMethodDeclaration(getCompilationUnit("D:/commit_curr"));
		// for (MethodDeclaration item : contents) {
		// System.out.println(item.getDeclarationAsString());
		// System.out.println(item.getBody().get().toString());
		// }
	}
	// public static void main(String args[]){

	// InputStream prev = RepoDataHelper.getInstance().
	// myCmd.extractAndReturnInputStream("services/core/java/com/android/server/ConnectivityService.java",
	// "964c76b368889e82b820493f140aa91f66f76a92");
	// InputStream curr = RepoDataHelper.getInstance().
	// myCmd.extractAndReturnInputStream("services/core/java/com/android/server/ConnectivityService.java",
	// "a21d687c2431f6084e9eeaad8182c41c9ee3eb32");
	//// FileWriter.writeInAll("D:/prev.java", prev);
	// FileWriter.writeInAll("D:/curr.java", curr);
	// FileChangeEditList a=null;
	// CommitCodeInfo cci =
	// RepoDataHelper.getInstance().myCmd.getCommitFileEditSummary("a21d687c2431f6084e9eeaad8182c41c9ee3eb32",
	// JGitCommand.JAVA_FILE);
	// for(Entry<RevCommit,List<FileChangeEditList>> item :
	// cci.getFileDiffEntryMap().entrySet()){
	// List<FileChangeEditList> mList = item.getValue();
	// for(FileChangeEditList m :mList){
	// if("services/core/java/com/android/server/ConnectivityService.java".equals(m.getOldFilePath())){
	// EditList el = m.getEditList();
	// a = m;
	//
	// for(Edit e:el){
	// int beginA=e.getBeginA();
	// int beginB=e.getBeginB();
	// int endA=e.getEndA();
	// int endB=e.getEndB();
	// if(beginA == endA && beginB < endB ){
	// System.out.println("insert startA 之后一行插入");
	// System.out.println(e.getBeginA()+" "+e.getEndA());
	// }else if(beginA < endA && beginB == endB){
	// System.out.println("delete");
	// System.out.println(e.getBeginA()+" "+e.getEndA());
	// }else if(beginA < endA && beginB < endB){
	// System.out.println("replace startA 之后一行 replace 到endA");
	// System.out.println(e.getBeginA()+" "+e.getEndA());
	// }
	// }
	// System.out.println(m.getPatchScript());
	// }
	// }
	// }
	// Set<MethodDeclaration> mmList =
	// JavaParserFactory.parseInputStreamGetOverlapMethodDeclarationList(prev,
	// "ConnectivityService", a);
	// System.out.println("--------------------------------");
	// for(MethodDeclaration item:mmList){
	// System.out.println(item.getDeclarationAsString());
	// }
	//
	// }
}
