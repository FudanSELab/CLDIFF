package cn.edu.fudan.se.api;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.symbolsolver.javaparser.Navigator;

import cn.edu.fudan.se.ast.AstParser;
import cn.edu.fudan.se.db.DB;
import cn.edu.fudan.se.util.JsonFileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JarAnalyzer {
	
	private JSONArray clazzs = new JSONArray();
//	private static String SAVE_PATH = "C:/Users/yw/Desktop/test/";
//	private static String INPUT_FILE_PATH = "C:/Users/yw/Desktop/test/";
	private static String DECOMPILED_LIB_PATH = "F:/GP/decompile/";
	private boolean go = false;		
	
	private void parseFile(int versionTypeId,String dir,String pathPrefix) {
		File or = new File(dir);
		File[] files = or.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".java") && !file.getName().contains("$")) {
					String absolutePath = file.getAbsolutePath();
					absolutePath = absolutePath.substring(0, absolutePath.length()-5);
					if(absolutePath.replace("\\", "/").startsWith(pathPrefix.replace("\\", "/"))) {
						String apiClass = absolutePath.substring(pathPrefix.length()+1).replace("/", ".").replace("\\", ".");						
//						if(go) {
						System.out.println("+++++++++++++++++++++++++++ "+apiClass);
						try {
							Map apis = getApi(AstParser.getCompilationUnit(file.getAbsolutePath()),apiClass);					
							apiPersistence(versionTypeId,apiClass,apis);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (OutOfMemoryError e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
						}	
//						}
//						if(apiClass.equals("org.sqlite.SQLite")) {
//							go =true;
//						}
					}
					
				} 
				else if (file.isDirectory()) {
					parseFile(versionTypeId,file.getAbsolutePath(),pathPrefix);
				}
			}
		}
	}
	
	private void parseFile(String dir,String pathPrefix) {
		File or = new File(dir);
		File[] files = or.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".java") && !file.getName().contains("$")) {
					String absolutePath = file.getAbsolutePath();
					absolutePath = absolutePath.substring(0, absolutePath.length()-5);
					if(absolutePath.replace("\\", "/").startsWith(pathPrefix.replace("\\", "/"))) {
						String apiClass = absolutePath.substring(pathPrefix.length()+1).replace("/", ".").replace("\\", ".");						
						System.out.println("+++++++++++++++++++++++++++ "+apiClass);
						try {
							Map apis = getApi(AstParser.getCompilationUnit(file.getAbsolutePath()),apiClass);
							JSONObject clazz = apiPersistence(apiClass,apis);
							if(clazz != null) {
								clazzs.add(clazz);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (OutOfMemoryError e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
						}							
					}
					
				} 
				else if (file.isDirectory()) {
					parseFile(file.getAbsolutePath(),pathPrefix);
				}
			}
		}
	}
	
	private JSONObject apiPersistence(String apiClass,Map apis) {	
		if(apis.size() == 0)
			return null;
		JSONArray array = new JSONArray();
		for (Map.Entry entry : apis.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("name", entry.getKey());
			obj.put("remark", entry.getValue());
			array.add(obj);
		}
		JSONObject clazz = new JSONObject();
		clazz.put("name", apiClass);
		clazz.put("api", array);
		return clazz;
	}
	
	private void apiPersistence(int versionTypeId,String apiClass,Map apis) {
		if(apis.size() == 0)
			return;
//		ResultSet rrs = DB.query("SELECT * FROM `api_classes` where `version_type_id`=" + versionTypeId);
//		try {
//			while (rrs.next()) {
//				return;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String sql = "INSERT INTO api_classes(version_type_id,class_name) VALUES ("+ versionTypeId + ",\'" + apiClass + "\')";
		DB.update(sql);
		int classId=-1;
		ResultSet rs = DB.query("select LAST_INSERT_ID()");
		try {
			if (rs.next())
				classId = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Map.Entry entry : apis.entrySet()) {
			sql = "INSERT INTO api_interface(class_id,name,remark) VALUES ("+ classId + ",\'" + entry.getKey() + "\', \'"+entry.getValue()+"\')";
			DB.update(sql);
		}
	}
	
	private void printList(Map apis) {
		for (Map.Entry entry : apis.entrySet()) {
			System.out.println(entry.getKey());
		}
	}
	
	public Map getApi(CompilationUnit cu,String importClass) throws IOException {
		Map apis = new HashMap<>();		
		if(cu ==null)
			return apis;
		String clazz = importClass.substring(importClass.lastIndexOf(".")+1);
		apis.put(clazz, "ClassOrInterface");
		List fieldDeclarations = Navigator.findAllNodesOfGivenClass(cu, FieldDeclaration.class);
		List classOrInterfaceDeclarations = Navigator.findAllNodesOfGivenClass(cu, ClassOrInterfaceDeclaration.class);
		List constructorDeclarations = Navigator.findAllNodesOfGivenClass(cu, ConstructorDeclaration.class);
		List methodDeclarations = Navigator.findAllNodesOfGivenClass(cu, MethodDeclaration.class);
		fieldDeclarations.forEach(fd -> {
			if(fd.isPublic()) {
				List children= fd.getChildNodes();
				for(Node c:children) {
					if(c instanceof VariableDeclarator) {
						String field = ((VariableDeclarator)c).getNameAsString();
						if(!apis.containsKey(field))
							apis.put(field, "Field");
					}
				}
			}
			
		}); 
		methodDeclarations.forEach(md -> {
			if(md.isPublic()) {
//				System.out.println(md);
				String method = md.getDeclarationAsString(false, false, false);
				int index = method.indexOf(" ");
				if(index >= 0)
					method = method.substring(index+1);
				if(!apis.containsKey(method))
					apis.put(method, "Method");
			}
			
		}); 
		constructorDeclarations.forEach(cd -> {
			if(cd.isPublic()) {
				String constructor = cd.getDeclarationAsString(false, false, false);
				if(!apis.containsKey(constructor))
					apis.put(constructor, "Constructor");
//				System.out.println(constructor);
			}			
		}); 
		classOrInterfaceDeclarations.forEach(cid -> {
			if(cid.isPublic()) {
				String classOrInterface = cid.getNameAsString();
				if(!apis.containsKey(classOrInterface))
					apis.put(classOrInterface, "ClassOrInterface");
			}
		}); 
		
		return apis;
	}
	
	public void enterParseById(int typeId) {
		ResultSet rs = DB.query("SELECT * FROM `version_types` where `type_id`=" + typeId);
		try {
			while (rs.next()) {
				String jackage_url = rs.getString("jar_package_url");
				if(jackage_url.endsWith(".jar")) {
					String path = DECOMPILED_LIB_PATH+jackage_url.substring(0, jackage_url.length()-4)+"_decompile";
					File file = new File(path);
					if(!file.exists()) {
						System.out.println("package : " + path +"  not exists");
						return;
					}
					enterParse(typeId,path);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void enterParse(int versionTypeId,String decompiledLibPath) {			
		parseFile(versionTypeId,decompiledLibPath,decompiledLibPath);
	}
	
	public void enterSave(String dirPath,String decompiledLibName,String savePath) {			
		parseFile(dirPath + decompiledLibName,dirPath + decompiledLibName);
		JsonFileUtil.save(savePath + decompiledLibName+".txt", clazzs);
	}
	
	public static void main(String[] args) {
		for(int i = 801;i<=1000;i++) {
			JarAnalyzer ja = new JarAnalyzer();		
			System.out.println(i);
			ja.enterParseById(i);
		}
	}
}
