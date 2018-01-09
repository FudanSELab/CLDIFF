package edu.fdu.se.main.ast;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

import edu.fdu.se.javaparser.JavaParserFactory;

public class PreprocessingSDKClass {
	
	public static void main(String args[]){
		CompilationUnit cu = JavaParserFactory.getCompilationUnit("D:/test.java");
		for(TypeDeclaration nl :cu.getTypes()){
			System.out.println(nl.getNameAsString());
			List<MethodDeclaration> md = nl.getMethods();
			for(MethodDeclaration item:md){
				BlockStmt bs = item.getBody().get();
				
			}
		}
	}

}
