package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchVariableDeclarationExpression {
	/**
	 * level III VARIABLEDECLARATIONSTATEMENT
	 * 
	 * @param a
	 * @return
	 */
	public static int matchVariableDeclaration(FindPattern fp,Action a) {
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
		if (status == MyTreeUtil.TYPE1) {
			
		} else if(status ==MyTreeUtil.TYPE2){
		
		}
		if (AstRelations.isClassCreation(subActions, con)) {
			summary += " object Initializing - variable declaration";
		} else {
			summary += " variable declaration";
		}

		System.out.println(summary);
		return subActions.size();

	}
}
