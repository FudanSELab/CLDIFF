package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.IfChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.SynchronizedChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.core.BasicCompilationUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MatchSynchronized {
	
	public static void matchSynchronized(MiningActionData fp, Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
			DefaultUpDownTraversal.traverseIf(a, subActions, changePacket);
		}
		fp.setActionTraversedMap(subActions);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		SynchronizedChangeEntity code = new SynchronizedChangeEntity(mBean);
		fp.addOneChangeEntity(code);
	}

	public static void matchSynchronizedChangeNewEntity(MiningActionData fp,Action a,Tree queryFather,Tree traverseFather){
		ChangePacket changePacket = new ChangePacket();
		List<Action> sameEdits = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		if(!BasicTreeTraversal.traverseWhenActionIsMove(a,sameEdits,changePacket,false)){
			DefaultDownUpTraversal.traverseIfPredicate(traverseFather,sameEdits,changePacket);
		}
		fp.setActionTraversedMap(sameEdits);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,queryFather);
		SynchronizedChangeEntity code = new SynchronizedChangeEntity(mBean);
		fp.addOneChangeEntity(code);

	}
	public static void matchSynchronizedChangeCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
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
