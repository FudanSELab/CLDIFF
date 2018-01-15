package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;

import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.miningoperationbean.HighLevelOperationBean;
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
	public static HighLevelOperationBean matchIf(MiningActionData f, Action a, String type) {
		String operationEntity = "";
		TreeContext treeContext;
		if(a instanceof Insert)
			treeContext = f.getDstTree();
		else
			treeContext = f.getSrcTree();

		if (AstRelations.isFatherIfStatement(a, treeContext)) {
			operationEntity = "ELSE_IF";
		} else {
			operationEntity = "IF";
		}
		List<ITree> children = a.getNode().getChildren();
		boolean ifNoBlockFlag = true;
		for(ITree tmp:children) {
			String labelType = null;
			if(a instanceof Insert){
				labelType = f.getDstTreeContextTypeLabel(tmp);
			}else{
				labelType = f.getSrcTreeContextTypeLabel(tmp);
			}
			if(StatementConstants.BLOCK.equals(labelType)) {
				ifNoBlockFlag = false;
				break;
			}
		}
		List<Action> ifSubActions = new ArrayList<Action>();
		int status  = -2;
		if(children.size()==2){
			status  = MyTreeUtil.traverseNodeGetAllEditActions(a, ifSubActions);
		}else{
			//size =3
			status = MyTreeUtil.traverseNodeGetAllEditActions(a, 0, 1, ifSubActions);
		}

		f.setActionTraversedMap(ifSubActions);
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,type,ifSubActions,status,operationEntity,null,null);
		return mHighLevelOperationBean;
	}
	

	/**
	 * level III precondition father 是 if statement
	 * 
	 * @param a
	 * @return
	 */
	public static HighLevelOperationBean matchElse(MiningActionData f, Action a, String nodeType, ITree ffFatherNode, String ffFatherNodeType) {
		String operationEntity = "ELSE";
		String labelType = f.getDstTreeContextTypeLabel(a.getNode()) ;
		if(!StatementConstants.BLOCK.equals(labelType)){
			String label = "(no {}) ";
		}
		List<Action> result = new ArrayList<Action>();
		int status = MyTreeUtil.traverseNodeGetAllEditActions(a, result);
		f.setActionTraversedMap(result);
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,result,status,operationEntity,ffFatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}
	
	
	/**
	 * level IV 因为往上找如果是if body那么匹配不是if statement 所以这部分应该就是predicate
	 * 
	 * @param a
	 * @param fp
	 * @return
	 */
	public static HighLevelOperationBean matchIfPredicate(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
		// fafafatherNode是if 那么 第一个孩子是if里的内容
		String operationEntity  = "IFPREDICATE";
		ITree srcParent = null;
		List<Action> allActions = new ArrayList<Action>();
		ITree srcfafafather = null;
		ITree dstfafafather = null;
//		if (a instanceof Insert) {
//			if (fp.getMappedSrcOfDstNode(fafafatherNode) != null) {
//				srcParent = fp.getMappedSrcOfDstNode(fafafatherNode);
//				List<Action> tmp = new ArrayList<Action>();
//				int status2 = MyTreeUtil.traverseNodeGetAllEditActions(srcParent, tmp);
//				allActions.addAll(tmp);
//			} else {
//				System.err.println("ERerererR");
//			}
//		} else {
//			srcParent = fafafatherNode;
//		}
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

		boolean dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather.getChild(0), allActions);
		boolean src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather.getChild(0), allActions);
		int status = MyTreeUtil.isSrcorDstAdded(src_status,dst_status);

		fp.setActionTraversedMap(allActions);
		HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
				a,nodeType,allActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
		return mHighLevelOperationBean;
	}

}
