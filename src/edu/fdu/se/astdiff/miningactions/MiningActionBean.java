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
		this.fatherToActionMap = new HashMap<ITree, List<Action>>();
		this.fatherToActionChangeTypeMap = new HashMap<ITree, Set<String>>();
		this.fatherTypeToFathersMap = new HashMap<String,List<ITree>>();
		
		this.methodSignatureMap = new HashMap<ITree, List<Action>>();
		this.methodSignatureAcionList = new HashMap<ITree,Set<String>>();
		this.mActionGeneratorBean.generateActionMap();
	}
	

	public ActionGeneratorBean mActionGeneratorBean;
	public MappingStore mMapping;

	public TreeContext mDstTree;
	public TreeContext mSrcTree;
	/**
	 * fatherTypeTofatherMap存每一种类型的father的list，
	 * 由这个list去其他两个map找对应的action和变化的类型
	 */
	public Map<String,List<ITree>> fatherTypeToFathersMap;
	public Map<ITree, List<Action>> fatherToActionMap;
	public Map<ITree, Set<String>> fatherToActionChangeTypeMap;

	public Map<ITree, List<Action>> methodSignatureMap;
	public Map<ITree, Set<String>> methodSignatureAcionList;
	
	
	public void addParentAndAction(ITree parent, Action a) {
		if (this.fatherToActionMap.containsKey(parent)) {
			List<Action> mList = this.fatherToActionMap.get(parent);
			mList.add(a);

		} else {
			List<Action> mList = new ArrayList<Action>();
			mList.add(a);
			this.fatherToActionMap.put(parent, mList);
		}
		String type = ActionConstants.getInstanceStringName(a);
		if (this.fatherToActionChangeTypeMap.containsKey(parent)) {
			Set<String> mSet = this.fatherToActionChangeTypeMap.get(parent);
			mSet.add(type);
		} else {
			Set<String> mSet = new HashSet<String>();
			mSet.add(type);
			this.fatherToActionChangeTypeMap.put(parent, mSet);
		}
	}
	private void mapMethodInvocationAndAction(ITree parent,Action a){
		if(this.fatherTypeToFathersMap.containsKey(ActionConstants.METHODINVOCATION)){
			List<ITree> mList = this.fatherTypeToFathersMap.get(ActionConstants.METHODINVOCATION);
			mList.add(parent);
		}else{
			List<ITree> mList = new ArrayList<ITree>();
			mList.add(parent);
			this.fatherTypeToFathersMap.put(ActionConstants.METHODINVOCATION, mList);
		}
		addParentAndAction(parent,a);
	}
	private void mapIfPredicateAndAction(ITree parent,Action a){
		if(this.fatherTypeToFathersMap.containsKey(ActionConstants.IfPredicate)){
			List<ITree> mList = this.fatherTypeToFathersMap.get(ActionConstants.IfPredicate);
			mList.add(parent);
		}else{
			List<ITree> mList = new ArrayList<ITree>();
			mList.add(parent);
			this.fatherTypeToFathersMap.put(ActionConstants.IfPredicate, mList);
		}
		addParentAndAction(parent,a);
	}
	private void mapForPredicateAndAction(ITree parent,Action a){
		if(this.fatherTypeToFathersMap.containsKey(ActionConstants.ForPredicate)){
			List<ITree> mList = this.fatherTypeToFathersMap.get(ActionConstants.ForPredicate);
			mList.add(parent);
		}else{
			List<ITree> mList = new ArrayList<ITree>();
			mList.add(parent);
			this.fatherTypeToFathersMap.put(ActionConstants.ForPredicate, mList);
		}
		addParentAndAction(parent,a);
	}
	public void mapMethodInvocationAndActions(ITree parent,List<Action> mList){
		for(Action tmp:mList){
			this.mapMethodInvocationAndAction(parent, tmp);
		}
	}
	public void mapIfPredicateAndAction(ITree parent,List<Action> mList){
		for(Action tmp:mList){
			this.mapIfPredicateAndAction(parent, tmp);
		}
	}
	public void mapForPredicateAndAction(ITree parent,List<Action> mList){
		for(Action tmp:mList){
			this.mapForPredicateAndAction(parent, tmp);
		}
	}
	
	
	/**
	 * fafafather即mathod declaration节点为key，action 为value
	 * @param fafafather
	 * @param a
	 */
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
	/**
	 * fafafather即mathod declaration节点为key，action list 为value
	 * @param fafafather
	 * @param mActionList
	 */
	public void mapMethodSignatureAction(ITree fafafather, List<Action> mActionList) {
		for (Action a : mActionList) {
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
	}
	/**
	 * mList的action，把map中的entry置为已访问
	 * @param mList
	 */
	public void setActionTraversedMap(List<Action> mList){
		for(Action tmp:mList){
			if(this.mActionGeneratorBean.getAllActionMap().containsKey(tmp)){
				this.mActionGeneratorBean.getAllActionMap().put(tmp, 1);
			}else {
				System.err.println("action not added");
			}
		}
	}

}
