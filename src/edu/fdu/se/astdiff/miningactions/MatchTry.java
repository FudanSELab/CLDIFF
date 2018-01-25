package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;

import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

public class MatchTry {
	
	/**
	 * level III
	 * 
	 * @param a
	 * @return
	 */
	public static ClusteredActionBean matchTry(MiningActionData fp, Action a, String nodeType) {
		String operationEntity = "TRYSTATEMENT";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchThrowStatement(MiningActionData fp, Action a, String nodeType, ITree ffFatherNode, String fatherNodeType) {
		String operationEntity = "THROWSTATEMENT";
		List<Action> throwAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, throwAction);
		fp.setActionTraversedMap(throwAction);
		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,throwAction,nodeLinePosition,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchFinally(MiningActionData fp, Action a, String nodeType, ITree ffFatherNode, String fatherNodeType) {
		String operationEntity = "FINALLY";
		List<Action> finallyAction = new ArrayList<>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, finallyAction);
		fp.setActionTraversedMap(finallyAction);
		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,finallyAction,nodeLinePosition,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}



}
