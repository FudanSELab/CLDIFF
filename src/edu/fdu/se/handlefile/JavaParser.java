package edu.fdu.se.handlefile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JavaParser {

	private Map<String, String> packages = new HashMap<String, String>();

	private Map<String, String> imports = new HashMap<String, String>();

	private Map<String, String> fields = new HashMap<String, String>();
	
	private Map<String, Method> methods = new HashMap<String, Method>();

	private Map<String, String> blocks = new HashMap<String, String>();		
	
	public void print() {
		for(Map.Entry<String, Method> entry : methods.entrySet()){  
		    System.out.println("Key = \n"+entry.getKey());  
		    System.out.println("Value = \n"+entry.getValue().getContent());  
		} 
		for(Map.Entry<String, String> entry : packages.entrySet()){  
		    System.out.println("Key = \n"+entry.getKey());  
		    System.out.println(entry.getValue());  
		} 
		for(Map.Entry<String, String> entry : imports.entrySet()){  
		    System.out.println("Key = \n"+entry.getKey());  
		    System.out.println(entry.getValue());  
		} 
	}	

	public void parseFile(List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			// if(line.contains("class")||line.contains("interface")) {
			//
			// }
			if (line.indexOf("package ") == 0) {
				packages.put(line.trim(), line);
			} else if (line.indexOf("import ") == 0) {
				imports.put(line.trim(), line);
			} 
			else if (line.indexOf("class ") >= 0) {
			}
			else if (isMethodSignature(line)) {
				Method method = getWholeMethod(i, lines);
				i=method.getEnd()-1;
				methods.put(method.getSignature(), method);
			}
		}
	}

	public Method getWholeMethod(int startIndex, List<String> lines) {
		String line = lines.get(startIndex);
		int lineNumber =startIndex ,begin = startIndex + 1, end=-1;
		Stack<String> stack = new Stack<String>();
		String signature = "",content="";
		int index=-1;
		if((index = line.indexOf(";")) > 0) {
			end=startIndex + 1;
			signature=line.substring(0, index).trim();
			content=line+"\n";
		}
		else {
			content+=line+"\n";
			if ((index = line.indexOf("{")) > 0) {
				stack.push("{");
				signature = line.substring(0, index).trim();
				if (index != line.length() - 1) {
					line = line.substring( index + 1);
				}
				else {
					line = lines.get(++lineNumber);
					content+=line+"\n";
				}		
			}
			else {
				signature = line.trim();
				line = lines.get(++lineNumber);
				content+=line+"\n";
			}
			while(lineNumber<lines.size()) {				
				for(int i = 0; i < line.length(); i++) {  
		            char character=line.charAt(i);  
		            switch(character){
		            case '{':
		            	stack.push("{");
		            	break;
		            case '}':
		            	stack.pop();
		            	break;
		            }
		            if(stack.isEmpty()) {
		            	break;
		            }		            	
		        } 
				if(stack.isEmpty()) {
	            	break; 
	            }	
				line = lines.get(++lineNumber);
				content+=line+"\n";
			}	
			end = lineNumber+1;
		}
		return new Method(begin,end,signature,content);
	}

	public List<String> readFile(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> lines = new ArrayList<String>();
		String tmp = null;

		try {
			while ((tmp = reader.readLine()) != null) {
				lines.add(tmp);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * 构造函数、abstrat
	 * @param input
	 *            input string
	 * @return whether the input string is a method signature
	 */
	public boolean isMethodSignature(String input) {

		// the pattern for name, java generics and type
		String namePattern = "[_a-zA-Z]+\\w*";
		String genericsPattern = "(<[_a-zA-Z]+\\w*(, [_a-zA-Z]+\\w*)*>)?";
		String arrayTypePattern = "(\\[\\])*";
		String typePattern = namePattern + "("+genericsPattern+"|"+ arrayTypePattern+")";

		// the pattern for parameters
		String argPatternZero = "\\s*"; // no parameter
		String argPatternDynamic = "\\s*...\\s*"; // dynamic parameters
		String argPattern = "\\s*" + typePattern + "\\s+" + namePattern + "\\s*"; // one
																					// parameter
		String argPatternOne = argPattern + "(,\\s*...\\s*)?"; // one parameter
																// + dynamic
																// parameters
		String argPatternMulti = argPattern + "(," + argPattern + ")*(,\\s*...\\s*)?"; // multiple
																						// parameters
																						// +
																						// dynamic
																						// parameters
		String argsPattern = "(" + argPatternZero + "|" + argPatternDynamic + "|" + argPatternOne + "|"
				+ argPatternMulti + ")";


		String methodSignaturePattern = "(public|protected|private)(\\s+static)?(\\s+final)?\\s+" + typePattern + " "
				+ namePattern + "\\(" + argsPattern + "\\)\\s*";		
        
		if(input.contains("{")) {
			String signature = input.substring(0,input.indexOf("{"));
			return signature.trim().matches(methodSignaturePattern);
		}
		
		return input.trim().matches(methodSignaturePattern);
	}
	
	public Map<String, String> getPackages() {
		return packages;
	}

	public void setPackages(Map<String, String> packages) {
		this.packages = packages;
	}

	public Map<String, String> getImports() {
		return imports;
	}

	public void setImports(Map<String, String> imports) {
		this.imports = imports;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public Map<String, Method> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, Method> methods) {
		this.methods = methods;
	}

	public Map<String, String> getBlocks() {
		return blocks;
	}

	public void setBlocks(Map<String, String> blocks) {
		this.blocks = blocks;
	}	
}
