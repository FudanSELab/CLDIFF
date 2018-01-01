package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSimpleNameOrLiteral {
	
	/**
	 * level IV fafafather 为VariableDeclarationStatement ExpressionStatement
	 * father为methodinvocation  按照fafafatherNode 为key，存所有相关的action,更细的情况放到map之后再做处理
	 * 
	 * @param a
	 * @param treeContext
	 * @return
	 */
	public static int matchExpressionStatementAndVariableDeclarationStatement(FindPattern fp,Action a, TreeContext treeContext,
			ITree fafafatherNode) {
		ITree srcParent = null;
		List<Action> allActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(fafafatherNode, allActions);
		// 如果为insert ，那么src树可能有别的标记
		// 如果不是
		if(a instanceof Insert){
			ITree tmp = fp.getMappedSrcOfDstNode(fafafatherNode);
			if(tmp!=null){
				srcParent = tmp;
				List<Action> tmpList = new ArrayList<Action>();
				int status2 = MyTreeUtil.traverseNodeGetAllEditActions(srcParent, tmpList);
				allActions.addAll(tmpList);
			}else{
				System.err.println("ERERERER");
			}
		}else{
			srcParent = fafafatherNode;
		}
		fp.setActionTraversedMap(allActions);
//		this.mMiningActionBean.mapMethodInvocationAndActions(srcParent, allActions);
		return 0;
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
	public static int matchSimplenameOrLiteral(FindPattern fp,Action a, TreeContext curContext) {
		// if predicate
		ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode(), curContext);
		String fafafatherType = curContext.getTypeLabel(fafafatherNode);
		int returnVal = -1;
		switch (fafafatherType) {
		case StatementConstants.IFSTATEMENT:
			System.out.println("If predicate");
			MatchIfElse.matchIfPredicate(fp,a, curContext, fafafatherNode);
			break;
		case StatementConstants.FORSTATEMENT:
			System.out.println("For predicate#TODO");
			MatchForWhile.matchForPredicate(fp,a, curContext);
			break;
		case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
		case StatementConstants.EXPRESSIONSTATEMENT:
			System.out.println("variable/expression");
			matchExpressionStatementAndVariableDeclarationStatement(fp,a, curContext, fafafatherNode);
			break;
		default:
			System.err.println("Default:" + fafafatherType);
			break;
		}
		return returnVal;
	}

}
