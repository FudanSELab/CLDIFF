package edu.fdu.se.handlefile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreprocessClassData {
	private Map<String, Method> sameMethods = new HashMap<String, Method>();

	private Map<String, String> sameFields = new HashMap<String, String>();

	private Map<String, String> sameBlocks = new HashMap<String, String>();
	
	private Map<String, String> samePackages = new HashMap<String, String>();
	
	private Map<String, String> sameImports = new HashMap<String, String>();

	/**
	 * same method name, key:method signature,value:list[0]=prev list[1]=curr
	 */
	private Map<String, List<Method>> differentMethodsMap = new HashMap<String, List<Method>>();
	/**
	 * different method name
	 */
	private Map<String, Method> prevDeletedMethods = new HashMap<String, Method>();
	private Map<String, Method> currInsertedMethods = new HashMap<String, Method>();

	private Map<String, String> prevBlocks = new HashMap<String, String>();
	private Map<String, String> currBlocks = new HashMap<String, String>();
	
	private Map<String, String> prevPackages = new HashMap<String, String>();
	private Map<String, String> currPackages = new HashMap<String, String>();
	
	private Map<String, String> prevImports = new HashMap<String, String>();
	private Map<String, String> currImports = new HashMap<String, String>();
	/**
	 * field
	 */
	private Map<String, String> prevDeletedFields = new HashMap<String, String>();
	private Map<String, String> currInsertedFields = new HashMap<String, String>();

	

	public static void main(String[] args) {
		PreprocessClassData preprocessClassData = new PreprocessClassData();
		preprocessClassData.fileDiff("E:\\BpNet1.java", "E:\\BpNet2.java");
		preprocessClassData.print();
	}
	
	public void print() {
//		System.out.println("==========same import==========");  
//		for(Map.Entry<String, String> entry : sameImports.entrySet()){  
//		    System.out.println("Key = \n"+entry.getKey());  
//		    System.out.println("Value = \n"+entry.getValue());  
//		} 
//		System.out.println("==========prev import==========");  
//		for(Map.Entry<String, String> entry : prevImports.entrySet()){  
//		    System.out.println("Key = \n"+entry.getKey());  
//		    System.out.println("Value = \n"+entry.getValue());  
//		} 
//		System.out.println("==========curr import==========");  
//		for(Map.Entry<String, String> entry : currImports.entrySet()){  
//		    System.out.println("Key = \n"+entry.getKey());  
//		    System.out.println("Value = \n"+entry.getValue());  
//		} 
//		
//		System.out.println("==========same package==========");  
//		for(Map.Entry<String, String> entry : samePackages.entrySet()){  
//		    System.out.println("Key = \n"+entry.getKey());  
//		    System.out.println("Value = \n"+entry.getValue());  
//		} 
//		System.out.println("==========prev package==========");  
//		for(Map.Entry<String, String> entry : prevPackages.entrySet()){  
//		    System.out.println("Key = \n"+entry.getKey());  
//		    System.out.println("Value = \n"+entry.getValue());  
//		} 
//		System.out.println("==========curr package==========");  
//		for(Map.Entry<String, String> entry : currPackages.entrySet()){  
//		    System.out.println("Key = \n"+entry.getKey());  
//		    System.out.println("Value = \n"+entry.getValue());  
//		} 
		System.out.println("==========same method==========");  
		for(Map.Entry<String, Method> entry : sameMethods.entrySet()){  
		    System.out.println("Key = \n"+entry.getKey());  
		    System.out.println("Value = \n"+entry.getValue().getContent());  
		} 
		System.out.println("==========different method==========");  
		for(Map.Entry<String, List<Method>> entry : differentMethodsMap.entrySet()){  
		    System.out.println("Key = \n"+entry.getKey());  		    
		    System.out.println("Value = ");  
		    for(int i=0;i<entry.getValue().size();i++){  
			    System.out.println(entry.getValue().get(i).getContent());  
			} 
		} 
		System.out.println("==========prev method==========");  
		for(Map.Entry<String, Method> entry : prevDeletedMethods.entrySet()){  
		    System.out.println("Key = \n"+entry.getKey());  
		    System.out.println("Value = \n"+entry.getValue().getContent());  
		} 
		System.out.println("==========curr method==========");  
		for(Map.Entry<String, Method> entry : currInsertedMethods.entrySet()){  
		    System.out.println("Key = \n"+entry.getKey());  
		    System.out.println("Value = \n"+entry.getValue().getContent());  
		}  
	}

	
	public void fileDiff(String prevFile, String currFile) {
		JavaParser javaParser1=new JavaParser(),javaParser2=new JavaParser();
		javaParser1.parseFile(javaParser1.readFile(prevFile));
		javaParser2.parseFile(javaParser2.readFile(currFile));
		for(Map.Entry<String, String> entry : javaParser1.getPackages().entrySet()){  
		    if(javaParser2.getPackages().containsKey(entry.getKey())) {
		    	this.samePackages.put(entry.getKey(), entry.getValue());
		    	javaParser2.getPackages().remove(entry.getKey());
		    }
		    else {
		    	this.prevPackages.put(entry.getKey(), entry.getValue());
		    }
		}
		this.currPackages=javaParser2.getPackages();		
		for(Map.Entry<String, String> entry : javaParser1.getImports().entrySet()){  
		    if(javaParser2.getImports().containsKey(entry.getKey())) {
		    	this.sameImports.put(entry.getKey(), entry.getValue());
		    	javaParser2.getImports().remove(entry.getKey());
		    }
		    else {
		    	this.prevImports.put(entry.getKey(), entry.getValue());
		    }
		}
		this.currImports=javaParser2.getImports();
		
		for(Map.Entry<String, Method> entry : javaParser1.getMethods().entrySet()){  
		    if(javaParser2.getMethods().containsKey(entry.getKey())) {
		    	Method prevMethod = entry.getValue();
		    	Method currMethod = javaParser2.getMethods().get(entry.getKey());
		    	if(prevMethod.getContent().replace(" ", "").equals(currMethod.getContent().replace(" ", ""))) {
		    		this.sameMethods.put(entry.getKey(), entry.getValue());
		    	}
		    	else {
		    		List<Method> differentMethods = new ArrayList<Method>();
		    		differentMethods.add(prevMethod);
		    		differentMethods.add(currMethod);
		    		this.differentMethodsMap.put(entry.getKey(), differentMethods);
		    	}
		    	
		    	javaParser2.getMethods().remove(entry.getKey());
		    }
		    else {
		    	this.prevDeletedMethods.put(entry.getKey(), entry.getValue());
		    }
		}
		this.currInsertedMethods=javaParser2.getMethods();
	}

}

