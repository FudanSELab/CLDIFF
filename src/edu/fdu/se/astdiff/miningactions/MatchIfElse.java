package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.IfChangeEntity;
import edu.fdu.se.gumtree.MyTreeUtil;

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
			operationEntity = "ELSE_IF";
		} else {
			operationEntity = "IF";
		}
		List<ITree> children = a.getNode().getChildren();
		List<Action> ifSubActions = new ArrayList<>();
		int status;
		if(children.size()==2){
			status  = MyTreeUtil.traverseNodeGetAllEditActions(a, ifSubActions);
		}else{
			//size =3
			status = MyTreeUtil.traverseNodeGetAllEditActions(a, 0, 1, ifSubActions);
		}

		f.setActionTraversedMap(ifSubActions);
		Range nodeLinePosition = AstRelations.getnodeLinePosition(a);
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,ifSubActions,nodeLinePosition,status,operationEntity,null,null);
		IfChangeEntity ifChangeEntity = new IfChangeEntity(mHighLevelOperationBean);
		f.addOneChangeEntity(ifChangeEntity);
	}
	

	/**
	 * level III precondition father 是 if statement
	 * 
	 */
	public static ClusteredActionBean matchElse(MiningActionData f, Action a, String nodeType, ITree ffFatherNode, String ffFatherNodeType) {
		String operationEntity = "ELSE";
		Tree t = (Tree) a.getNode();
		String labelType = t.getAstClass().getSimpleName();
		if(!StatementConstants.BLOCK.equals(labelType)){
			String label = "(no {}) ";
		}
		List<Action> result = new ArrayList<>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, result);
		f.setActionTraversedMap(result);
		Range nodeLinePosition = AstRelations.getnodeLinePosition(a);
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,result,nodeLinePosition,status,operationEntity,ffFatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}
	
	
	/**
	 * level IV 因为往上找如果是if body那么匹配不是if statement 所以这部分应该就是predicate
	 * 
	 */
	public static ClusteredActionBean matchIfPredicate(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
		// fafafatherNode是if 那么 第一个孩子是if里的内容
		String operationEntity  = "IFPREDICATE";

		List<Action> allActions = new ArrayList<Action>();

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

		Set<String> srcT = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather.getChild(0), allActions);
		Set<String> dstT = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather.getChild(0), allActions);
		int status = MyTreeUtil.isSrcOrDstAdded(srcT,dstT);

		fp.setActionTraversedMap(allActions);
		Range nodeLinePosition = AstRelations.getnodeLinePosition(a);
		ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
				a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}

}
