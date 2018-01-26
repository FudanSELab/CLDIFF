package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.MatchTry;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;
import edu.fdu.se.astdiff.miningactions.util.TraverseTree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	/**
	 * 方法签名改变的情况
	 *
	 * @param a
	 * @param fafafather
	 * @return
	 */
	public static ClusteredActionBean matchMethodSignatureChange(MiningActionData fp, Action a, String nodeType, ITree fafafather, String fafafatherType) {
		String operationEntity = "METHODSIGNATURE";
		if(!StatementConstants.METHODDECLARATION.equals(fafafatherType)) {
			System.err.println(operationEntity+" CHANGE: "+"fafafatherType is not MethodDeclaration" );
			return null;
		}
		ITree srcfafafather = null;
		ITree dstfafafather = null;
		if (a instanceof Insert) {
			dstfafafather = fafafather;
			srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
			if (srcfafafather == null) {
				System.err.println("err null mapping");
			}
		} else {
			srcfafafather = fafafather;
			dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
			if (dstfafafather == null) {
				System.err.println("err null mapping");
			}
		}
		List<Action> signatureChidlren = new ArrayList<>();
		Set<String> src_status = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, srcfafafather, signatureChidlren);
		Set<String> dst_status = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a, dstfafafather, signatureChidlren);

		int status = MatchTry.MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
		fp.setActionTraversedMap(signatureChidlren);
		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(
				a,nodeType,signatureChidlren,nodeLinePosition,status,operationEntity,null,null);
		return mBean;
	}

}
