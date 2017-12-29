package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;

public class HighLevelOperationBean {
	
	public Tree statementNode;
	
	public List<Action> actions;

	//insert delete
	public String operationType;
	
	
	public Tree fatherStatement;
	

}
