package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchForWhile {
	
	/**
	 * level IV For EnchancedFor
	 * 
	 * @param a
	 * @param treeContext
	 * @return
	 */
	public static HighLevelOperationBean matchForPredicate(FindPattern fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity  = "FORPREDICATE";

		List<Action> allActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(fafafatherNode.getChild(0), allActions);
		fp.setActionTraversedMap(allActions);
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,allActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}

}
