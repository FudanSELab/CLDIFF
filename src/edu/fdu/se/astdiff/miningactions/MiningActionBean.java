package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ActionGeneratorBean;

public class MiningActionBean {
	
	public MiningActionBean(ActionGeneratorBean agb, TreeContext src, TreeContext dst,MappingStore mapping){
		this.mActionGeneratorBean = agb;
		this.mMapping = mapping;
		this.mActionGeneratorBean.generateActionMap();
		StaticTreeContext.mDstTree = dst;
		StaticTreeContext.mSrcTree = src;
	}
	public ActionGeneratorBean mActionGeneratorBean;
	public MappingStore mMapping;
	
//	public TreeContext mSrcTree;
//	public TreeContext mDstTree;
	
	

	
}
