package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

public class AstRelations {
	
	public static boolean isFatherIfStatement(Action a){
		Tree t = (Tree) a.getNode();
		String type = StaticTreeContext.mDstTree.getTypeLabel(t.getParent());
		if(type.equals(StatementConstants.IFSTATEMENT)){
			return true;
		}
		return false;
	}
	
	public static boolean isAllChildrenNew(List<ITree> list){
		boolean allNewChildren = true;
		for(ITree a:list){
			Tree t = (Tree) a;
			if (t.getDoAction() == null) {
				System.err.println("Unexpected Condition");
			} else if (t.getDoAction() instanceof Insert) {

			} else {
				// update 或者move 操作 说明if语句内部有原语句
				allNewChildren = false;
			}
		}
		return allNewChildren;
	}
	
	public static boolean isClassCreation(List<Action> list){
		for(Action a:list){
			String str = StaticTreeContext.mDstTree.getTypeLabel(a.getNode());
			if(StatementConstants.CLASSINSTANCECREATION.equals(str)){
				return true;
			}
		}
		return false;
	}
}
