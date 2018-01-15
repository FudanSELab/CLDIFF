package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSwitch {
	
	public static ClusteredActionBean matchSwitch(MiningActionData fp, Action a, String nodeType){
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

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchSwitchCase(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType){
		String operationEntity = "SWITCHCASE-" +nodeType;
//		String fatherType;
//		if (a instanceof Insert) {
//			fatherType = fp.getDstTreeContextTypeLabel(a.getNode().getParent());
//		} else{
//			fatherType = fp.getSrcTreeContextTypeLabel(a.getNode().getParent());
//		}
//
//        if(!"".equals(fatherType) && fatherType.equals(ffFatherNodeType))
//			return null;

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

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}
}
