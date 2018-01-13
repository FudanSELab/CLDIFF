package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionGeneratorBean;

public class MiningActionBean {

	public MiningActionBean(ActionGeneratorBean agb, TreeContext src, TreeContext dst, MappingStore mapping) {
		this.mActionGeneratorBean = agb;
		this.mMapping = mapping;

		this.mDstTree = dst;
		this.mSrcTree = src;

		this.mActionGeneratorBean.generateActionMap();
	}

	public ActionGeneratorBean mActionGeneratorBean;
	public MappingStore mMapping;

	public TreeContext mDstTree;
	public TreeContext mSrcTree;

	/**
	 * mList的action，把map中的entry置为已访问
	 * 
	 * @param mList
	 */
	public void setActionTraversedMap(List<Action> mList) {
		for (Action tmp : mList) {
			if (this.mActionGeneratorBean.getAllActionMap().containsKey(tmp)) {
				this.mActionGeneratorBean.getAllActionMap().put(tmp, 1);
			} else {
				System.err.println("action not added");
			}
		}
	}

	/**
	 * mList的action，把map中的entry置为已访问
	 * 
	 * @param mList
	 */
	public void setActionTraversedMap(Action a) {
		if (this.mActionGeneratorBean.getAllActionMap().containsKey(a)) {
			this.mActionGeneratorBean.getAllActionMap().put(a, 1);
		} else {
			System.err.println("action not added");
		}
	}

}
