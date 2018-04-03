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
			 sb.append(" from "+ prettyString((Tree)treeNode.getParent()) +" "+ treeNode.getRangeString()+ "\n");
		}
		if (a instanceof Insert) {
			sb.append( " to " + prettyString((Tree)treeNode.getParent()) + " at " +((Insert)a).getPosition() +" "+ treeNode.getRangeString()+ "\n");
		}
		if (a instanceof Move) {
			Move move = (Move) a;
			sb.append(" to " + prettyString((Tree)move.getParent()) + " at " + ((Move)a).getPosition()+" "+ treeNode.getRangeString() + "\n");// old
		}
		if (a instanceof Update) {
			sb.append( "from " + treeNode.getLabel() + " to " + ((Update)a).getValue() +" "+ treeNode.getRangeString()+ "\n");// old
		}
		return sb.toString();
	}

	public static final String INS = "I,";
	public static final String MOV = "M,";
	public static final String UPD = "U,";
	public static final String DEL = "D,";
	public static String prettyString(Tree node) {
		if(node.getDoAction()==null) {

			return node.getId() + "." + node.getAstClass().getSimpleName() + ":" + node.getLabel()+ " "+node.getRangeString();
		}else{
			String res = "[";
			for(Action a:node.getDoAction()){
				if(a instanceof Insert){
					res += INS;
				}else if(a instanceof Move){
					res += MOV;
				}else if(a instanceof Update){
					res+=UPD;
				}else{
					res+=DEL;
				}
			}
			res = res.substring(0,res.length()-1);
			res +="]";
			return node.getId() + "." + node.getAstClass().getSimpleName() + ":" + node.getLabel()+" "+res + " "+node.getRangeString();
		}
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
