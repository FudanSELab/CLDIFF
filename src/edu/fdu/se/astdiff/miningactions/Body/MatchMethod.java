package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.MethodChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchMethod {
	

	public static void matchMethdDeclaration(MiningActionData fp, Action a) {
		ChangePacket changePacket = new ChangePacket();
		List<Action> subActions = new ArrayList<>();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
		changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
		DefaultUpDownTraversal.traverseMethod(a,subActions,changePacket);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
		MethodChangeEntity code = new MethodChangeEntity(mBean);
		fp.addOneChangeEntity(code);
	}


	public static void matchMethodSignatureChangeNewEntity(MiningActionData fp, Action a, Tree fafather) {
		ChangePacket changePacket = new ChangePacket();
		changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
		List<Action> sameEdits = new ArrayList<>();
		DefaultDownUpTraversal.traverseMethodSignature(fafather,sameEdits,changePacket);
		fp.setActionTraversedMap(sameEdits);
		Range range = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,(Tree)fafather);
		MethodChangeEntity code = new MethodChangeEntity(mBean);
		fp.addOneChangeEntity(code);
	}





}
