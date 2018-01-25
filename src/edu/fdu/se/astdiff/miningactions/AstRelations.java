package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class AstRelations {
	/**
	 * 找当前节点的父节点 XXXStatement XXXDelclaration JavaDoc CatchClause
	 *
	 * @param node aa
	 * @return 返回fafafather
	 */
	public static Tree findFafafatherNode(ITree node) {
		String type = null;
		Tree curNode = (Tree)node;
		while (!curNode.isRoot()) {
			type = curNode.getAstClass().getSimpleName();
			if (type.endsWith("Statement")) {
				break;
			} else{
				boolean isEnd = false;
				switch(type) {
					//declaration
					case StatementConstants.METHODDECLARATION:
					case StatementConstants.FIELDDECLARATION:
					case StatementConstants.TYPEDECLARATION:
						//
					case StatementConstants.BLOCK:
					case StatementConstants.JAVADOC:
					case StatementConstants.INITIALIZER:
					case StatementConstants.SWITCHCASE:
						//this(),super()
					case StatementConstants.CONSTRUCTORINVOCATION:
					case StatementConstants.SUPERCONSTRUCTORINVOCATION:
						isEnd = true;
						break;
					default:
						curNode = (Tree)curNode.getParent();
						break;
				}
				if(isEnd) {
					break;
				}
			}
		}
		if (curNode.isRoot())
			return null;
		else
			return curNode;
	}

	public static Range getRangeOfAstNode(Action a){
		Integer[] range = ((Tree)a.getNode()).getRange();
		Range linePosition = new Range(new Position(range[0],1),new Position(range[1],1));
		return linePosition;
	}



	public static boolean isSubChildContainXXX(Action a,String stmt) {
		List<ITree> child = a.getNode().getChildren();
		if (child.size() < 1) {
			System.err.println("There is no child");
			return false;
		}
		for (ITree tmp : child) {
			Tree t = (Tree) tmp;
			String type = t.getAstClass().getSimpleName();
			if (stmt.equals(type)) {
				return true;
			}
		}
		return false;
	}


	public static boolean isFatherXXXStatement(Action a,String stmt) {
		Tree parentTree = (Tree) a.getNode().getParent();
		String type = parentTree.getAstClass().getSimpleName();
		if (stmt.equals(type)) {
			return true;
		}
		return false;
	}


	public static boolean actionListContainsXXX(List<Action> list,String stmt) {
		for (Action a : list) {
			Tree t = (Tree) a.getNode();
			String str = t.getAstClass().getSimpleName();
			if (stmt.equals(str)) {
				return true;
			}
		}
		return false;
	}

//	/**
//	 * 遍历节点
//	 * @param fp
//	 * @param a
//	 * @param nodeType
//	 * @param operationEntity
//     * @return
//     */
//    public static ClusteredActionBean matchByNode(MiningActionData fp, Action a, String nodeType,String operationEntity){
//        List<Action> subActions = new ArrayList<>();
//        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
//        fp.setActionTraversedMap(subActions);
//		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
//		int traverseType = ClusteredActionBean.TRAVERSE_UP_DOWN;
//        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(traverseType
//                a,subActions,nodeLinePosition,status,operationEntity,null,null);
//        return mHighLevelOperationBean;
//    }
//
//    public static ClusteredActionBean matchByFafafatherNode(MiningActionData fp, Action a, String nodeType,String operationEntity, ITree fafafatherNode, String ffFatherNodeType){
//        List<Action> allActions = new ArrayList<>();
//        ITree srcfafafather = null;
//        ITree dstfafafather = null;
//        if (a instanceof Insert) {
//            dstfafafather = fafafatherNode;
//            srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
//            if (srcfafafather == null) {
//                System.err.println("err null mapping");
//            }
//        } else {
//            srcfafafather = fafafatherNode;
//            dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
//            if (dstfafafather == null) {
//                System.err.println("err null mapping");
//            }
//        }
//        Set<String> src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
//        Set<String> dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
//        int status = MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
//        fp.setActionTraversedMap(allActions);
//		Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
//        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
//                a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
//        return mHighLevelOperationBean;
//    }


//	public static boolean isFatherTryStatement(Action a) {
//		Tree t = (Tree) a.getNode().getParent();
//		String type = t.getAstClass().getSimpleName();
//		if (StatementConstants.TRYSTATEMENT.equals(type)) {
//			return true;
//		}
//		return false;
//	}

//	public static boolean isFatherSwitchStatement(Action a) {
//		Tree t = (Tree) a.getNode().getParent();
//		String type = t.getAstClass().getSimpleName();
//		if (StatementConstants.SWITCHSTATEMENT.equals(type)) {
//			return true;
//		}
//		return false;
//	}


//	public static boolean ifFatherNodeTypeEquals(Action a, String statementConstants) {
//		Tree t = (Tree) a.getNode().getParent();
//		String type = t.getAstClass().getSimpleName();
//
//		if (type.equals(statementConstants)) {
//			return true;
//		}
//		return false;
//	}





//	public static String fatherStatement(Action a) {
//		Tree t = (Tree) a.getNode().getParent();
//		String type = t.getAstClass().getSimpleName();
//		return type;
//	}

	// public static boolean isAllChildrenNew(List<ITree> list) {
	// boolean allNewChildren = true;
	// for (ITree a : list) {
	// Tree t = (Tree) a;
	// if (t.getDoAction() == null) {
	// System.err.println("Unexpected Condition XXX");
	// } else if (t.getDoAction() instanceof Insert) {
	//
	// } else {
	// // update 或者move 操作 说明if语句内部有原语句
	// allNewChildren = false;
	// }
	// }
	// return allNewChildren;
	// }


//
//	public static boolean isNullCheck(ITree ifStatementNode) {
//		if (ifStatementNode.getChildren().size() == 2) {
//			Tree c1 = (Tree) ifStatementNode.getChild(0);
//			Tree c2 = (Tree) ifStatementNode.getChild(1);
//			String type = c2.getAstClass().getSimpleName();
//			if (StatementConstants.BLOCK.equals(type)) {
//				for (ITree tmp : c1.postOrder()) {
//					Tree tmp2 = (Tree) tmp;
//					if (StatementConstants.NULLLITERAL.equals(tmp2.getAstClass().getSimpleName())) {
//						return true;
//					}
//				}
//				return false;
//			} else {
//				System.err.println("Not a block Error if");
//			}
//		}
//		return false;
//	}




}
