package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSynchronized {
	
	public static ClusteredActionBean matchSynchronized(MiningActionData fp, Action a, String nodeType){

		String operationEntity = "SYNCHRONIZED";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}


}
