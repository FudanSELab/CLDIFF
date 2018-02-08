package edu.fdu.se.astdiff.miningactions.statement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.util.MyTreeUtil;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.SwitchChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.TryCatchChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

public class MatchTry {
	

	public static void matchTry(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
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
	//try-with-resources statement

	public static void matchCatchClause(MiningActionData fp,Action a){
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_UPGRADE);
		DefaultUpDownTraversal.traverseIf(a,subActions,changePacket);
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
		DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_UPGRADE);
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
		DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
		changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_UPGRADE);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		TryCatchChangeEntity code = new TryCatchChangeEntity(mBean);
		fp.addOneChangeEntity(code);
		code.changeEntity = TryCatchChangeEntity.finallyClause;
		fp.setActionTraversedMap(subActions);
	}



}
