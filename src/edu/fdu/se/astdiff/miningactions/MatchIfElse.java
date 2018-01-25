package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.IfChangeEntity;

public class MatchIfElse {
	/**
	 * level III if 操作识别两种 一种是 原来语句包一个if 一种是直接新增if语句 if -> children update 可能
	 * insert 可能 新增if/else if + body是否也是新 2*2 = 4种情况 null 目前没有找到反例
	 * 
	 * @param a
	 * @param nodeType
	 * @return
	 */
	public static void matchIf(MiningActionData f, Action a, String nodeType) {
		String operationEntity = "";
		if (AstRelations.isFatherXXXStatement(a,StatementConstants.IFSTATEMENT)) {
			operationEntity = IfChangeEntity.ELSE_IF;
		} else {
			operationEntity = IfChangeEntity.IF;
		}
		List<ITree> children = a.getNode().getChildren();
		List<Action> ifSubActions = new ArrayList<>();
		int status;
		if(children.size()==2){
			status  = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(a, ifSubActions);
		}else{
			//size =3
			status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(a, 0, 1, ifSubActions);
		}
		f.setActionTraversedMap(ifSubActions);
		Range nodeLinePosition =  AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,ifSubActions,nodeLinePosition,status,operationEntity,null,null);
		IfChangeEntity ifChangeEntity = new IfChangeEntity(mHighLevelOperationBean);
		System.out.println(ifChangeEntity.toString());
		f.addOneChangeEntity(ifChangeEntity);
	}
	

	/**
	 * level III precondition father 是 if statement
	 * 
	 */
	public static void matchElse(MiningActionData f, Action a, String nodeType, ITree ffFatherNode, String ffFatherNodeType) {
		String operationEntity = IfChangeEntity.ELSE;
		List<Action> result = new ArrayList<>();
		int status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(a, result);
		f.setActionTraversedMap(result);
		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,result,nodeLinePosition,status,operationEntity,ffFatherNode,ffFatherNodeType);
		IfChangeEntity elseChangeEntity = new IfChangeEntity(mHighLevelOperationBean);
		f.addOneChangeEntity(elseChangeEntity);
	}
	
	
	/**
	 * level IV 因为往上找如果是if body那么匹配不是if statement 所以这部分应该就是predicate
	 * 
	 */
	public static void matchIfPredicate(MiningActionData fp, Action a, String nodeType, ITree fafatherNode, String fFatherNodeType) {
		// fafafatherNode是if 那么 第一个孩子是if里的内容
		String operationEntity = "";
		if (AstRelations.isFatherXXXStatement(a,StatementConstants.IFSTATEMENT)) {
			operationEntity = IfChangeEntity.ELSE_IF;
		} else {
			operationEntity = IfChangeEntity.IF;
		}

		ITree srcfafather = null;
		ITree dstfafather = null;
		if (a instanceof Insert) {
			dstfafather = fafatherNode;
			srcfafather = fp.getMappedSrcOfDstNode(dstfafather);
			if (srcfafather == null) {
				System.err.println("err null mapping");
			}
		} else {
			srcfafather = fafatherNode;
			dstfafather = fp.getMappedDstOfSrcNode(srcfafather);
			if (dstfafather == null) {
				System.err.println("err null mapping");
			}
		}
		List<Action> allActions = new ArrayList<>();
		Set<String> srcT = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(srcfafather.getChild(0), allActions);
		Set<String> dstT = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(dstfafather.getChild(0), allActions);
		int status = MatchTry.MyTreeUtil.isSrcOrDstAdded(srcT,dstT);
		fp.setActionTraversedMap(allActions);
		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafatherNode,fFatherNodeType);
		IfChangeEntity elseChangeEntity = new IfChangeEntity(mHighLevelOperationBean);
		fp.addOneChangeEntity(elseChangeEntity);
	}

}
