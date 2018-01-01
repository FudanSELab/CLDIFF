package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchExpressionStatement {
	/**
	 * level III
	 * 
	 * @param a
	 * @return
	 */
	public static int matchExpression(FindPattern fp,Action a) {
		TreeContext con = null;
		if (a instanceof Insert) {
			con = fp.getDstTree();
		} else if (a instanceof Delete) {
			con = fp.getSrcTree();
		}
		String summary = "[PATTERN]";
		summary += ActionConstants.getInstanceStringName(a);
		List<Action> subActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);
		if(status == MyTreeUtil.TYPE1){
			summary += "insert";
		}else if(status == MyTreeUtil.TYPE2){
			summary += "delete";
		}
		if (AstRelations.isClassCreation(subActions, con)) {
			summary += " object Initializing - expression assignment";
		} else {
			summary += " expression assignment";
		}
		

		System.out.println(summary);
		return subActions.size();
	}


}
