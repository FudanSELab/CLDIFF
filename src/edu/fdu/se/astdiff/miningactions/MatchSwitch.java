package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;

import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSwitch {
	
	public static HighLevelOperationBean matchSwitch(FindPattern fp,Action a,String nodeType){
		String operationEntity = "SWITCHSTATEMENT";
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);
//		if (status == MyTreeUtil.TYPE1) {
//			summary += " and body";
//		} else {
//			if (a instanceof Insert) {
//				summary += " wrapper[insert]";
//			} else if (a instanceof Delete) {
//				summary += " wrapper[delete]";
//			}
//		}

		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static HighLevelOperationBean matchSwitchCase(FindPattern fp, Action a, String nodeType){
		String operationEntity = "SWITCHCASE";
		ITree father = a.getNode().getParent();

		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);
//		if (status == MyTreeUtil.TYPE1) {
//			summary += " and body";
//		} else {
//			if (a instanceof Insert) {
//				summary += " wrapper[insert]";
//			} else if (a instanceof Delete) {
//				summary += " wrapper[delete]";
//			}
//		}

		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}
}
