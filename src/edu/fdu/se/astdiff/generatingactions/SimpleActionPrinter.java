package edu.fdu.se.astdiff.generatingactions;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

public class SimpleActionPrinter {
	public static String tabs(int tabNum) {
		String res = "";
		for (int i = 0; i < tabNum; i++) {
			res += "\t";
		}
		return res;
	}

	public static String getMyOneActionString(Action a) {
		StringBuilder sb = new StringBuilder();
		Tree treeNode = (Tree)a.getNode();
		sb.append(a.getClass().getSimpleName());
		sb.append("> ");
		sb.append(prettyString(treeNode));
		if (a instanceof Delete) {
			 sb.append(" from "+ prettyString((Tree)treeNode.getParent()) + "\n");
		}
		if (a instanceof Insert) {
			sb.append( " to " + prettyString((Tree)treeNode.getParent()) + " at " +((Insert)a).getPosition() + "\n");
		}
		if (a instanceof Move) {
			sb.append(" to " + prettyString((Tree)treeNode.getParent()) + " at " + ((Move)a).getPosition() + "\n");// old
		}
		if (a instanceof Update) {
			sb.append( "from " + treeNode.getLabel() + " to " + ((Update)a).getValue() + "\n");// old
		}
		return sb.toString();
	}

	public static String prettyString(Tree node) {
		return node.getId() + ". " + node.getAstClass().getSimpleName() + ":" + node.getLabel();//+node.getRangeString();
	}





	public static void printMyActions(List<Action> actions) {
		System.out.println("ActionSize:" + actions.size());
		actions.forEach(action -> System.out.print(getMyOneActionString(action)));
	}

	/**
	 * 
	 */
	public static String getPrettyTreeString(ITree tree) {
		StringBuilder b = new StringBuilder();
		for (ITree t : TreeUtils.preOrder(tree)) {
			Tree t2 = (Tree) t;
			b.append(indent(t2) + prettyString(t2) + "\n");
		}
		return b.toString();
	}

	private static String indent(Tree t) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < t.getDepth(); i++)
			b.append("\t");
		return b.toString();
	}

}
