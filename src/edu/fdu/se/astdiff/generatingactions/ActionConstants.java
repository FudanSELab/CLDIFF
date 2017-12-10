package edu.fdu.se.astdiff.generatingactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;

public class ActionConstants {
	
	final public static String DELETE = "DELETE";
	final public static String INSERT = "INSERT";
	final public static String UPDATE = "UPDATE";
	final public static String MOVE = "MOVE";
	
	final public static String METHODINVOCATION = "MethodInvocation";
	// fafafather node 
	//TODO 应该还有其他情况
	final public static String IfPredicate = "IfStatement";
	final public static String ForPredicate = "ForStatement";
	final public static String ExpressionStatement = "ExpressionStatement";
	final public static String VariableDelcarationStatement = "VariableDeclarationStatement";
	
	
	public static String getInstanceStringName(Action a){
		if(a instanceof Insert){
			return ActionConstants.INSERT;
		}else if( a instanceof Update){
			return ActionConstants.UPDATE;
		}else if( a instanceof Delete){
			return ActionConstants.DELETE;
		}else if( a instanceof Move){
			return ActionConstants.MOVE;
			
		}else{
			return null;
		}
	}

}
