package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

import edu.fdu.se.astdiff.miningactions.util.MatchUtil;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.TraverseTree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

public class MatchExpressionStatement {


	public static void matchExpression(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		MatchUtil.setChangePacket(a,changePacket);
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT);
		changePacket.setStatementType(OperationTypeConstants.STATEMENT_DETIAL_EXPRESSION);
		ClusteredActionBean mHighLevelOperationBean = TraverseTree.traverseNodeUpDown(fp,a,changePacket);
		TraverseTree.traverseNodeUpDown(fp,a,changePacket);
		return mHighLevelOperationBean;
	}
	public static void matchExpressionByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity = "FATHER-EXPRESSIONSTATEMENT";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}


}
