package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchExpressionStatement {
	/**
	 * level III
	 * 
	 * @param a
	 * @return
	 */
	public static ClusteredActionBean matchExpression(MiningActionData fp, Action a, String nodeType) {
		String operationEntity = "EXPRESSIONSTATEMENT";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}
	public static ClusteredActionBean matchExpressionByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity = "FATHER-EXPRESSIONSTATEMENT";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}


}
