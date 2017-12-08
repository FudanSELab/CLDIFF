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
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;

public class MiningActionBean {

	public MiningActionBean(ActionGeneratorBean agb, TreeContext src, TreeContext dst, MappingStore mapping) {
		this.mActionGeneratorBean = agb;
		this.mMapping = mapping;
		
		this.mDstTree = dst;
		this.mSrcTree = src;
		this.methodInvocationMap = new HashMap<ITree, List<Action>>();
		this.methodInvocationActionList = new HashMap<ITree, Set<String>>();
		
		this.methodSignatureMap = new HashMap<ITree, List<Action>>();
		this.methodSignatureAcionList = new HashMap<ITree,Set<String>>();
		this.mActionGeneratorBean.generateActionMap();
	}

	public ActionGeneratorBean mActionGeneratorBean;
	public MappingStore mMapping;

	public TreeContext mDstTree;
	public TreeContext mSrcTree;

	public Map<ITree, List<Action>> methodInvocationMap;
	public Map<ITree, Set<String>> methodInvocationActionList;

	public Map<ITree, List<Action>> methodSignatureMap;
	public Map<ITree, Set<String>> methodSignatureAcionList;

	public void mapMethodInvocationAndAction(ITree parent, Action a) {
		if (this.methodInvocationMap.containsKey(parent)) {
			List<Action> mList = this.methodInvocationMap.get(parent);
			mList.add(a);

		} else {
			List<Action> mList = new ArrayList<Action>();
			mList.add(a);
			this.methodInvocationMap.put(parent, mList);
		}
		String type = ActionConstants.getInstanceStringName(a);
		if (this.methodInvocationActionList.containsKey(parent)) {
			Set<String> mSet = this.methodInvocationActionList.get(parent);
			mSet.add(type);
		} else {
			Set<String> mSet = new HashSet<String>();
			mSet.add(type);
			this.methodInvocationActionList.put(parent, mSet);
		}
	}

	public void mapMethodSignatureAction(ITree fafafather, Action a) {
		if (this.methodSignatureMap.containsKey(fafafather)) {
			List<Action> mList = this.methodSignatureMap.get(fafafather);
			mList.add(a);
		} else {
			List<Action> mList = new ArrayList<Action>();
			mList.add(a);
			this.methodSignatureMap.put(fafafather, mList);
		}
		String type = ActionConstants.getInstanceStringName(a);
		if (this.methodSignatureAcionList.containsKey(fafafather)) {
			Set<String> mSet = this.methodSignatureAcionList.get(fafafather);
			mSet.add(type);
		} else {
			Set<String> mSet = new HashSet<String>();
			mSet.add(type);
			this.methodSignatureAcionList.put(fafafather, mSet);
		}
	}

	public void mapMethodSignatureAction(ITree fafafather, List<Action> mActionList) {
		for (Action a : mActionList) {
			if (this.methodSignatureMap.containsKey(fafafather)) {
				List<Action> mList = this.methodSignatureMap.get(fafafather);
				mList.add(a);
			} else {
				System.out.println("put");
				List<Action> mList = new ArrayList<Action>();
				mList.add(a);
				this.methodSignatureMap.put(fafafather, mList);
			}
			String type = ActionConstants.getInstanceStringName(a);
			if (this.methodSignatureAcionList.containsKey(fafafather)) {
				Set<String> mSet = this.methodSignatureAcionList.get(fafafather);
				mSet.add(type);
			} else {
				Set<String> mSet = new HashSet<String>();
				mSet.add(type);
				this.methodSignatureAcionList.put(fafafather, mSet);
			}
		}
	}
	
	public void setActionTraversedMap(List<Action> mList){
		for(Action tmp:mList){
			if(this.mActionGeneratorBean.getAllActionMap().containsKey(tmp)){
				ConsolePrint.printMyOneActionString(tmp, 0, mDstTree);
				this.mActionGeneratorBean.getAllActionMap().put(tmp, 1);
			}else {
				System.err.println("action not added");
			}
		}
	}

}
