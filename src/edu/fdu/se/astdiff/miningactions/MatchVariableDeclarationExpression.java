package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.miningoperationbean.HighLevelOperationBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchVariableDeclarationExpression {
	/**
	 * level III VARIABLEDECLARATIONSTATEMENT
	 * 
	 * @param a
	 * @return
	 */
	public static HighLevelOperationBean matchVariableDeclaration(MiningActionData fp, Action a, String nodeType) {
		TreeContext con = null;
		if (a instanceof Insert) {
			con = fp.getDstTree();
		} else if (a instanceof Delete) {
			con = fp.getSrcTree();
		}
		String operationEntity = "VARIABLEDECLARATION";
//		String summary = "[PATTERN]";
//		summary += ActionConstants.getInstanceStringName(a);
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);
		if (AstRelations.isClassCreation(subActions, con)) {
			operationEntity += " OBJECT INITIALIZING";
		}

		ITree fatherNode;
		fatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(),con);
		String fatherNodeType = fatherNode.getLabel();

		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,subActions,status,operationEntity,fatherNode,fatherNodeType);
		return mHighLevelOperationBean;
	}
}
