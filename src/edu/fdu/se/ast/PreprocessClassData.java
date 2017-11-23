package edu.fdu.se.ast;

import java.util.List;
import java.util.Map;

public class PreprocessClassData {
	
	public Map<String,String> sameMethod;
	
	public Map<String,String> sameField;
	
	/**
	 *  same method name, key:method signature,value:list[0]=prev list[1]=curr
	 */
	public Map<String,List<String>> differentMethodsMap;
	/**
	 * different method name
	 */
	public Map<String,String> prevDeletedMethods;
	public Map<String,String> currInsertedMethods;
	
	/**
	 * field
	 */
	public Map<String,String> prevDeletedFields;
	
	public Map<String,String> currInsertedFields;
	

}
