package edu.fdu.se.base.generatingactions;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;

public class ActionConstants {
	
	final public static String DELETE = Delete.class.getSimpleName();
	final public static String INSERT = Insert.class.getSimpleName();
	final public static String UPDATE = Update.class.getSimpleName();
	final public static String MOVE = Move.class.getSimpleName();
	
	final public static String NULLACTION = "NULLACTION";
	


}
