package edu.fdu.se.javaparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import edu.fdu.se.git.commitcodeinfo.FileChangeEditList;


public class JavaParserFactory {
	

	public static List<MethodDeclaration> parseInputStreamGetOverlapMethodDeclarationList(InputStream is,String className,FileChangeEditList fileChangeEditList){
		List<MethodDeclaration> changedMethod = new ArrayList<MethodDeclaration>();
		EditList list = fileChangeEditList.getEditList();
		List<Integer[]> lineChangeRangeList = new ArrayList<Integer[]>();
		for(Edit e:list){
			e.getBeginA();
			Integer[] mInteger = {e.getBeginA(),e.getEndA()};
			lineChangeRangeList.add(mInteger);
		}
		
		CompilationUnit compilationUnit = JavaParser.parse(is);
		Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName(className);
		if (classA.isPresent()) {
			  ClassOrInterfaceDeclaration classAA = classA.get();
			  List<MethodDeclaration> mDeclaration = classAA.getMethods();
			  for(MethodDeclaration item:mDeclaration){
//				  System.out.println(item.getDeclarationAsString());
				  Optional<Position> begin = item.getBegin();
				  Optional<Position> end = item.getEnd();
				  int a = begin.get().line;
				  int b = end.get().line;
				  for(Integer[] candidate:lineChangeRangeList){
					  int s = candidate[0];
					  int e = candidate[1];
					  if(a<=s&&e<=b){
						  changedMethod.add(item);
						  break;
					  }
				  }
			  }
			  return changedMethod;
		}else{
			return null;
		}
	}
	
	public static List<MethodDeclaration> parseFileGetAllMethodDeclaration(InputStream is,String className){
		CompilationUnit compilationUnit = JavaParser.parse(is);
		Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName(className);
		if (classA.isPresent()) {
			  ClassOrInterfaceDeclaration classAA = classA.get();
			  List<MethodDeclaration> mDeclaration = classAA.getMethods();
			  return mDeclaration;
		}else{
			return null;
		}
	} 
	
	public static List<MethodDeclaration> parseFileGetAllMethodDeclaration(String filePath,String className){
		CompilationUnit compilationUnit = null;
		try {
			compilationUnit = JavaParser.parse(new File(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName(className);
		if (classA.isPresent()) {
			  ClassOrInterfaceDeclaration classAA = classA.get();
			  List<MethodDeclaration> mDeclaration = classAA.getMethods();
			  return mDeclaration;
		}else{
			return null;
		}
	} 
	public static void main(String args[]){
		List<MethodDeclaration> contents = parseFileGetAllMethodDeclaration("D:/Workspace/Android_Diff/SDKTools/sources/2017-12-21/android-26/android/accessibilityservice/AccessibilityButtonController.java","AccessibilityButtonController");
		for(MethodDeclaration item :contents){
			System.out.println(item.getDeclarationAsString());
			System.out.println(item.getBody().get().toString());
		}
	}
}
