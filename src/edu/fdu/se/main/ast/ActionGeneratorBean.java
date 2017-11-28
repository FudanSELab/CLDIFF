package edu.fdu.se.main.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

public class ActionGeneratorBean {
	
	
	protected List<Action> dstTreeActions;
	protected List<Integer> dstLayerLastNodeIndex;
	protected List<Integer> dstTreeActionIndex;
	
	
	protected List<Action> srcTreeActions;
	protected List<Integer> srcLayerLastNodeIndex;
	protected List<Integer> srcTreeActionIndex;
	

	public ActionGeneratorBean() {
		super();
		this.dstTreeActions = new ArrayList<Action>();
		this.dstLayerLastNodeIndex = new ArrayList<Integer>();
		this.dstTreeActionIndex = new ArrayList<Integer>();
		
		this.srcTreeActions = new ArrayList<Action>();
		this.srcLayerLastNodeIndex =new ArrayList<Integer>();
		this.srcTreeActionIndex = new ArrayList<Integer>();
	}


	public List<Action> getDstTreeActions() {
		return dstTreeActions;
	}


	public List<Integer> getDstLayerLastNodeIndex() {
		return dstLayerLastNodeIndex;
	}


	public List<Integer> getDstTreeActionIndex() {
		return dstTreeActionIndex;
	}


	public List<Action> getSrcTreeActions() {
		return srcTreeActions;
	}


	public List<Integer> getSrcLayerLastNodeIndex() {
		return srcLayerLastNodeIndex;
	}


	public List<Integer> getSrcTreeActionIndex() {
		return srcTreeActionIndex;
	}
	

}
