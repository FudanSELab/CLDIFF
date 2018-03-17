package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statement.SwitchChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchSwitch {
	
	public static void matchSwitch(MiningActionData fp, Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
			DefaultUpDownTraversal.traverseSwitchStatements(a,subActions,changePacket);
		}
		fp.setActionTraversedMap(subActions);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);
		code.changeEntity = SwitchChangeEntity.switchStatement;
		fp.addOneChangeEntity(code);
	}

	public static void matchSwitchCase(MiningActionData fp, Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_CHANGE);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultUpDownTraversal.traverseSwitchCase(a, subActions, changePacket);
		}
		fp.setActionTraversedMap(subActions);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);
		if(a.getNode().getChildren() == null || a.getNode().getChildren().size()==0){
			code.changeEntity = SwitchChangeEntity.defaultCase;
		}else {
			code.changeEntity = SwitchChangeEntity.switchCase;
		}
		fp.addOneChangeEntity(code);

	}

	public static void matchSwitchNewEntity(MiningActionData fp,Action a, Tree queryFather,int treeType,Tree traverseFather){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_REFURNISH);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultDownUpTraversal.traverseSwitchCondition(traverseFather,subActions,changePacket);
		}
		fp.setActionTraversedMap(subActions);
		ClusteredActionBean mBean =  new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,subActions,changePacket,queryFather,treeType);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);
		fp.addOneChangeEntity(code);

	}
	public static void matchSwitchCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
		ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
		List<Action> actions = changeEntity.clusteredActionBean.actions;
		List<Action> newActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,actions,changePacket,false)){
			DefaultDownUpTraversal.traverseSwitchCondition(traverseFather,actions,changePacket);
		}
		for(Action tmp:newActions){
			if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
				continue;
			}
			actions.add(tmp);
		}
		fp.setActionTraversedMap(newActions);
	}


	public static void matchSwitchCaseNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType,Tree traverseFather){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_REFURNISH);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,subActions,changePacket);
		}
		fp.setActionTraversedMap(subActions);
		ClusteredActionBean mBean =  new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,subActions,changePacket,queryFather,treeType);
		SwitchChangeEntity code = new SwitchChangeEntity(mBean);
		code.changeEntity = SwitchChangeEntity.switchCase;
		fp.addOneChangeEntity(code);
	}

	public static void matchSwitchCaseCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
		ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
		List<Action> actions = changeEntity.clusteredActionBean.actions;
		List<Action> newActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,newActions,changePacket,false)){
			DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,newActions,changePacket);
		}
		for(Action tmp:newActions){
			if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
				continue;
			}
			actions.add(tmp);
		}
		fp.setActionTraversedMap(newActions);
	}
}
