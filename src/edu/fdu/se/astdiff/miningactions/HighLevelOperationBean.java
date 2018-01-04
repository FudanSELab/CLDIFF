package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
/**
 * 记录find时候找到的节点，以及对应的fafafather 节点，以及该节点下，所有的action
 * @author huangkaifeng
 *
 */
public class HighLevelOperationBean {

	public Action curAction;
	public List<Action> actions;

	
	
	public ITree curNode;
	public String curNodeType;
	//insert delete change
	public int operationType;
	// if 
	public String operationEntity;

	public ITree fatherNode;
	public String fatherNodeType;
	public HighLevelOperationBean(Action curAction,String curNodeType,List<Action> actions,int operationType,String operationEntity,ITree fatherNode,String fatherNodeType){
		this.curAction = curAction;
		this.curNode = curAction.getNode();
		this.curNodeType = curNodeType;
		this.actions = actions;
		this.operationType = operationType;
		this.operationEntity = operationEntity;
		this.fatherNode = fatherNode;
		this.fatherNodeType = fatherNodeType;
	}
	
	public ITree getCurNode() {
		return curNode;
	}
	public List<Action> getActions() {
		return actions;
	}
	public int getOperationType() {
		return operationType;
	}
	public String getOperationEntity() {
		return operationEntity;
	}

	public String getCurNodeType() {
		return curNodeType;
	}

	public ITree getFatherNode() {
		return fatherNode;
	}

	public String getFatherNodeType() {
		return fatherNodeType;
	}

	public Action getCurAction() {
		return curAction;
	}
	
	@Override
	public String toString(){
		//TODO
		int type = getOperationType();
		String operationTypeString = "";
		switch (type){
			case 1:
				operationTypeString = "INSERT";
				break;
			case 2:
				operationTypeString = "DELETE";
				break;
			case 4:
				operationTypeString = "INSERT + MOVE";
				break;
			case 5:
				operationTypeString = "DELETE + MOVE";
				break;
			case 0:
				operationTypeString = "FAFATHERNODE";
				break;
			case -1:
			default:
				operationTypeString = "UNKNOWN";
				break;
		}
		//System.out.print();
		String result = "[Pattern] "+ operationTypeString + " " + getOperationEntity();
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}