package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.miningoperationbean.HighLevelOperationBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchExpressionStatement {
	/**
	 * level III
	 * 
	 * @param a
	 * @return
	 */
	public static HighLevelOperationBean matchExpression(FindPatternData fp, Action a, String nodeType, ITree fafafather, String fafafatherType) {
		String operationEntity = "EXPRESSIONSTATEMENT";
		TreeContext con = null;
		if (a instanceof Insert) {
			con = fp.getDstTree();
		} else{
			con = fp.getSrcTree();
		}
		//String summary = "[PATTERN]";
		//summary += ActionConstants.getInstanceStringName(a);
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);
//		if(status == MyTreeUtil.TYPE1){
//			summary += "insert";
//		}else if(status == MyTreeUtil.TYPE2){
//			summary += "delete";
//		}
//		if (AstRelations.isClassCreation(subActions, con)) {
//			summary += " object Initializing - expression assignment";
//		} else {
//			summary += " expression assignment";
//		}


		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,subActions,status,operationEntity,fafafather,fafafatherType);
		return mHighLevelOperationBean;
	}


}
