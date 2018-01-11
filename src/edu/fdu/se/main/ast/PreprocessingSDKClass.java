package edu.fdu.se.main.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

import edu.fdu.se.fileutil.FileWriter;
import edu.fdu.se.javaparser.JavaParserFactory;

public class PreprocessingSDKClass {
	
	public static void main(String args[]){
//		new PreprocessingSDKClass().compareTwoSDKFile3("D:/Workspace/Android_Diff/SDK_Files_15-26/android-25/android/accessibilityservice/AccessibilityService.java",
//				"D:/Workspace/Android_Diff/SDK_Files_15-26/android-26/android/accessibilityservice/AccessibilityService.java");
		new PreprocessingSDKClass().test("D:/test.java");
	}
	/*
	 * 
	 */
	public static void compareTwoSDKFile(String prev,String curr){
		
		CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(prev);
		CompilationUnit cuCurr = JavaParserFactory.getCompilationUnit(curr);
		FileWriter.writeInAll("D:/cuPrev.java", cuPrev.toString());
		FileWriter.writeInAll("D:/cuCurr.java", cuCurr.toString());
		List<BodyDeclaration> mListPrev = JavaParserFactory.parseCompilationUnitGetAllMethodDeclaration(cuPrev);
		List<BodyDeclaration> mListCurr = JavaParserFactory.parseCompilationUnitGetAllMethodDeclaration(cuCurr);
		Map<BodyDeclaration,Integer> prevMethodMapHash = new HashMap<BodyDeclaration,Integer>();
		Map<BodyDeclaration,String> prevMethodMapName = new HashMap<BodyDeclaration,String>();
		Map<BodyDeclaration,Integer> prevMethodMapIsVisited = new HashMap<BodyDeclaration,Integer>();
		for(BodyDeclaration item:mListPrev){
			prevMethodMapHash.put(item, item.hashCode());
			if(item instanceof MethodDeclaration){
				prevMethodMapName.put(item, ((MethodDeclaration)item).getNameAsString());
			}else if(item instanceof ConstructorDeclaration){
				prevMethodMapName.put(item, ((ConstructorDeclaration)item).getNameAsString());
			}else if(item instanceof FieldDeclaration){
			
			}
			
			
		}
		for(BodyDeclaration item:mListCurr){
			int v =item.hashCode();
			Entry<BodyDeclaration,Integer> e = containsValue(prevMethodMapHash,v);
			if(e != null){
				// 完全相同
				item.remove();
				e.getKey().remove();
			}else {
				// 其他
				if(item instanceof MethodDeclaration){
					MethodDeclaration md = (MethodDeclaration)item;
					System.out.println(md.getNameAsString());
				}
			}
		}
		
		
	}
	
	public static Entry<BodyDeclaration,Integer> containsValue(Map<BodyDeclaration,Integer> mMap,int v){
		for(Entry<BodyDeclaration,Integer> item:mMap.entrySet()){
			if(item.getValue().intValue() == v){
				return item;
			}
		}
		return null;
	}
	
	
	public static void compareTwoSDKFile2(String prev,String curr){
		CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(prev);
		CompilationUnit cuCurr = JavaParserFactory.getCompilationUnit(curr);
		
		assert cuPrev.getTypes() != null;
		assert cuPrev.getTypes().size() == 1;
		assert cuCurr.getTypes() != null;
		assert cuCurr.getTypes().size() == 1;
		TypeDeclaration mTypePrev = cuPrev.getType(0);
		TypeDeclaration mTypeCurr = cuCurr.getType(0);
		NodeList nodeList = mTypeCurr.getMembers();
		Set<String> m = new HashSet<String>();
		
//		List<BodyDeclaration> mMethodDeclarationList = new ArrayList<BodyDeclaration>();
		for(int i  = 0; i < nodeList.size();i++){
			Node node = nodeList.get(i);
			m.add(node.getClass().toString());
			if(node instanceof AnnotationDeclaration){
//				System.out.println(node.toString());
			}
			if(node instanceof ConstructorDeclaration){
//				ConstructorDeclaration cd = (ConstructorDeclaration) node;
//				mMethodDeclarationList.add(cd);
			}
			if(node instanceof MethodDeclaration){
				MethodDeclaration md = (MethodDeclaration) node;
//				mMethodDeclarationList.add((MethodDeclaration)node);
				mTypePrev.getMethodsBySignature(md.getNameAsString());
			}

			if(node instanceof ClassOrInterfaceDeclaration){
				ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration)node;
//				List<MethodDeclaration> tmpMethod = innerClass.getMethods();
//				List<ConstructorDeclaration> cList = innerClass.getConstructors();
//				mMethodDeclarationList.addAll(tmpMethod);
//				mMethodDeclarationList.addAll(cList);
			}

		}
		for(String a : m){
			System.out.println(a);
		}
	}
	
	/**
	 * prev的method，field 都到map里做标记
	 * @param cu
	 */
	public void loadPrev(CompilationUnit cu){
		bdMapPrev = new HashMap<String,BodyDeclaration>();
		bdMapPrevMethodName = new HashMap<String,List<BodyDeclaration>>();
		visitedPrevNode = new HashMap<BodyDeclaration,Integer>();
		TypeDeclaration mTypePrev = cu.getType(0);
		NodeList nodeList = mTypePrev.getMembers();
		for(int i  = 0; i < nodeList.size();i++){
			Node node = nodeList.get(i);
			if(node instanceof AnnotationDeclaration){
				//TODO
				node.remove();
			}
			if(node instanceof ConstructorDeclaration){
				ConstructorDeclaration cd = (ConstructorDeclaration) node;
				cd.removeComment();
				cd.removeJavaDocComment();
				bdMapPrev.put(cd.getDeclarationAsString(),cd);
				if(bdMapPrevMethodName.containsKey(cd.getNameAsString())){
					List<BodyDeclaration> mList = bdMapPrevMethodName.get(cd.getNameAsString());
					mList.add(cd);
				}else{
					List<BodyDeclaration> mList = new ArrayList<BodyDeclaration>();
					mList.add(cd);
					bdMapPrevMethodName.put(cd.getNameAsString(),mList);
				}
				
			}
			if(node instanceof MethodDeclaration){
				MethodDeclaration md = (MethodDeclaration) node;
				md.removeComment();
				md.removeJavaDocComment();
				bdMapPrev.put(md.getDeclarationAsString(),md);
				if(bdMapPrevMethodName.containsKey(md.getNameAsString())){
					List<BodyDeclaration> mList = bdMapPrevMethodName.get(md.getNameAsString());
					mList.add(md);
				}else{
					List<BodyDeclaration> mList = new ArrayList<BodyDeclaration>();
					mList.add(md);
					bdMapPrevMethodName.put(md.getNameAsString(),mList);
				}
			}
			if(node instanceof FieldDeclaration){
				FieldDeclaration md = (FieldDeclaration) node;
				md.removeJavaDocComment();
				md.removeComment();
				bdMapPrev.put(md.toString(),md);
			}

			if(node instanceof ClassOrInterfaceDeclaration){
				ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration)node;
				NodeList innerList = innerClass.getMembers();
				for(int j =0;j<innerList.size();j++){
					Node item2 = innerList.get(j);
					if(item2 instanceof ConstructorDeclaration){
						ConstructorDeclaration cd2 = (ConstructorDeclaration) item2;
						cd2.removeJavaDocComment();
						cd2.removeComment();
						bdMapPrev.put(innerClass.getNameAsString()+"."+cd2.getDeclarationAsString(),cd2);
						String key = innerClass.getNameAsString()+"."+cd2.getNameAsString();
						if(bdMapPrevMethodName.containsKey(key)){
							List<BodyDeclaration> mList = bdMapPrevMethodName.get(key);
							mList.add(cd2);
						}else{
							List<BodyDeclaration> mList = new ArrayList<BodyDeclaration>();
							mList.add(cd2);
							bdMapPrevMethodName.put(key,mList);
						}
					}
					if(item2 instanceof MethodDeclaration){
						MethodDeclaration md2 = (MethodDeclaration) item2;
						md2.removeJavaDocComment();
						md2.removeComment();
						bdMapPrev.put(innerClass.getNameAsString()+"."+md2.getDeclarationAsString(),md2);
						String key = innerClass.getNameAsString()+"."+md2.getNameAsString();
						if(bdMapPrevMethodName.containsKey(key)){
							List<BodyDeclaration> mList = bdMapPrevMethodName.get(key);
							mList.add(md2);
						}else{
							List<BodyDeclaration> mList = new ArrayList<BodyDeclaration>();
							mList.add(md2);
							bdMapPrevMethodName.put(key,mList);
						}
					}
					if(item2 instanceof FieldDeclaration){
						FieldDeclaration md2 = (FieldDeclaration) item2;
						md2.removeJavaDocComment();
						md2.removeComment();
						bdMapPrev.put(innerClass.getNameAsString()+"."+md2.toString(),md2);
					}
				}
				bdMapPrev.put(innerClass.getNameAsString(),innerClass);
			}
		}
	}
	private Map<String,BodyDeclaration> bdMapPrev;
	private Map<String,List<BodyDeclaration>> bdMapPrevMethodName;
	private Map<BodyDeclaration,Integer> visitedPrevNode;
	
	public void compareTwoSDKFile3(String prev,String curr){
		CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(prev);
		CompilationUnit cuCurr = JavaParserFactory.getCompilationUnit(curr);
		
		assert cuPrev.getTypes() != null;
		assert cuPrev.getTypes().size() == 1;
		assert cuCurr.getTypes() != null;
		assert cuCurr.getTypes().size() == 1;
		loadPrev(cuPrev);
		TypeDeclaration mTypeCurr = cuCurr.getType(0);
		NodeList nodeList = mTypeCurr.getMembers();
		for(int i  = 0; i < nodeList.size();i++){
			Node node = nodeList.get(i);
			if(node instanceof AnnotationDeclaration){
				node.remove();
			}
			if(node instanceof ConstructorDeclaration){
				ConstructorDeclaration cd = (ConstructorDeclaration) node;
				cd.removeComment();
				cd.removeJavaDocComment();
				if(this.bdMapPrev.containsKey(cd.getDeclarationAsString())){
					BodyDeclaration prevNode = this.bdMapPrev.get(cd.getDeclarationAsString());
					if(prevNode.hashCode() == cd.hashCode()){
						prevNode.remove();
						cd.remove();
					}else{
						System.out.println("Mapping pair not same");
					}
				}else if(this.bdMapPrevMethodName.containsKey(cd.getNameAsString())){
					List<BodyDeclaration> mOverload = this.bdMapPrevMethodName.get(cd.getNameAsString());
					// 可能为修改签名之后的方法，也可能为新增的方法
				}else {
					// new method
				}
			}
			if(node instanceof MethodDeclaration){
				MethodDeclaration md = (MethodDeclaration) node;
				md.removeComment();
				md.removeJavaDocComment();
				if(this.bdMapPrev.containsKey(md.getDeclarationAsString())){
					BodyDeclaration prevNode = this.bdMapPrev.get(md.getDeclarationAsString());
					if(prevNode.hashCode() == md.hashCode()){
						prevNode.remove();
						md.remove();
					}else{
						System.out.println("Mapping pair not same");
					}
				}else if(this.bdMapPrevMethodName.containsKey(md.getNameAsString())){
					List<BodyDeclaration> mOverload = this.bdMapPrevMethodName.get(md.getNameAsString());
					// 可能为修改签名之后的方法，也可能为新增的方法
				}else{
					// new method
				}
			}

			if(node instanceof ClassOrInterfaceDeclaration){
				ClassOrInterfaceDeclaration innerClass = (ClassOrInterfaceDeclaration)node;
			}

		}
		for(Entry<BodyDeclaration,Integer> item:this.visitedPrevNode.entrySet()){
			if(item.getValue()==1){
				continue;
			}
			// 其他节点为删除节点
		}
	}

	public void test(String a){
		CompilationUnit cuPrev = JavaParserFactory.getCompilationUnit(a);
		TypeDeclaration mTypeCurr = cuPrev.getType(0);
		NodeList nodeList = mTypeCurr.getMembers();
		for(int i=0;i<nodeList.size();i++){
			Node node = nodeList.get(i);
			MethodDeclaration md = (MethodDeclaration)node;
			md.removeComment();
			System.out.println(md.getDeclarationAsString());
			System.out.println(md.hashCode());
			
		}
	}

}
