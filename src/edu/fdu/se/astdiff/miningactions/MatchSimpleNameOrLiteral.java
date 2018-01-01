package edu.fdu.se.astdiff.miningactions;

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
	public int matchExpressionStatementAndVariableDeclarationStatement(Action a, TreeContext treeContext,
			ITree fafafatherNode) {
		ITree srcParent = null;
		List<Action> allActions = MyTreeUtil.traverseNodeGetAllEditAction(fafafatherNode);
		if(a instanceof Insert){
			if(this.mMiningActionBean.mMapping.getSrc(fafafatherNode)!=null){
				srcParent = this.mMiningActionBean.mMapping.getSrc(fafafatherNode);
				List<Action> tmp = MyTreeUtil.traverseNodeGetAllEditAction(srcParent);
				allActions.addAll(tmp);
			}else{
				System.err.println("ERERERER");
			}
		}else{
			srcParent = fafafatherNode;
		}
		this.mMiningActionBean.setActionTraversedMap(allActions);
		this.mMiningActionBean.mapMethodInvocationAndActions(srcParent, allActions);
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
	public int matchSimplenameOrLiteral(Action a, TreeContext curContext) {
		// if predicate
		ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode(), curContext);
		String fafafatherType = curContext.getTypeLabel(fafafatherNode);
		int returnVal = -1;
		switch (fafafatherType) {
		case StatementConstants.IFSTATEMENT:
			System.out.println("If predicate");
			returnVal = this.matchIfPredicate(a, curContext, fafafatherNode);
			break;
		case StatementConstants.FORSTATEMENT:
			System.out.println("For predicate#TODO");
			returnVal = this.matchForPredicate(a, curContext);
			break;
		case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
		case StatementConstants.EXPRESSIONSTATEMENT:
			System.out.println("variable/expression");
			returnVal = this.matchExpressionStatementAndVariableDeclarationStatement(a, curContext, fafafatherNode);
			break;
		default:
			System.err.println("Default:" + fafafatherType);
			break;
		}
		return returnVal;
	}

}
