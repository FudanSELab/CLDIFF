package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.gumtree.MyTreeUtil;

public class MatchIfElse {
	/**
	 * level III if 操作识别两种 一种是 原来语句包一个if 一种是直接新增if语句 if -> children update 可能
	 * insert 可能 新增if/else if + body是否也是新 2*2 = 4种情况 null 目前没有找到反例
	 * 
	 * @param a
	 * @param type
	 * @return
	 */
	public static HighLevelOperationBean matchIf(FindPattern f,Action a, String type) {
		String ifOrElseif = "";
		String operationType = "";
		String operationEntity = "";
		if (AstRelations.isFatherIfStatement(a, f.getDstTree())) {
			ifOrElseif = "else if clause";
			operationEntity = "ELSE_IF";
		} else {
			ifOrElseif = "if clause";
			operationEntity = "IF";
		}
		String changeType = ActionConstants.getInstanceStringName(a);
		
		String summary = "[PATTERN] " + changeType + " " + ifOrElseif;

		List<ITree> children = a.getNode().getChildren();
		boolean ifNoBlockFlag = true;
		for(ITree tmp:children) {
			String labelType = f.getDstTreeContextTypeLabel(tmp);
			if(StatementConstants.BLOCK.equals(labelType)) {
				ifNoBlockFlag = false;
				break;
			}
		}
		if(ifNoBlockFlag)
			summary += "( no {} )";

		List<Action> ifSubActions = new ArrayList<Action>();
		int status  = MyTreeUtil.traverseNodeGetAllEditActions(a, ifSubActions);
		boolean nullCheck = AstRelations.isNullCheck(a.getNode(),f.getDstTree());
		f.setActionTraversedMap(ifSubActions);
		switch(status){
		case MyTreeUtil.TYPE1:
		case MyTreeUtil.TYPE2:
		
		}
		if (status == MyTreeUtil.TYPE1) {
			operationType = changeType+"--BODY";
			summary += " and body";
		} else if(status == MyTreeUtil.TYPE2){
			
		} else {
			if (a instanceof Insert) {
				summary += " wrapper[insert]";
				operationType = changeType+"--WRAPPER";
			} else if (a instanceof Delete) {
				summary += " wrapper[delete]";
				operationType = changeType+"--WRAPPER";
			}

		}
		if (nullCheck) {
			System.out.println("5.Adding a null checker." + summary);
		}
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,type,ifSubActions,operationType,operationEntity,null,null);
		return mHighLevelOperationBean;
	}
	

	/**
	 * level III precondition father 是 if statement
	 * 
	 * @param a
	 * @return
	 */
	public static int matchElse(FindPattern f,Action a) {
		String changeType = ActionConstants.getInstanceStringName(a);
		String summary = changeType + " else clause ";
		String labelType = f.getDstTreeContextTypeLabel(a.getNode()) ;
		if(!StatementConstants.BLOCK.equals(labelType))
			summary += "(no {}) ";
		Tree root = (Tree) a.getNode();
		List<ITree> children = root.getChildren();
		List<Action> result = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, result);
		switch(status){
		case MyTreeUtil.TYPE1:
		case MyTreeUtil.TYPE2:
		case MyTreeUtil.TYPE4:
		case MyTreeUtil.TYPE5:
		default:
			break;
		}
		f.setActionTraversedMap(result);
		return result.size();
	}
	
	
	/**
	 * level IV 因为往上找如果是if body那么匹配不是if statement 所以这部分应该就是predicate
	 * 
	 * @param a
	 * @param treeContext
	 * @return
	 */
	public static int matchIfPredicate(FindPattern fp,Action a, TreeContext treeContext, ITree fafafatherNode) {
		// fafafatherNode是if 那么 第一个孩子是if里的内容
		ITree srcParent = null;
		List<Action> allActions = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(fafafatherNode.getChild(0), allActions);
		switch(status){
		case MyTreeUtil.TYPE4:
		case MyTreeUtil.TYPE5:
		default:break;
		}
		if (a instanceof Insert) {
			if (fp.getMappedSrcOfDstNode(fafafatherNode) != null) {
				srcParent = fp.getMappedSrcOfDstNode(fafafatherNode);
				List<Action> tmp = new ArrayList<Action>();
				int status2 = MyTreeUtil.traverseNodeGetAllEditActions(srcParent, tmp);
				allActions.addAll(tmp);
			} else {
				System.err.println("ERerererR");
			}
		} else {
			srcParent = fafafatherNode;
		}
		fp.setActionTraversedMap(allActions);
//		this.mMiningActionBean.mapIfPredicateAndAction(srcParent, allActions);
		return allActions.size();
	}

}
