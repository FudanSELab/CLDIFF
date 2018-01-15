package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchTry {
	
	/**
	 * level III
	 * 
	 * @param a
	 * @return
	 */
	public static ClusteredActionBean matchTry(MiningActionData fp, Action a, String nodeType) {
		String operationEntity = "TRYSTATEMENT";
		List<Action> tryAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, tryAction);

		fp.setActionTraversedMap(tryAction);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,tryAction,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchThrowStatement(MiningActionData fp, Action a, String nodeType, ITree ffFatherNode, String fatherNodeType) {
		String operationEntity = "THROWSTATEMENT";
		List<Action> throwAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, throwAction);
		fp.setActionTraversedMap(throwAction);
//		ITree fatherNode;
//		if(a instanceof Insert)
//			fatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(),fp.getDstTree());
//		else
//			fatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(),fp.getSrcTree());
//		String fatherNodeType = fatherNode.getLabel();
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,throwAction,status,operationEntity,ffFatherNode,fatherNodeType);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchFinally(MiningActionData fp, Action a, String nodeType, ITree ffFatherNode, String fatherNodeType) {
		String operationEntity = "FINALLY";
		List<Action> finallyAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, finallyAction);
		fp.setActionTraversedMap(finallyAction);
//		ITree fatherNode;
//		if(a instanceof Insert)
//			fatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(),fp.getDstTree());
//		else
//			fatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(),fp.getSrcTree());
//		String fatherNodeType = fatherNode.getLabel();
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,finallyAction,status,operationEntity,ffFatherNode,fatherNodeType);
		return mHighLevelOperationBean;
	}


}
