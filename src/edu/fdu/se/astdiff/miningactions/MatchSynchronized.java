package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchSynchronized {
	
	public static int matchSynchronized(FindPattern fp,Action a){

//		List<ITree> child = a.getNode().getChildren();
//		//String type = treeContext.getTypeLabel(t.getChild(0));
//		if(child.size() !=1){
//			System.out.println("Other Synchronized, the child size is "+child.size());
//			return 0;
//		}

		String changeType = ActionConstants.getInstanceStringName(a);
		String summary = "[PATTERN] " + changeType + " Synchronized ";

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
