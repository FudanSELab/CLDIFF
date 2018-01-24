package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionPrinter;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MatchSimpleNameOrLiteral {
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
	public static void matchSimplenameOrLiteral(MiningActionData fp, Action a, String nodeType) {
		// if predicate
		Tree fafafatherNode = AstRelations.findFafafatherNode(a.getNode());

		String ffFatherNodeType = fafafatherNode.getAstClass().getSimpleName();
		ClusteredActionBean operationBean;
		switch (ffFatherNodeType) {
		case StatementConstants.IFSTATEMENT:
//			System.out.println("If predicate");
			operationBean = MatchIfElse.matchIfPredicate(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.FORSTATEMENT:
//			System.out.println("For predicate");
			operationBean = MatchForStatement.matchForPredicate(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			System.out.println(operationBean.toString());
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
			operationBean = MatchVariableDeclarationExpression.matchVariableDeclarationByFather(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.EXPRESSIONSTATEMENT:
//			System.out.println("variable/expression");
			operationBean = MatchExpressionStatement.matchExpressionByFather(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.JAVADOC:
			operationBean = MatchJavaDoc.matchJavaDocByFather(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.SWITCHCASE:
			//switchcase
			operationBean = MatchSwitch.matchSwitchCaseByFather(fp, a, nodeType, fafafatherNode, ffFatherNodeType);
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.RETURNSTATEMENT:
			//return statement
			operationBean = MatchReturnStatement.matchReturnStatentByFather(fp, a, nodeType, fafafatherNode,ffFatherNodeType);
			fp.addHighLevelOperationBeanToList(operationBean);
			break;
		case StatementConstants.CONSTRUCTORINVOCATION:
			//构造方法this
			operationBean = MatchMethod.matchConstructorInvocationByFather(fp,a,nodeType,fafafatherNode,ffFatherNodeType);
			fp.mHighLevelOperationBeanList.add(operationBean);
			break;
		case StatementConstants.SUPERCONSTRUCTORINVOCATION:
			//构造方法super
			operationBean = MatchMethod.matchSuperConstructorInvocationByFather(fp,a,nodeType,fafafatherNode,ffFatherNodeType);
			fp.mHighLevelOperationBeanList.add(operationBean);
			break;
		default:
			String nextAction = SimpleActionPrinter.getMyOneActionString(a);
			System.out.print(nextAction);
			System.out.println("Default, SimplenameOrLiteral: " + ffFatherNodeType +"\n");
			fp.setActionTraversedMap(a);
			break;
		}
	}

}
