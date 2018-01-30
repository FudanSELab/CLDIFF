package edu.fdu.se.astdiff.miningactions.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.MatchTry;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.VariableChangeEntity;

public class MatchVariableDeclarationExpression {
	/**
	 * level III VARIABLEDECLARATIONSTATEMENT
	 * 
	 * @param a
	 * @return
	 */
	public static ClusteredActionBean matchVariableDeclaration(MiningActionData fp, Action a, String nodeType) {
		String operationEntity = VariableChangeEntity.VARIABLEDECLARATION;

		List<Action> subActions = new ArrayList<>();
		int status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
		fp.setActionTraversedMap(subActions);

		ITree nodeContainVariableDeclarationFragment = AstRelations.isChildContainVariableDeclarationFragment(a.getNode());
		if (nodeContainVariableDeclarationFragment != null && nodeContainVariableDeclarationFragment.getChildren().size()>1) {
			operationEntity += "-OBJECT-INITIALIZING";
			if (AstRelations.isClassCreation(subActions)) {
				operationEntity += "-NEW";
			}
		}

		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,nodeLinePosition,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}

	public static void matchVariableDeclarationByFather(MiningActionData fp, Action a, String nodeType,ITree fafafatherNode, String ffFatherNodeType) {
		String operationEntity = VariableChangeEntity.VARIABLEDECLARATION;
		List<Action> subActions = new ArrayList<>();

		ITree srcfafafather = null;
		ITree dstfafafather = null;
		if (a instanceof Insert) {
			dstfafafather = fafafatherNode;
			srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
			if (srcfafafather == null) {
				System.err.println("err null mapping");
			}
		} else {
			srcfafafather = fafafatherNode;
			dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
			if (dstfafafather == null) {
				System.err.println("err null mapping");
			}
		}

		Set<String> srcT = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, subActions);
		Set<String> dstT = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, subActions);
		int status = MyTreeUtil.isSrcOrDstAdded(srcT,dstT);

		fp.setActionTraversedMap(subActions);

		ITree nodeContainVariableDeclarationFragment = AstRelations.isChildContainVariableDeclarationFragment(fafafatherNode);
		if (nodeContainVariableDeclarationFragment != null && nodeContainVariableDeclarationFragment.getChildren().size()>1) {
			operationEntity += "-OBJECT-INITIALIZING";
			if (AstRelations.isClassCreation(subActions)) {
				operationEntity += "-NEW";
			}
		}

		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);

		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,subActions,nodeLinePosition,status,operationEntity,null,null);
	}
}
