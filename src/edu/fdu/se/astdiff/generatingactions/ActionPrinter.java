package edu.fdu.se.astdiff.generatingactions;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;

public class ActionPrinter {
	public static String tabs(int tabNum) {
		String res = "";
		for (int i = 0; i < tabNum; i++) {
			res += "\t";
		}
		return res;
	}

	public static String getMyOneActionString(Action a, int tabNum, TreeContext treeContext) {
		StringBuilder sb = new StringBuilder();
		String tabStr = tabs(tabNum);
		if (a instanceof Delete) {
			sb.append(tabStr + "Delete > ");
			Delete delete = (Delete) a;
			ITree deleteNode = delete.getNode();
			sb.append(prettyString(treeContext, deleteNode) + " from " + prettyString(treeContext, deleteNode.getParent()) + "\n");
//			sb.append(delete.toString() + "\n");
		}
		if (a instanceof Insert) {
			sb.append(tabStr + "Insert > ");
			Insert insert = (Insert) a;
			ITree insertNode = insert.getNode();
			sb.append(prettyString(treeContext, insertNode) + " to " + prettyString(treeContext, insertNode.getParent()) + " at "
					+ insert.getPosition() + "\n");
		}
		if (a instanceof Move) {
			sb.append(tabStr + "Move > ");
			Move move = (Move) a;
			ITree moveNode = move.getNode();
			sb.append(prettyString(treeContext, moveNode) + " to " + prettyString(treeContext, move.getParent()) + " at "
					+ move.getPosition() + "\n");// old
		}
		if (a instanceof Update) {
			sb.append(tabStr + "Update > ");
			Update update = (Update) a;
			ITree updateNode = update.getNode();
			sb.append(prettyString(treeContext, updateNode) + "from " + updateNode.getLabel() + " to " + update.getValue()
					+ "\n");// old
		}
		return sb.toString();
	}

	public static String prettyString(TreeContext con, ITree node) {
		return node.getId() + ". " + ((Tree)node).getAstClass().getSimpleName() + ":" + node.getLabel() + "(" + getStartLineNum(con, node)
				+ "-" + getEndLineNum(con, node) + ")";
	}

	public static int getStartLineNum(TreeContext con, ITree node) {
		ASTNode n = ((Tree) node).getAstNode();
		return con.getCu().getLineNumber(n.getStartPosition());
	}

	public static int getEndLineNum(TreeContext con, ITree node) {
		ASTNode n = ((Tree) node).getAstNode();
		return con.getCu().getLineNumber(n.getStartPosition() + n.getLength() - 1);
	}



	private static void printMyActions(List<Action> actions, TreeContext conD,TreeContext conS) {
		System.out.println("ActionSize:" + actions.size());
		for (Action a : actions) {
			if(a instanceof Insert)
				System.out.print(getMyOneActionString(a, 0, conD));
			else
				System.out.print(getMyOneActionString(a, 0, conS));
		}
	}

	/**
	 * 
	 * @param con
	 * @param tree
	 */
	public static String getPrettyTreeString(TreeContext con, ITree tree) {
		StringBuilder b = new StringBuilder();
		for (ITree t : TreeUtils.preOrder(tree))
			b.append(indent(t) + prettyString(con, t) + "\n");
		return b.toString();
	}

	private static String indent(ITree t) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < t.getDepth(); i++)
			b.append("\t");
		return b.toString();
	}

}
