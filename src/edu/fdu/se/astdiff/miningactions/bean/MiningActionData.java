package edu.fdu.se.astdiff.miningactions.bean;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.GeneratingActionsData;
import edu.fdu.se.astdiff.generatingactions.GumTreeDiffParser;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.generatingactions.JavaParserTreeGenerator;
import edu.fdu.se.astdiff.preprocessingfile.data.PreprocessedData;

public class MiningActionData {


	public GeneratingActionsData mGeneratingActionsData;
	public MappingStore mMapping;

	protected TreeContext mDstTree;
	protected TreeContext mSrcTree;

	private List<ChangeEntity> mChangeEntityList;

	public PreprocessedData preprocessedData;

	public MiningActionData(PreprocessedData preprocessedData, GeneratingActionsData agb, JavaParserTreeGenerator treeGenerator){
		this.preprocessedData = preprocessedData;
		this.mGeneratingActionsData = agb;
		this.mMapping = treeGenerator.mapping;
		this.mDstTree = treeGenerator.dstTC;
		this.mSrcTree = treeGenerator.srcTC;
		this.mGeneratingActionsData.generateActionMap();
		this.mChangeEntityList = new ArrayList<>();
	}


	public MiningActionData(GeneratingActionsData agb, GumTreeDiffParser treeGenerator){

		this.mGeneratingActionsData = agb;
		this.mMapping = treeGenerator.mapping;
		this.mDstTree = treeGenerator.dstTC;
		this.mSrcTree = treeGenerator.srcTC;
		this.mGeneratingActionsData.generateActionMap();
		this.mChangeEntityList = new ArrayList<>();
	}



	public void setActionTraversedMap(List<Action> mList) {
		for (Action tmp : mList) {
			if (this.mGeneratingActionsData.getAllActionMap().containsKey(tmp)) {
				this.mGeneratingActionsData.getAllActionMap().put(tmp, 1);
			} else {
				System.err.println("[ERR]action not added");
			}
		}
	}

	public void setActionTraversedMap(Action a) {
		if (this.mGeneratingActionsData.getAllActionMap().containsKey(a)) {
			this.mGeneratingActionsData.getAllActionMap().put(a, 1);
		} else {
			System.err.println("[ERR]action not added");
		}
	}



	public void addOneChangeEntity(ChangeEntity changeEntity){
		this.mChangeEntityList.add(changeEntity);

	}


	public ChangeEntity getEntityByNode(ITree tree){
		if(tree==null) return null;
		for(ChangeEntity changeEntity:this.getChangeEntityList()){
			if(changeEntity.clusteredActionBean.curAction instanceof Move){
				continue;
			}
			if(changeEntity.clusteredActionBean.fafather == tree){
				return changeEntity;
			}
		}
		return null;
	}


	public List<ChangeEntity> getChangeEntityList() {
		return this.mChangeEntityList;
	}

	public ITree getMappedSrcOfDstNode(ITree dst){
		return this.mMapping.getSrc(dst);
	}
	public ITree getMappedDstOfSrcNode(ITree src){
		return this.mMapping.getDst(src);
	}

}
