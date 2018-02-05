package edu.fdu.se.astdiff.miningactions.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.util.MyTreeUtil;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.IfChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.ReturnChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

public class MatchIfElse {


	public static void matchIf(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		DefaultUpDownTraversal.traverseTypeIIStatements(a,subActions,changePacket);
		fp.setActionTraversedMap(subActions);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		IfChangeEntity code = new IfChangeEntity(mBean);
		fp.addOneChangeEntity(code);
		if (AstRelations.isFatherXXXStatement(a, ASTNode.IF_STATEMENT)) {
			code.changeEntity = IfChangeEntity.ELSE_IF;
			changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_UPGRADE);
		} else {
			code.changeEntity = IfChangeEntity.IF;
			changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE);
		}
	}
	


	public static void matchElse(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		DefaultUpDownTraversal.traverseTypeIIStatements(a,subActions,changePacket);
		fp.setActionTraversedMap(subActions);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		IfChangeEntity code = new IfChangeEntity(mBean);
		fp.addOneChangeEntity(code);
		code.changeEntity = IfChangeEntity.ELSE;
	}
	
	
	/**
	 * level IV 因为往上找如果是if body那么匹配不是if statement 所以这部分应该就是predicate
	 * 
	 */
	public static void matchIfPredicateChangeNewEntity(MiningActionData fp, Action a, Tree fafather, List<Action> sameEdits) {

		ChangePacket changePacket = new ChangePacket();
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
		fp.setActionTraversedMap(sameEdits);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,fafather);
		IfChangeEntity code = new IfChangeEntity(mBean);
		fp.addOneChangeEntity(code);

		// fafafatherNode是if 那么 第一个孩子是if里的内容
		String operationEntity = "";
		if (AstRelations.isFatherXXXStatement(a,ASTNode.IF_STATEMENT)) {
			operationEntity = IfChangeEntity.ELSE_IF;
		} else {
			operationEntity = IfChangeEntity.IF;
		}

	}

}
