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
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}
	public static ClusteredActionBean matchExpressionByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity = "FATHER-EXPRESSIONSTATEMENT";
		List<Action> subActions = new ArrayList<Action>();

		ITree srcfafafather = null;
		ITree dstfafafather = null;
		if (a instanceof Insert) {
			dstfafafather = fafafatherNode;
			srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
			if (srcfafafather == null) {
				System.err.println("err null mapping");
			}
		} else {
			srcfafafather = fafafatherNode;
			dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
			if (dstfafafather == null) {
				System.err.println("err null mapping");
			}
		}

		Set<String> srcT = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, subActions);
		Set<String> dstT = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, subActions);
		int status = MyTreeUtil.isSrcOrDstAdded(srcT,dstT);

		fp.setActionTraversedMap(subActions);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}


}
