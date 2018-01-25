package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

public class MatchMethod {
	

	public static ClusteredActionBean matchNewOrDeleteMethod(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_METHOD);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
		if(a instanceof Insert){
			changePacket.setOperationType(OperationTypeConstants.INSERT);
		}else if(a instanceof Delete){
			changePacket.setOperationType(OperationTypeConstants.DELETE);
		}
		ClusteredActionBean mBean = TraverseTree.traverseNodeUpDown(fp,a,changePacket);
		return mBean;
	}

	public static ClusteredActionBean matchConstructorInvocation(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CONSTRUCTOR);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
		if(a instanceof Insert){
			changePacket.setOperationType(OperationTypeConstants.INSERT);
		}else if(a instanceof Delete){
			changePacket.setOperationType(OperationTypeConstants.DELETE);
		}
		ClusteredActionBean mBean = TraverseTree.traverseNodeUpDown(fp,a,changePacket);
		return mBean;
	}

	public static ClusteredActionBean matchConstructorInvocationByFather(MiningActionData fp, Action a,ITree fafafatherNode, String ffFatherNodeType) {
		ChangePacket changePacket = new ChangePacket();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CONSTRUCTOR);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
		if(a instanceof Insert){
			changePacket.setOperationType(OperationTypeConstants.INSERT);
		}else if(a instanceof Delete){
			changePacket.setOperationType(OperationTypeConstants.DELETE);
		} else if(a instanceof Move){
			changePacket.setOperationType(OperationTypeConstants.MOVE);
		}
		ClusteredActionBean mBean = TraverseTree.traverseNodeUpdownNodePair(fp,a);
		return mBean;
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
