package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSwitch {
	
	public static int matchSwitch(FindPattern fp,Action a){
		String changeType = ActionConstants.getInstanceStringName(a);
		String summary = "[PATTERN] " + changeType + " Switch ";

		List<Action> ifSubActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, ifSubActions);
		fp.setActionTraversedMap(ifSubActions);
		if (status == MyTreeUtil.TYPE1) {
			summary += " and body";
		} else {
			if (a instanceof Insert) {
				summary += " wrapper[insert]";
			} else if (a instanceof Delete) {
				summary += " wrapper[delete]";
			}

		}

		System.out.println(summary);
		return ifSubActions.size();
	}


}
