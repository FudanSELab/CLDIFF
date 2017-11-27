package edu.fdu.se.handlefile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreprocessClassData {
	public Map<String, String> sameMethod;

	public Map<String, String> sameField;

	public Map<String, String> sameBlock;
	
	public Map<String, String> samePackage;
	
	public Map<String, String> sameImport;

	/**
	 * same method name, key:method signature,value:list[0]=prev list[1]=curr
	 */
	public Map<String, List<String>> differentMethodsMap;
	/**
	 * different method name
	 */
	public Map<String, String> prevDeletedMethods;
	public Map<String, String> currInsertedMethods;

	public Map<String, String> prevBlocks;
	public Map<String, String> currBlocks;
	
    public Map<String, String> prevPackage;
    public Map<String, String> currPackage;
	
	public Map<String, String> prevImport;
	public Map<String, String> currImport;
	

	/**
	 * field
	 */
	public Map<String, String> prevDeletedFields;

	public Map<String, String> currInsertedFields;

	public static void main(String[] args) {
		PreprocessClassData preprocessClassData = new PreprocessClassData();
		preprocessClassData.isMethodSignature("");
	}

	public void fileDiff(String prevFile, String currFile) {
		// 根据对应的编码格式读取
		BufferedReader prevReader = null, currReader = null;
		try {
			prevReader = new BufferedReader(new InputStreamReader(new FileInputStream(prevFile)));
			currReader = new BufferedReader(new InputStreamReader(new FileInputStream(currFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> prevLines = new ArrayList<String>(), currLines = new ArrayList<String>();
		String tmp = null;

		try {
			while ((tmp = prevReader.readLine()) != null) {
				prevLines.add(tmp);
			}
			prevReader.close();
			while ((tmp = currReader.readLine()) != null) {
				currLines.add(tmp);
			}
			currReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void parseFile(List<String> prevLines,List<String> currLines) {
		for(int i=0;i<prevLines.size();i++) {
			String line = prevLines.get(i);
			if(line.contains("class")||line.contains("interface")) {
				
			}
			else if(line.contains("package")) {
				prevPackage.put(line, line);
			}
			else if(line.contains("import")) {
				prevImport.put(line, line);
			}
			else if(isMethodSignature(line)) {
				
			}
			else if()
		}
	}

	public int lineIndexInFile(List<String> fileLines, String line, int startIndex) {
		int index = -1;
		for (int i = startIndex; i < fileLines.size(); i++) {
			if (fileLines.get(i).trim().equals(line.trim())) {
				return i;
			}

		}
		return index;
	}

	/**
	 * 
	 * @param input
	 *            input string
	 * @return whether the input string is a method signature
	 */
	public boolean isMethodSignature(String input) {

		String m = "public List<int> foo(Type name, ...)";

		// the pattern for name, java generics and type
		String namePattern = "[_a-zA-Z]+\\w*";
		String genericsPattern = "(<[_a-zA-Z]+\\w*(, [_a-zA-Z]+\\w*)*>)?";
		String typePattern = namePattern + genericsPattern;

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

		// the pattern for method signature
		// String methodSignaturePattern =
		// "((public|protected|private)(\\s+static)?(\\s+final)?\\s+)|((\\s*static)(\\s+final)?\\s+)|((\\s*final)?\\s+)"
		// + typePattern + " "
		// + namePattern + "\\(" + argsPattern + "\\)";

		String methodSignaturePattern = "(public|protected|private)(\\s+static)?(\\s+final)?\\s+" + typePattern + " "
				+ namePattern + "\\(" + argsPattern + "\\)";

		// System.out.println("List<int, Object>".matches(typePattern));
		// System.out.println("".matches(argPatternZero));
		// System.out.println("...".matches(argPatternDynamic));
		// System.out.println(argPatternOne);
		// System.out.println("int age".matches(argPatternOne));
		// System.out.println(argPatternMulti);
		// System.out.println("int age, List<Integer>
		// values".matches(argPatternMulti));
		// System.out.println("int age, List<Integer> values,
		// ...".matches(argsPattern));

		// 测试是否一个方法的签名
		// 暂时规则，单词间只用一个空格分隔，因为实现情况要考虑回车，多个空格，tab键等，把空格在正则中换成\\s+
		// 例如要在','前后加\\s*，函数签名中的'(', ')'前后加\\s*
		// 这里为了不使正则变得太难懂，直接只用了一个空格.
		System.out.println(methodSignaturePattern);
		// System.out.println("void foo(...)".matches(methodSignaturePattern));
		System.out.println("public void foo(...)".matches(methodSignaturePattern));
		System.out.println(
				"public List<Integer> foo(String name, Set<Double> values, ...)".matches(methodSignaturePattern));
		return false;
	}
}
