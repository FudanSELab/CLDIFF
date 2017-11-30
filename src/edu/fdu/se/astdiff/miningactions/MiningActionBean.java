package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionGeneratorBean;

public class MiningActionBean {
	
	public MiningActionBean(ActionGeneratorBean agb, TreeContext src, TreeContext dst,MappingStore mapping){
		this.mActionGeneratorBean = agb;
		this.mMapping = mapping;
		this.mSrcTree = src;
		this.mDstTree = dst;
	}
	public ActionGeneratorBean mActionGeneratorBean;
	public MappingStore mMapping;
	
	public TreeContext mSrcTree;
	public TreeContext mDstTree;
	
}
