package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ActionGeneratorBean;

public class MiningActionBean {

	public MiningActionBean(ActionGeneratorBean agb, TreeContext src, TreeContext dst, MappingStore mapping) {
		this.mActionGeneratorBean = agb;
		this.mMapping = mapping;
		this.mActionGeneratorBean.generateActionMap();
		this.mDstTree = dst;
		this.mSrcTree = src;
		this.methodInvocationList = new HashMap<ITree, List<Action>>();
		this.methodInvocationActionList = new HashMap<ITree, Set<String>>();
	}

	public ActionGeneratorBean mActionGeneratorBean;
	public MappingStore mMapping;

	public TreeContext mDstTree;
	public TreeContext mSrcTree;

	public Map<ITree, List<Action>> methodInvocationList;
	public Map<ITree, Set<String>> methodInvocationActionList;

	public void addMethodInvocationAction(ITree parent, Action a) {
		if (this.methodInvocationList.containsKey(parent)) {
			List<Action> mList = this.methodInvocationList.get(parent);
			mList.add(a);

		} else {
			List<Action> mList = new ArrayList<Action>();
			mList.add(a);
			this.methodInvocationList.put(parent, mList);
		}
		String type = null;
		if (a instanceof Insert) {
			type = ActionConstants.INSERT;
		} else if (a instanceof Update) {
			type = ActionConstants.UPDATE;
		} else if (a instanceof Delete) {
			type = ActionConstants.DELETE;
		} else {
			System.err.println("Upexpected Condition 6");
			return;
		}
		if (this.methodInvocationActionList.containsKey(parent)) {
			Set<String> mSet = this.methodInvocationActionList.get(parent);
			mSet.add(type);
		} else {
			Set<String> mSet = new HashSet<String>();
			mSet.add(type);
			this.methodInvocationActionList.put(parent, mSet);
		}
	}

}
