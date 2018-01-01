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
	public int matchExpression(Action a) {
		TreeContext con = null;
		if (a instanceof Insert) {
			con = this.mMiningActionBean.mDstTree;
		} else if (a instanceof Delete) {
			con = this.mMiningActionBean.mSrcTree;
		}
		String summary = "[PATTERN]";
		summary += ActionConstants.getInstanceStringName(a);
		List<Action> subActions = new ArrayList<Action>();
		boolean flag = MyTreeUtil.traverseAllChilrenCheckIfSameAction(a, subActions);
		this.mMiningActionBean.setActionTraversedMap(subActions);
		if (flag) {
			if (AstRelations.isClassCreation(subActions, con)) {
				summary += " object Initializing - expression assignment";
			} else {
				summary += " expression assignment";
			}
		} else {
			System.err.println("Unexpected Condition 2");
		}

		System.out.println(summary);
		return subActions.size();
	}


}
