package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

import edu.fdu.se.astdiff.miningoperationbean.HighLevelOperationBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSynchronized {
	
	public static HighLevelOperationBean matchSynchronized(MiningActionData fp, Action a, String nodeType){

		String operationEntity = "SYNCHRONIZED";
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);

		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}


}
