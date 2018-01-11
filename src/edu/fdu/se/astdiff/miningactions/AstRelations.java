package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

public class AstRelations {

	public static boolean isFatherIfStatement(Action a, TreeContext treeContext) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());
		if (type.equals(StatementConstants.IFSTATEMENT)) {
			return true;
		}
		return false;
	}

	public static boolean isChildContainIfStatement(Action a, TreeContext treeContext) {
		List<ITree> child = a.getNode().getChildren();
		if (child.size() < 1) {
			System.err.println("There is no child");
			return false;
		}
		for (ITree tmp : child) {
			Tree t = (Tree) tmp;
			String type = treeContext.getTypeLabel(t);
			if (type.equals(StatementConstants.IFSTATEMENT)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFatherTryStatement(Action a, TreeContext treeContext) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());
		if (type.equals(StatementConstants.TRYSTATEMENT)) {
			return true;
		}
		return false;
	}

	public static boolean isFatherSwitchStatement(Action a, TreeContext treeContext) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());
		if (type.equals(StatementConstants.SWITCHSTATEMENT)) {
			return true;
		}
		return false;
	}

	public static boolean isChildCotainSynchronizedStatement(Action a, TreeContext treeContext) {
		// Tree t = (Tree) a.getNode();
		List<ITree> child = a.getNode().getChildren();
		if (child.size() < 1) {
			System.err.println("There is no child");
			return false;
		}
		for (ITree tmp : child) {
			Tree t = (Tree) tmp;
			String type = treeContext.getTypeLabel(t);
			if (type.equals(StatementConstants.SYNCHRONIZEDSTATEMENT)) {
				return true;
			}
		}
		return false;
	}

	public static boolean ifFatherNodeTypeSameAs(Action a, TreeContext treeContext, String statementConstants) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());

		if (type.equals(statementConstants)) {
			return true;
		}
		return false;
	}

	public static String fatherStatement(Action a, TreeContext con) {
		Tree t = (Tree) a.getNode();
		String type = con.getTypeLabel(t.getParent());
		return type;
	}

	// public static boolean isAllChildrenNew(List<ITree> list) {
	// boolean allNewChildren = true;
	// for (ITree a : list) {
	// Tree t = (Tree) a;
	// if (t.getDoAction() == null) {
	// System.err.println("Unexpected Condition XXX");
	// } else if (t.getDoAction() instanceof Insert) {
	//
	// } else {
	// // update 或者move 操作 说明if语句内部有原语句
	// allNewChildren = false;
	// }
	// }
	// return allNewChildren;
	// }

	public static boolean isClassCreation(List<Action> list, TreeContext treeContext) {
		for (Action a : list) {
			String str = treeContext.getTypeLabel(a.getNode());
			if (StatementConstants.CLASSINSTANCECREATION.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNullCheck(ITree ifStatementNode, TreeContext treeContext) {
		if (ifStatementNode.getChildren().size() == 2) {
			Tree c1 = (Tree) ifStatementNode.getChild(0);
			Tree c2 = (Tree) ifStatementNode.getChild(1);
			String type = treeContext.getTypeLabel(c2);
			if (StatementConstants.BLOCK.equals(type)) {
				for (ITree tmp : c1.postOrder()) {
					if (StatementConstants.NULLLITERAL.equals(treeContext.getTypeLabel(tmp))) {
						return true;
					}
				}
				return false;
			} else {
				System.err.println("Not a block Error if");
			}
		}
		return false;
	}

	/**
	 * 找当前节点的父节点 XXXStatement XXXDelclaration JavaDoc CatchClause
	 * 
	 * @param node
	 * @param treeContext
	 * @return 返回fafafather
	 */
	public static ITree findFafafatherNode(ITree node, TreeContext treeContext) {
		String type = null;
		ITree curNode = node;
		while (!curNode.isRoot()) {
			type = treeContext.getTypeLabel(curNode);
			if (type.endsWith("Statement")) {
				break;
			} else if (type.equals(StatementConstants.METHODDECLARATION)||type.equals(StatementConstants.FIELDDECLARATION)) {
				break;
			} else if (type.equals(StatementConstants.BLOCK)) {
				break;
			} else if (type.equals(StatementConstants.JAVADOC)) {
				break;
			} else {
				curNode = curNode.getParent();
			}
		}
		if (curNode.isRoot())
			return null;
		else
			return curNode;
	}

	/**
	 * 根据所传statementType的值，找符合条件的父节点并返回
	 * 
	 * @param node
	 * @param treeContext
	 * @return 返回fafafather
	 */
	public static ITree findFafafatherNodeByStatementType(ITree node, TreeContext treeContext, String statementType) {
		// CatchClause
		String type = null;
		ITree curNode = node;
		String returnType = null;
		while (true) {
			type = treeContext.getTypeLabel(curNode);
			if (type.endsWith(statementType)) {
				returnType = type;
				break;
			} else {
				curNode = curNode.getParent();
			}
		}
		return curNode;
	}

}
