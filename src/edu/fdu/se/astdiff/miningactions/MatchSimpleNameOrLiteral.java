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
	public static void matchSimplenameOrLiteral(MiningActionData fp, Action a, Tree fafather) {
		// if predicate

		String faFatherType = fafather.getAstClass().getSimpleName();
		switch (faFatherType) {
		case StatementConstants.IFSTATEMENT:
//			System.out.println("If predicate");
			MatchIfElse.matchIfPredicate(fp,a,faFatherType, fafafatherNode, ffFatherNodeType);
			break;
		case StatementConstants.FORSTATEMENT:
//			System.out.println("For predicate");
			MatchForStatement.matchForPredicate(fp,a,faFatherType, fafafatherNode, ffFatherNodeType);
			break;
		case StatementConstants.ENHANCEDFORSTATEMENT:
//			System.out.println("Enhanced For predicate");
			MatchForStatement.matchEnhancedForPredicate(fp,a,faFatherType, fafafatherNode, ffFatherNodeType);
			break;
		case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
			MatchVariableDeclarationExpression.matchVariableDeclarationByFather(fp,a,faFatherType, fafafatherNode, ffFatherNodeType);
			break;
		case StatementConstants.EXPRESSIONSTATEMENT:
//			System.out.println("variable/expression");
			MatchExpressionStatement.matchExpressionByFather(fp,a,faFatherType, fafafatherNode, ffFatherNodeType);
			break;
		case StatementConstants.JAVADOC:
			operationBean = MatchJavaDoc.matchJavaDocByFather(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
			break;
		case StatementConstants.SWITCHCASE:
			//switchcase
			operationBean = MatchSwitch.matchSwitchCaseByFather(fp, a, nodeType, fafafatherNode, ffFatherNodeType);
			break;
		case StatementConstants.RETURNSTATEMENT:
			//return statement
			operationBean = MatchReturnStatement.matchReturnStatentByFather(fp, a, nodeType, fafafatherNode,ffFatherNodeType);
			break;
		case StatementConstants.CONSTRUCTORINVOCATION:
			//构造方法this
			operationBean = MatchMethod.matchConstructorInvocationByFather(fp,a,nodeType,fafafatherNode,ffFatherNodeType);
			break;
		case StatementConstants.SUPERCONSTRUCTORINVOCATION:
			//构造方法super
			operationBean = MatchMethod.matchSuperConstructorInvocationByFather(fp,a,nodeType,fafafatherNode,ffFatherNodeType);
			break;
		case StatementConstants.TYPEDECLARATION:
			//classs signiture
			operationBean = MatchClassSignature.matchClassSignature(fp,a,nodeType,fafafatherNode,ffFatherNodeType);
			break;
		case StatementConstants.TYPEDECLARATION:
			MatchClassSignature.matchClassSignature(fp, a, fafafather);
			break;
		case StatementConstants.FIELDDECLARATION:
			MatchFieldDeclaration.matchFieldDeclarationByFather(fp, a, type, fafafather, fatherType);
			break;
		case StatementConstants.INITIALIZER:
			break;
		case StatementConstants.METHODDECLARATION:
			if (!StatementConstants.BLOCK.equals(type)) {
				MatchMethodSignatureChange.matchMethodSignatureChange(fp, a, type, fafafather, fatherType);
			}
			break;
		default:
			String nextAction = SimpleActionPrinter.getMyOneActionString(a);
			System.err.print(nextAction);
			System.err.println("Default, SimplenameOrLiteral: " + faFatherType +"\n");
			fp.setActionTraversedMap(a);
			break;
		}
	}

}
