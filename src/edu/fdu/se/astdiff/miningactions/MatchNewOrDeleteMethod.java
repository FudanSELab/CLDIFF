package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchNewOrDeleteMethod {
	
	/**
	 * level III insert 操作中的新增方法 ok
	 * 
	 * 
	 * @param a
	 * @return
	 */
	public static HighLevelOperationBean matchNewOrDeleteMethod(FindPattern f,Action a) {
		String result = null;
		List<Action> actions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, actions);
		if (status ==MyTreeUtil.TYPE1) {
			result = "[PATTREN] Add method";
		} else if(status == MyTreeUtil.TYPE2){
		
		} else {
			System.err.println("What? Not New Method?");
		}
		f.setActionTraversedMap(actions);
		return null;
	}

}
