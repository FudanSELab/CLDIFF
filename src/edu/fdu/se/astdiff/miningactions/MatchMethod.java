package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchMethod {
	
	/**
	 * level III insert 操作中的新增方法 ok
	 * 
	 * 
	 * @param a
	 * @return
	 */
	public static ClusteredActionBean matchNewOrDeleteMethod(MiningActionData fp, Action a, String nodeType) {
		String operationEntity = "NEWORDELETEMETHOD";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchConstructorInvocation(MiningActionData fp, Action a, String nodeType) {
		String operationEntity = "CONSTRUCTORINVOCATION-THIS()";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchConstructorInvocationByFather(MiningActionData fp, Action a, String nodeType,ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity = "FATHER-CONSTRUCTORINVOCATION-THIS()";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchSuperConstructorInvocation(MiningActionData fp, Action a, String nodeType) {
		String operationEntity = "SUPERCONSTRUCTORINVOCATION-SUPER()";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
		return mHighLevelOperationBean;
	}

	public static ClusteredActionBean matchSuperConstructorInvocationByFather(MiningActionData fp, Action a, String nodeType,ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity = "FATHER-SUPERCONSTRUCTORINVOCATION-SUPER()";
		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}

}
