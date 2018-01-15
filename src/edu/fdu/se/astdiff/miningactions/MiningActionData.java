package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MiningActionData {

	public MiningActionData(GeneratingActionsData agb, TreeContext src, TreeContext dst, MappingStore mapping) {
		this.mGeneratingActionsData = agb;
		this.mMapping = mapping;
		this.mDstTree = dst;
		this.mSrcTree = src;
		this.mGeneratingActionsData.generateActionMap();

		this.mHighLevelOperationBeanList = new ArrayList<ClusteredActionBean>();
	}

	public GeneratingActionsData mGeneratingActionsData;
	public MappingStore mMapping;

	protected TreeContext mDstTree;
	protected TreeContext mSrcTree;




	protected List<ClusteredActionBean> mHighLevelOperationBeanList;


	public void setActionTraversedMap(List<Action> mList) {
		for (Action tmp : mList) {
			if (this.mGeneratingActionsData.getAllActionMap().containsKey(tmp)) {
				this.mGeneratingActionsData.getAllActionMap().put(tmp, 1);
			} else {
				System.err.println("action not added");
			}
		}
	}

	public void setActionTraversedMap(Action a) {
		if (this.mGeneratingActionsData.getAllActionMap().containsKey(a)) {
			this.mGeneratingActionsData.getAllActionMap().put(a, 1);
		} else {
			System.err.println("action not added");
		}
	}


	public void addHighLevelOperationBeanToList(ClusteredActionBean mHighLevelOperationBean) {
		this.mHighLevelOperationBeanList.add(mHighLevelOperationBean);
	}

	public TreeContext getDstTree(){
		return this.mDstTree;
	}

	public TreeContext getSrcTree(){
		return this.mSrcTree;
	}
	public String getDstTreeContextTypeLabel(ITree node){
		return this.mDstTree.getTypeLabel(node);
	}

	public String getSrcTreeContextTypeLabel(ITree node){
		return this.mSrcTree.getTypeLabel(node);
	}



	public List<ClusteredActionBean> getmHighLevelOperationBeanList() {
		return mHighLevelOperationBeanList;
	}




	public ITree getMappedSrcOfDstNode(ITree dst){
		return this.mMapping.getSrc(dst);
	}
	public ITree getMappedDstOfSrcNode(ITree src){
		return this.mMapping.getDst(src);
	}

}
