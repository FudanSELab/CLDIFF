package edu.fdu.se.astdiff.miningactions;


import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionGeneratorBean;

public class MiningActionBean {
	
	public MiningActionBean(ActionGeneratorBean agb, TreeContext src, TreeContext dst,MappingStore mapping){
		this.mActionGeneratorBean = agb;
		this.mMapping = mapping;
		this.mActionGeneratorBean.generateActionMap();
		this.mDstTree = dst;
		this.mSrcTree = src;
	}
	public ActionGeneratorBean mActionGeneratorBean;
	public MappingStore mMapping;
	
	
	public TreeContext mDstTree;
	public TreeContext mSrcTree;
	
	

	
}
