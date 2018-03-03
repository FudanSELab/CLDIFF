package edu.fdu.se.astdiff.miningactions.util;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Position;
import com.github.javaparser.Range;

public class AstRelations {

	public static Range getRangeOfAstNode(Action a){
		Integer[] range = ((Tree)a.getNode()).getRange();
		Range linePosition = new Range(new Position(range[0],1),new Position(range[1],1));
		return linePosition;
	}

	public static boolean isSubChildContainXXX(Action a,String stmt) {
		List<ITree> child = a.getNode().getChildren();
		if (child.size() < 1) {
			System.err.println("There is no child");
			return false;
		}
		for (ITree tmp : child) {
			Tree t = (Tree) tmp;
			String type = t.getAstClass().getSimpleName();
			if (stmt.equals(type)) {
				return true;
			}
		}
		return false;
	}


	public static boolean isFatherXXXStatement(Action a,int astNodeType) {
		Tree parentTree = (Tree) a.getNode().getParent();
		int type = parentTree.getAstNode().getNodeType();
		if (astNodeType == type) {
			return true;
		}
		return false;
	}

	public static boolean isFatherXXXStatement(Tree node,int astNodeType){
		int type =((Tree) node.getParent()).getAstNode().getNodeType();
		if (astNodeType == type) {
			return true;
		}
		return false;
	}


	public static boolean actionListContainsXXX(List<Action> list,String stmt) {
		for (Action a : list) {
			Tree t = (Tree) a.getNode();
			String str = t.getAstClass().getSimpleName();
			if (stmt.equals(str)) {
				return true;
			}
		}
		return false;
	}





}
