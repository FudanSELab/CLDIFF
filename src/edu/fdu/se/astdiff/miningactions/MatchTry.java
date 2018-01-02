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
	public static int matchTry(FindPattern fp,Action a) {
		String summary = "[PATTERN] " + ActionConstants.getInstanceStringName(a);
		List<Action> tryAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, tryAction);

		if (status == MyTreeUtil.TYPE1) {
			summary += " try catch clause and body";
		} else{
			summary += " try catch clause wrapper";
		}
		fp.setActionTraversedMap(tryAction);
		System.out.println(summary);
		return tryAction.size();
	}

	public int matchTryPlus(FindPattern fp,Action a) {
		String summary = "[PATTERN] " + ActionConstants.getInstanceStringName(a);
		List<Action> tryAction = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, tryAction);
		Insert ins = (Insert) a;
		ITree insNode = ins.getNode();
		ITree fafafatherCatchClause = AstRelations.findFafafatherNodeByStatementType(insNode, fp.getDstTree(),StatementConstants.CATCHCLAUSE);
		String fatherCatchClauseType = fp.getDstTreeContextTypeLabel(fafafatherCatchClause);
		
		if (status == MyTreeUtil.TYPE1) {
			summary += " try catch clause and body";
		} else{
			summary += " try catch clause wrapper";
		}
		fp.setActionTraversedMap(tryAction);
		System.out.println(summary);
		return tryAction.size();
	}

}
