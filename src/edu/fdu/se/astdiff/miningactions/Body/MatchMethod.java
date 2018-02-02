package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.MatchTry;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchMethod {
	

	public static void matchMethdDeclaration(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
		DefaultUpDownTraversal.traverseClass(a,subActions,changePacket);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
		fp.addOneChangeEntity(code);
	}


	public static void matchMethodSignatureChangeNewEntity(MiningActionData fp, Action a, ITree fafather,List<Action> sameEditActions) {
		ChangePacket changePacket = new ChangePacket();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
		fp.setActionTraversedMap(sameEditActions);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEditActions,changePacket,range,(Tree)fafather);
		ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
		fp.addOneChangeEntity(code);
	}

//	public static ClusteredActionBean matchConstructorInvocation(MiningActionData fp, Action a) {
//		ChangePacket changePacket = new ChangePacket();
//		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CONSTRUCTOR);
//		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
//		if(a instanceof Insert){
//			changePacket.setOperationType(OperationTypeConstants.INSERT);
//		}else if(a instanceof Delete){
//			changePacket.setOperationType(OperationTypeConstants.DELETE);
//		}
//		ClusteredActionBean mBean = TraverseTree.traverseNodeUpDown(fp,a,changePacket);
//		return mBean;
//	}

//	public static ClusteredActionBean matchConstructorInvocationByFather(MiningActionData fp, Action a,ITree fafafatherNode, String ffFatherNodeType) {
//		ChangePacket changePacket = new ChangePacket();
//		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CONSTRUCTOR);
//		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
//		if(a instanceof Insert){
//			changePacket.setOperationType(OperationTypeConstants.INSERT);
//		}else if(a instanceof Delete){
//			changePacket.setOperationType(OperationTypeConstants.DELETE);
//		} else if(a instanceof Move){
//			changePacket.setOperationType(OperationTypeConstants.MOVE);
//		}
//		ClusteredActionBean mBean = TraverseTree.traverseNodeUpdownNodePair(fp,a);
//		return mBean;
//		String operationEntity = "FATHER-CONSTRUCTORINVOCATION-THIS()";
//		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
//		return mHighLevelOperationBean;
//	}
//
//	public static ClusteredActionBean matchSuperConstructorInvocation(MiningActionData fp, Action a, String nodeType) {
//		String operationEntity = "SUPERCONSTRUCTORINVOCATION-SUPER()";
//		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
//		return mHighLevelOperationBean;
//	}
//
//	public static ClusteredActionBean matchSuperConstructorInvocationByFather(MiningActionData fp, Action a, String nodeType,ITree fafafatherNode, String ffFatherNodeType) {
//		String operationEntity = "FATHER-SUPERCONSTRUCTORINVOCATION-SUPER()";
//		ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
//		return mHighLevelOperationBean;
//	}



}
