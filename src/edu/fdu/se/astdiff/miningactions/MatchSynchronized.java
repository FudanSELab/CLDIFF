package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSynchronized {
	
	public static HighLevelOperationBean matchSynchronized(FindPattern fp,Action a,String nodeType){

		String operationEntity = "SYNCHRONIZED";
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);

		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,subActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}


}
