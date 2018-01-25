package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

public class MatchExpressionStatement {


	public static void matchExpression(MiningActionData fp, Action a, String nodeType) {
		ChangePacket changePacket = new ChangePacket();
		MatchUtil.setChangePacketOperationType(a,changePacket);
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT);
		String operationEntity = "EXPRESSIONSTATEMENT";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		TraverseTree.traverseNodeUpDown(fp,a,changePacket);
		return mHighLevelOperationBean;
	}
	public static ClusteredActionBean matchExpressionByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity = "FATHER-EXPRESSIONSTATEMENT";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}


}
