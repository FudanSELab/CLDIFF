package edu.fdu.se.astdiff.miningactions.statement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.statement.IfChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

public class MatchIfElse {


	public static void matchIf(MiningActionData fp, Action a) {
//		checkExample(a);
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
			DefaultUpDownTraversal.traverseIf(a,subActions,changePacket);
		}
		ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
		IfChangeEntity code = new IfChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
		code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_IF_STMT);
//		code.stageIIBean.setOpt2(null);
		code.stageIIBean.setSubEntity(null);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.setActionTraversedMap(subActions);
		fp.addOneChangeEntity(code);

//		if (AstRelations.isFatherXXXStatement(a, ASTNode.IF_STATEMENT)) {
//			//code.changeEntity = IfChangeEntity.ELSE_IF;
	}
	public static void checkExample(Action a){
		if(!(a instanceof Insert)){
			return;
		}
		List<ITree> children = a.getNode().getChildren();
		if(children.size()!=2){
			return;
		}
		Tree block = (Tree)children.get(1);
		if(block.getAstNode().getNodeType()!= ASTNode.BLOCK){
			return;
		}
		List<ITree> children2 =block.getChildren();
		Set<String> tmp = new HashSet<>();
		for(ITree tree:children2){
			Tree t= (Tree)tree;
			if(t.getDoAction()==null){
				tmp.add(ActionConstants.NULLACTION);
				continue;
			}
			for(Action b:t.getDoAction()){
				tmp.add(b.getClass().getSimpleName());
			}
		}
		if(tmp.size()==2 && tmp.contains(ActionConstants.NULLACTION) && tmp.contains(Insert.class.getSimpleName())){
			System.out.println("[Find] find Example--------------------------------------");
		}
	}
	


	public static void matchElse(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
		}
		ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
		IfChangeEntity code = new IfChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
//		code.stageIIBean.setOpt(OperationTypeConstants.getChangeEntityDescString(a));
		if(a instanceof Move){
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE);
		}else {
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
		}
		code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_IF_STMT);
//		code.stageIIBean.setOpt2(OperationTypeConstants.getChangeEntityDescString(a));
		code.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_ELSE);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.addOneChangeEntity(code);
		fp.setActionTraversedMap(subActions);
	}
	




	public static void matchIfPredicateChangeNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType,Tree traverseFather) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> sameEdits = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,sameEdits,changePacket,false)){
			DefaultDownUpTraversal.traverseIfPredicate(traverseFather,sameEdits,changePacket);
		}
		ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,queryFather,treeType);
		IfChangeEntity code = new IfChangeEntity(mBean);
		code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
		code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
		if(a instanceof Move){
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE);
			code.stageIIBean.setChangeEntity(((Tree)a.getNode()).getAstClass().getSimpleName());
		}else {
			code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
			code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_IF_STMT);
		}
//		code.stageIIBean.setOpt2(OperationTypeConstants.getChangeEntityDescString(a));
		code.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_CONDITION);
		code.stageIIBean.setLineRange(code.lineRange.toString());
		code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
		fp.setActionTraversedMap(sameEdits);
		fp.addOneChangeEntity(code);
//		if (AstRelations.isFatherXXXStatement(traverseFather,ASTNode.IF_STATEMENT)) {
////			code.changeEntity = IfChangeEntity.ELSE_IF;
//		} else {
////			code.changeEntity = IfChangeEntity.IF;
//		}

	}
	public static void matchIfPredicateChangeCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
		ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
		List<Action> actions = changeEntity.clusteredActionBean.actions;
		List<Action> newActions = new ArrayList<>();
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,newActions,changePacket,false)){
			DefaultDownUpTraversal.traverseIfPredicate(traverseFather,newActions,changePacket);
		}
		for(Action tmp:newActions){
			if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
				continue;
			}
			actions.add(tmp);
		}
//		changeEntity.linkBean.addAppendedActions(newActions);
		fp.setActionTraversedMap(newActions);
	}

}
