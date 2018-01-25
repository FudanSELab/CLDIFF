package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MatchSwitch {
	
	public static ClusteredActionBean matchSwitch(MiningActionData fp, Action a, String nodeType){
		String operationEntity = "SWITCHSTATEMENT";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchSwitchCase(MiningActionData fp, Action a, String nodeType){
		String operationEntity = "SWITCHCASE";

		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchSwitchCaseByFather(MiningActionData fp, Action a,ITree fafafatherNode){
		String operationEntity = "FATHER-SWITCHCASE-" +nodeType;

		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}
}
