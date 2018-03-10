package edu.fdu.se.astdiff.miningactions.statement;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statement.TryCatchChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

public class MatchTry {
	

	public static void matchTry(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
			DefaultUpDownTraversal.traverseTryStatements(a,subActions,changePacket);
		}
		fp.setActionTraversedMap(subActions);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		TryCatchChangeEntity code = new TryCatchChangeEntity(mBean);
		Tree firstC = (Tree) a.getNode().getChild(0);
		if(firstC.getAstNode().getNodeType() == ASTNode.BLOCK){
			code.changeEntity = TryCatchChangeEntity.tryCatch;
		}else{
			code.changeEntity = TryCatchChangeEntity.tryWithResources;
		}
		fp.addOneChangeEntity(code);
	}


	public static void matchCatchClause(MiningActionData fp,Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_CHANGE);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
			DefaultUpDownTraversal.traverseIf(a,subActions,changePacket);
		}
		fp.setActionTraversedMap(subActions);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		TryCatchChangeEntity code = new TryCatchChangeEntity(mBean);
		code.changeEntity = TryCatchChangeEntity.catchClause;
		fp.addOneChangeEntity(code);
	}

	public static void matchThrowStatement(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
		}
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_CHANGE);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		TryCatchChangeEntity code = new TryCatchChangeEntity(mBean);
		fp.addOneChangeEntity(code);
		code.changeEntity = TryCatchChangeEntity.throwStatement;
		fp.setActionTraversedMap(subActions);

	}

	public static void matchFinally(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
			DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
		}
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_CHANGE);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		TryCatchChangeEntity code = new TryCatchChangeEntity(mBean);
		fp.addOneChangeEntity(code);
		code.changeEntity = TryCatchChangeEntity.finallyClause;
		fp.setActionTraversedMap(subActions);
	}

	public static void matchCatchChangeNewEntity(MiningActionData fp,Action a,Tree queryFather,Tree traverseFather){
		ChangePacket changePacket = new ChangePacket();
		List<Action> sameEdits = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_REFURNISH);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,sameEdits,changePacket,false)){
			DefaultDownUpTraversal.traverseIfPredicate(traverseFather,sameEdits,changePacket);
		}
		fp.setActionTraversedMap(sameEdits);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,queryFather);
		TryCatchChangeEntity code = new TryCatchChangeEntity(mBean);
		code.changeEntity = TryCatchChangeEntity.catchClause;
		fp.addOneChangeEntity(code);

	}
	public static void matchCatchChangeCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
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
		fp.setActionTraversedMap(newActions);

	}



}
