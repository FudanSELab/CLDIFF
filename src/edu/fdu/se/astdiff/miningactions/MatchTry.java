package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchTry {
	
	/**
	 * level III
	 * 
	 * @param a
	 * @return
	 */
	public int matchTry(Action a) {
		String summary = "[PATTERN] " + ActionConstants.getInstanceStringName(a);
		List<Action> tryAction = new ArrayList<Action>();
		boolean flag = MyTreeUtil.traverseAllChilrenCheckIfSameAction(a, tryAction);
		if (flag) {
			summary += " try catch clause and body";
		} else{
			summary += " try catch clause wrapper";
		}
		this.mMiningActionBean.setActionTraversedMap(tryAction);
		System.out.println(summary);
		return tryAction.size();
	}

	public int matchTryPlus(Action a) {
		String summary = "[PATTERN] " + ActionConstants.getInstanceStringName(a);
		List<Action> tryAction = new ArrayList<Action>();
		boolean flag = MyTreeUtil.traverseAllChilrenCheckIfSameAction(a, tryAction);
		Insert ins = (Insert) a;
		ITree insNode = ins.getNode();
		ITree fafafatherCatchClause = AstRelations.findFafafatherNodeByStatementType(insNode, this.mMiningActionBean.mDstTree,StatementConstants.CATCHCLAUSE);
		String fatherCatchClauseType = this.mMiningActionBean.mDstTree.getTypeLabel(fafafatherCatchClause);
		if (flag ) {
			summary += " try catch clause and body";
		} else{
			summary += " try catch clause wrapper";
		}
		this.mMiningActionBean.setActionTraversedMap(tryAction);
		System.out.println(summary);
		return tryAction.size();
	}

}
