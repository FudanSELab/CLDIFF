package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSimpleNameOrLiteral {
	private static HighLevelOperationBean operationBean;
	/**
	 * level IV fafafather 为VariableDeclarationStatement ExpressionStatement
	 * father为methodinvocation  按照fafafatherNode 为key，存所有相关的action,更细的情况放到map之后再做处理
	 * 
	 * @param fp
	 * @param a
	 * @param nodeType
	 * @return
	 */
	public static HighLevelOperationBean matchExpressionStatementAndVariableDeclarationStatement(FindPattern fp,Action a, String nodeType,ITree fafafatherNode,String ffFatherNodeType) {
		String operationEntity  = "IFPREDICATE";
		ITree srcParent = null;
		List<Action> allActions = new ArrayList<Action>();

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
		boolean src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
		boolean dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
		int status = MyTreeUtil.isSrcorDstAdded(src_status,dst_status);

		fp.setActionTraversedMap(allActions);
//		this.mMiningActionBean.mapMethodInvocationAndActions(srcParent, allActions);
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,allActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}

	/**
	 * Sub sub level-III 访问到simple name节点或者literal 节点分两种情况，一种是father
	 * 为ifStatement 另一种是father不是ifstatement type 为simple name action可以为update
	 * 可以为insert match 之后标记action为已读 ， 目前思路： 按照parent为key存储map，insert update
	 * delete 遍历操作之后再进行判断 放入新的list再一起处理
	 * 
	 * 针对下面的switch 情况，因为修改不止一个action，而且可能有删有减有update有move 所以需要按照parent为key
	 * ，存储action，到最后再做操作。
	 * 
	 * @param a
	 * @return
	 */
	public static int matchSimplenameOrLiteral(FindPattern fp,Action a, String nodeType,TreeContext curContext) {
		// if predicate
		ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode(), curContext);
		String ffFatherNodeType = curContext.getTypeLabel(fafafatherNode);
		int returnVal = -1;
		switch (ffFatherNodeType) {
		case StatementConstants.IFSTATEMENT:
			System.out.println("If predicate");
			operationBean = MatchIfElse.matchIfPredicate(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			System.out.println(operationBean.toString());
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.FORSTATEMENT:
			System.out.println("For predicate");
			operationBean = MatchForStatement.matchForPredicate(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			System.out.println(operationBean.toString());
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
		case StatementConstants.EXPRESSIONSTATEMENT:
			System.out.println("variable/expression");
			operationBean = matchExpressionStatementAndVariableDeclarationStatement(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			System.out.println(operationBean.toString());
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.JAVADOC:
			operationBean = MatchJavaDoc.matchJavaDoc(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			System.out.println(operationBean.toString());
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		default:
			System.out.println("Default:" + ffFatherNodeType);
			break;
		}
		return returnVal;
	}

}
