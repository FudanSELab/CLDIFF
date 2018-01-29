package edu.fdu.se.astdiff.miningoperationbean;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;

/**
 * 记录find时候找到的节点，以及对应的fafafather 节点，以及该节点下，所有的action
 * @author huangkaifeng
 *
 */
public class ClusteredActionBean {

	/**
	 * 1 up-down   2 down-up
	 */
	public int traverseType;

	public Action curAction;
	public List<Action> actions;

	/**
	 * operation type / entity / sub entity
	 */
	public ChangePacket changePacket;


	public static final int TRAVERSE_UP_DOWN = 1;
	public static final int TRAVERSE_DOWN_UP = 2;

	public Range nodeLinePosition;


	public Tree fafather;


	public ClusteredActionBean(int traverseType,Action curAction, List<Action> actions,ChangePacket changePacket,Range nodeLine){
		this.traverseType = traverseType;
		this.curAction = curAction;
		this.actions = actions;
		this.changePacket = changePacket;
		this.nodeLinePosition = nodeLine;
		this.fafather = (Tree) curAction.getNode();
	}
	public ClusteredActionBean(int traverseType,Action curAction, List<Action> actions, ChangePacket changePacket,Range nodeLine,Tree fafather){
		this.traverseType = traverseType;
		this.curAction = curAction;
		this.actions = actions;
		this.changePacket = changePacket;
		this.nodeLinePosition = nodeLine;
		this.fafather = fafather;
	}
	public Range getNodeLinePosition() {
		return nodeLinePosition;
	}
	public String getNodePositionAsString(){
		return this.nodeLinePosition.toString();
	}

	public void setNodeLinePosition(Range nodeLinePosition) {
		this.nodeLinePosition = nodeLinePosition;
	}

	public List<Action> getActions() {
		return actions;
	}

	public ITree getFatherNode() {
		return fafather;
	}


	public Action getCurAction() {
		return curAction;
	}




	public int getTraverseType() {
		return traverseType;
	}

	@Override
	public String toString(){
		//TODO
//		String operationTypeString = OperationTypeConstants.getKeyNameByValue(type);
//		String result = "[Pattern] "+ operationTypeString + " " + getOperationEntity()+", [curNodeType] ";
//		if(!"".equals(fatherNodeType)) {
//			result += ", [fafafatherNodeType] " + fatherNodeType;
//		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}