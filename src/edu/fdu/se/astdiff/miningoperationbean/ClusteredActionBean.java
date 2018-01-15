package edu.fdu.se.astdiff.miningoperationbean;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.gumtree.MyTreeUtil;

/**
 * 记录find时候找到的节点，以及对应的fafafather 节点，以及该节点下，所有的action
 * @author huangkaifeng
 *
 */
public class ClusteredActionBean {

	public Action curAction;
	public List<Action> actions;


	public ITree curNode;
	public String curNodeType;
	//insert delete change
	public int operationType;
	// if 
	public String operationEntity;

	public ITree fafafatherNode;
	public String fatherNodeType;
	public ClusteredActionBean(Action curAction, String curNodeType, List<Action> actions, int operationType, String operationEntity, ITree fafafatherNode, String fatherNodeType){
		this.curAction = curAction;
		this.curNode = curAction.getNode();
		this.curNodeType = curNodeType;
		this.actions = actions;
		this.operationType = operationType;
		this.operationEntity = operationEntity;
		this.fafafatherNode = fafafatherNode;
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
		return fafafatherNode;
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
		String operationTypeString = OperationTypeConstants.getKeyNameByValue(type);
		String result = "[Pattern] "+ operationTypeString + " " + getOperationEntity()+", [curNodeType] "+curNodeType;
		if(!"".equals(fatherNodeType)) {
			result += ", [fafafatherNodeType] " + fatherNodeType;
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}