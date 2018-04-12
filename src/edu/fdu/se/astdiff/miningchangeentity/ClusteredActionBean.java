package edu.fdu.se.astdiff.miningchangeentity;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;

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

	public ChangePacket changePacket;

	public Tree fafather;

	public int nodeType;



	public ClusteredActionBean(int traverseType,Action curAction, List<Action> actions,ChangePacket changePacket){
		this.traverseType = traverseType;
		this.curAction = curAction;
		this.actions = actions;
		this.changePacket = changePacket;
		this.fafather = (Tree) curAction.getNode();
		if(curAction instanceof Insert){
			this.nodeType = ChangeEntityDesc.StageITreeType.DST_TREE_NODE;
		}else{
			this.nodeType = ChangeEntityDesc.StageITreeType.SRC_TREE_NODE;
		}
	}
	public ClusteredActionBean(int traverseType,Action curAction, List<Action> actions, ChangePacket changePacket,Tree fafather,int nodeType){
		this.traverseType = traverseType;
		this.curAction = curAction;
		this.actions = actions;
		this.changePacket = changePacket;
		this.fafather = fafather;
		this.nodeType = nodeType;
	}


	@Override
	public String toString(){
		return this.curAction.getClass().getSimpleName()+" "+this.actions.size();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}