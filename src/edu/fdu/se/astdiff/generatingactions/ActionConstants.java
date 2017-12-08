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
