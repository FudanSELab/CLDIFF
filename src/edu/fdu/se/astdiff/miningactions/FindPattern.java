package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.gumtree.MyTreeUtil;

public class FindPattern {

	public FindPattern(MiningActionBean bean) {
		this.mMiningActionBean = bean;
	}

	private MiningActionBean mMiningActionBean;

	// ITree insNode = ins.getNode();
	// if(dstTC.getTypeLabel(insNode).equals("IfStatement")){
	// ITree child = insNode.getChild(0);
	// if(dstTC.getTypeLabel(child).equals("InfixExpression")){
	// for(ITree tmp:insNode.preOrder()){
	//// System.out.println(dstTC.getTypeLabel(tmp));
	/**
	 * if 操作识别两种 一种是 原来语句包一个if 一种是直接新增if语句 if -> children update 可能 insert 可能
	 * null 目前没有找到反例 TODO
	 * 
	 * @param a
	 * @param type
	 * @return
	 */
	public int matchIf(Action a, String type) {
		String ifOrElseif = "";
		if (AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mDstTree)) {
			ifOrElseif = "else if";
		} else {
			ifOrElseif = "if";
		}
		String summary = "insert " + ifOrElseif;
		List<Action> ifSubActions = MyTreeUtil.traverseNodeGetSameEditActions(a);
		for (Action tmp : ifSubActions) {
			this.mMiningActionBean.mActionGeneratorBean.getInsertActionMap().put(tmp, 1);
			Insert blockIns = (Insert) tmp;
			String str = this.mMiningActionBean.mDstTree.getTypeLabel(blockIns.getNode());
			if ("Block".equals(str)) {
				Tree tree = (Tree) blockIns.getNode();
				List<ITree> children = tree.getChildren();
				if (AstRelations.isAllChildrenNew(children)) {
					summary += " clause and body";
				} else {
					summary += " clause to api calls";
				}
			}
		}
		System.out.println(summary);
		return ifSubActions.size();
	}

	public int matchElse(Action a) {
		// precondition 前面是if statement了
		String summary = "";
		Tree root = (Tree) a.getNode();
		List<ITree> children = root.getChildren();
		if (AstRelations.isAllChildrenNew(children)) {
			summary = "insert else clause and body";
		} else {
			summary = "insert else clause to api calls";
		}
		List<Action> ifSubActions = MyTreeUtil.traverseNodeGetSameEditActions(a);
		for (Action tmp : ifSubActions) {
			this.mMiningActionBean.mActionGeneratorBean.getInsertActionMap().put(tmp, 1);
		}
		System.out.println(summary);
		return ifSubActions.size();
	}

	public int matchTry(Action a) {
		String summary;
		List<Action> ifSubActions = MyTreeUtil.traverseNodeGetSameEditActions(a);
		Tree root = (Tree) a.getNode();
		List<ITree> children = root.getChildren();
		if (AstRelations.isAllChildrenNew(children)) {
			summary = "insert try catch clause and body";
		} else {
			summary = "insert try catch clause to api calls";
		}
		for (Action tmp : ifSubActions) {
			this.mMiningActionBean.mActionGeneratorBean.getInsertActionMap().put(tmp, 1);
		}
		System.out.println(summary);
		return ifSubActions.size();
	}

	public int matchVariableDeclaration(Action a) {
		// variable declaration statement
		String summary = "";
		List<Action> subActions = MyTreeUtil.traverseNodeGetSameEditActions(a);
		List<ITree> children = new ArrayList<ITree>();
		for (Action tmp : subActions) {
			this.mMiningActionBean.mActionGeneratorBean.getInsertActionMap().put(tmp, 1);
			Tree it = (Tree) a.getNode();
			children.add(it);
		}
		if (AstRelations.isAllChildrenNew(children)) {
			if (AstRelations.isClassCreation(subActions, this.mMiningActionBean.mDstTree)) {
				summary = "[insert] variable declaration - class creation";
			} else {
				summary = "[insert] variable declaration";
			}

		} else {
			System.err.println("Unexpected Condition 2");
		}

		System.out.println(summary);
		return subActions.size();

	}

	public int matchExpression(Action a) {
		String summary = "";
		List<Action> subActions = MyTreeUtil.traverseNodeGetSameEditActions(a);
		List<ITree> children = new ArrayList<ITree>();
		for (Action tmp : subActions) {
			this.mMiningActionBean.mActionGeneratorBean.getInsertActionMap().put(tmp, 1);
			Tree it = (Tree) a.getNode();
			children.add(it);
		}
		if (AstRelations.isAllChildrenNew(children)) {
			if (AstRelations.isClassCreation(subActions, this.mMiningActionBean.mDstTree)) {
				summary = "insert expression assignment - class creation";
			} else {
				summary = "insert expression assignment";
			}

		} else {
			System.err.println("Unexpected Condition 2");
		}

		System.out.println(summary);
		return subActions.size();
	}

	public int matchSimplename(Action a) {
		if (AstRelations.ifFatherStatementSame(a, this.mMiningActionBean.mDstTree,StatementConstants.METHODINVOCATION)) {
			System.out.println("Method call change / parameter addition \n");
			return 1;
		} 
		if (AstRelations.ifFatherStatementSame(a, this.mMiningActionBean.mDstTree,StatementConstants.VARIABLEDECLARATIONFRAGMENT)) {
			System.err.println("Unexpected Condition 3");
			return 1;
		} 
		System.err.println("Unexpected Condition 4");
		return 1;

	}

	public void find() {
		this.findInsert();
		this.findUpdate();
	}

	// if else if else情况
	public void findInsert() {

		int insertActionCount = this.mMiningActionBean.mActionGeneratorBean.getInsertActionMap().size();
		int insertActionIndex = 0;
		int count = 0;
		while (insertActionCount != 0) {
			Action a = this.mMiningActionBean.mActionGeneratorBean.getInsertActions().get(insertActionIndex);
			insertActionIndex++;
			if (this.mMiningActionBean.mActionGeneratorBean.getInsertActionMap().get(a) == 1) {
				// 标记过的action
				continue;
			}
			Insert ins = (Insert) a;
			ITree insNode = ins.getNode();
			String type = this.mMiningActionBean.mDstTree.getTypeLabel(insNode);
			String nextAction = ConsolePrint.printMyOneActionString(a, 0, this.mMiningActionBean.mDstTree);
			System.out.println(nextAction);
			switch (type) {
			case StatementConstants.IFSTATEMENT:
				// Pattern 1. Match If/else if
				count = matchIf(a, type);
				insertActionCount -= count;
				break;
			case StatementConstants.BLOCK:
				// Pattern 1.2 Match else
				if (AstRelations.isFatherIfStatement(a, this.mMiningActionBean.mDstTree)) {
					count = this.matchElse(a);
					insertActionCount -= count;
				} else {
					System.err.println("Other Condition");
					//TODO剩下的情况
				}
				break;
			case StatementConstants.TRYSTATEMENT:
				count = matchTry(a);
				insertActionCount -= count;break;
			case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
				count = this.matchVariableDeclaration(a);
				insertActionCount -= count;break;
			case StatementConstants.EXPRESSIONSTATEMENT:
				count = this.matchExpression(a);
				insertActionCount -= count;break;
			case StatementConstants.SIMPLENAME:
				count = this.matchSimplename(a);
				insertActionCount -= count;break;
			default:
				break;
			}

		}
	}

	public void findUpdate() {
		int updateActionMapSize = this.mMiningActionBean.mActionGeneratorBean.getUpdateActionMap().size();
		int index = 0;
		while (updateActionMapSize != index) {
			Action a = this.mMiningActionBean.mActionGeneratorBean.getUpdateActions().get(index);
			index++;
			Update up = (Update) a;
			ITree tmp = a.getNode();
			System.out.println("[update] from" + tmp.getLabel()+" to " + up.getValue());
		}

	}

}
