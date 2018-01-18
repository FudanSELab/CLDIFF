package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSwitch {
	
	public static ClusteredActionBean matchSwitch(MiningActionData fp, Action a, String nodeType){
		String operationEntity = "SWITCHSTATEMENT";
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchSwitchCase(MiningActionData fp, Action a, String nodeType){
		String operationEntity = "SWITCHCASE";

		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchSwitchCaseByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType){
		String operationEntity = "FATHER-SWITCHCASE-" +nodeType;

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
		Set<String> src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, subActions);
		Set<String> dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, subActions);

		int status = MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
		fp.setActionTraversedMap(subActions);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}
}
