package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSwitch {
	
	public int matchSwitch(Action a){
		String changeType = ActionConstants.getInstanceStringName(a);
		String summary = "[PATTERN] " + changeType + " Switch ";

		List<Action> ifSubActions = new ArrayList<Action>();
		boolean flag = MyTreeUtil.traverseAllChilrenCheckIfSameAction(a, ifSubActions);
		boolean nullCheck = AstRelations.isNullCheck(a.getNode(), this.mMiningActionBean.mDstTree);
		this.mMiningActionBean.setActionTraversedMap(ifSubActions);
		if (flag) {
			summary += " and body";
		} else {
			if (a instanceof Insert) {
				summary += " wrapper[insert]";
			} else if (a instanceof Delete) {
				summary += " wrapper[delete]";
			}

		}
		if (nullCheck) {
			System.out.println("5.Adding a null checker." + summary);
		}

		System.out.println(summary);
		return ifSubActions.size();
	}


}
