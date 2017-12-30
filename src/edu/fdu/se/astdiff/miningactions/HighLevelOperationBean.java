package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

public class HighLevelOperationBean {
	public ITree curNode;
	public String curNodeType;
	public List<Action> actions;
	//insert delete
	public String operationType;
	public ITree fatherNode;
	public String fatherNodeType;
	public HighLevelOperationBean(ITree curNode,String curNodeType,List<Action> actions,String operationType,ITree fatherNode,String fatherNodeType){
		this.curNode = curNode;
		this.curNodeType = curNodeType;
		this.actions = actions;
		this.operationType = operationType;
		this.fatherNode = fatherNode;
		this.fatherNodeType = fatherNodeType;
	}
}