package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchTry {
	
	/**
	 * level III
	 * 
	 * @param a
	 * @return
	 */
	public static HighLevelOperationBean matchTry(FindPattern fp,Action a,String nodeType) {
		String operationEntity = "TRYSTATEMENT";
		List<Action> tryAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, tryAction);

		fp.setActionTraversedMap(tryAction);

		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,tryAction,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static HighLevelOperationBean matchThrowStatement(FindPattern fp,Action a,String nodeType,ITree ffFatherNode,String fatherNodeType) {
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
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,throwAction,status,operationEntity,ffFatherNode,fatherNodeType);
		return mHighLevelOperationBean;
	}

	public static HighLevelOperationBean matchFinally(FindPattern fp,Action a,String nodeType,ITree ffFatherNode,String fatherNodeType) {
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
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,finallyAction,status,operationEntity,ffFatherNode,fatherNodeType);
		return mHighLevelOperationBean;
	}

	public int matchCatchclause(FindPattern fp,Action a) {
		String summary = "[PATTERN] " + ActionConstants.getInstanceStringName(a);
		List<Action> tryAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, tryAction);
		Insert ins = (Insert) a;
		ITree insNode = ins.getNode();
		ITree fafafatherCatchClause = AstRelations.findFafafatherNodeByStatementType(insNode, fp.getDstTree(),StatementConstants.CATCHCLAUSE);
		String fatherCatchClauseType = fp.getDstTreeContextTypeLabel(fafafatherCatchClause);
		
		if (status == MyTreeUtil.TYPE1) {
			summary += " try catch clause and body";
		} else{
			summary += " try catch clause wrapper";
		}
		fp.setActionTraversedMap(tryAction);
		System.out.println(summary);
		return tryAction.size();
	}

}
