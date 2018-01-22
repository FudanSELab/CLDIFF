package edu.fdu.se.astdiff.miningactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

public class AstRelations {
    public static ClusteredActionBean matchByNode(MiningActionData fp, Action a, String nodeType,String operationEntity){
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);
		TreeContext con = null;
		if (a instanceof Insert) {
			con = fp.getDstTree();
		} else{
			con = fp.getSrcTree();
		}
		Range nodeLinePosition = getnodeLinePosition(a,con);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,nodeLinePosition,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }

    public static ClusteredActionBean matchByFafafatherNode(MiningActionData fp, Action a, String nodeType,String operationEntity, ITree fafafatherNode, String ffFatherNodeType){
        ITree srcParent = null;
        List<Action> allActions = new ArrayList<Action>();
        ITree srcfafafather = null;
        ITree dstfafafather = null;
		TreeContext con = null;

        if (a instanceof Insert) {
			con = fp.getDstTree();
            dstfafafather = fafafatherNode;
            srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
            if (srcfafafather == null) {
                System.err.println("err null mapping");
            }
        } else {
			con = fp.getSrcTree();
            srcfafafather = fafafatherNode;
            dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
            if (dstfafafather == null) {
                System.err.println("err null mapping");
            }
        }

        Set<String> src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
        Set<String> dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
        int status = MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);

        fp.setActionTraversedMap(allActions);

		Range nodeLinePosition = getnodeLinePosition(a,con);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }

    public static Range getnodeLinePosition(Action a, TreeContext treeContext){
		int startLineNum = ConsolePrint.getStartLineNum(treeContext, a.getNode());
		int endLineNum = ConsolePrint.getEndLineNum(treeContext, a.getNode());
		Range linePosition = new Range(new Position(startLineNum,1),new Position(endLineNum,1));
		return linePosition;
	}

	public static boolean isFatherIfStatement(Action a, TreeContext treeContext) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());
		if (StatementConstants.IFSTATEMENT.equals(type)) {
			return true;
		}
		return false;
	}

	public static boolean isChildContainIfStatement(Action a, TreeContext treeContext) {
		List<ITree> child = a.getNode().getChildren();
		if (child.size() < 1) {
			System.err.println("There is no child");
			return false;
		}
		for (ITree tmp : child) {
			Tree t = (Tree) tmp;
			String type = treeContext.getTypeLabel(t);
			if (StatementConstants.IFSTATEMENT.equals(type)) {
				return true;
			}
		}
		return false;
	}

	public static ITree isChildContainVariableDeclarationFragment(ITree node, TreeContext treeContext) {
		List<ITree> child = node.getChildren();
		if (child.size() < 1) {
			System.err.println("There is no child");
			return null;
		}
		for (ITree tmp : child) {
			Tree t = (Tree) tmp;
			String type = treeContext.getTypeLabel(t);
			if (StatementConstants.VARIABLEDECLARATIONFRAGMENT.equals(type)) {
				return tmp;
			}
		}
		return null;
	}

	public static boolean isFatherTryStatement(Action a, TreeContext treeContext) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());
		if (StatementConstants.TRYSTATEMENT.equals(type)) {
			return true;
		}
		return false;
	}

	public static boolean isFatherSwitchStatement(Action a, TreeContext treeContext) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());
		if (StatementConstants.SWITCHSTATEMENT.equals(type)) {
			return true;
		}
		return false;
	}

	public static boolean isChildCotainSynchronizedStatement(Action a, TreeContext treeContext) {
		// Tree t = (Tree) a.getNode();
		List<ITree> child = a.getNode().getChildren();
		if (child.size() < 1) {
			System.err.println("There is no child");
			return false;
		}
		for (ITree tmp : child) {
			Tree t = (Tree) tmp;
			String type = treeContext.getTypeLabel(t);
			if (StatementConstants.SYNCHRONIZEDSTATEMENT.equals(type)) {
				return true;
			}
		}
		return false;
	}

	public static boolean ifFatherNodeTypeSameAs(Action a, TreeContext treeContext, String statementConstants) {
		Tree t = (Tree) a.getNode();
		String type = treeContext.getTypeLabel(t.getParent());

		if (type.equals(statementConstants)) {
			return true;
		}
		return false;
	}

	public static String fatherStatement(Action a, TreeContext con) {
		Tree t = (Tree) a.getNode();
		String type = con.getTypeLabel(t.getParent());
		return type;
	}

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

	public static boolean isClassCreation(List<Action> list, TreeContext treeContext) {
		for (Action a : list) {
			String str = treeContext.getTypeLabel(a.getNode());
			if (StatementConstants.CLASSINSTANCECREATION.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNullCheck(ITree ifStatementNode, TreeContext treeContext) {
		if (ifStatementNode.getChildren().size() == 2) {
			Tree c1 = (Tree) ifStatementNode.getChild(0);
			Tree c2 = (Tree) ifStatementNode.getChild(1);
			String type = treeContext.getTypeLabel(c2);
			if (StatementConstants.BLOCK.equals(type)) {
				for (ITree tmp : c1.postOrder()) {
					if (StatementConstants.NULLLITERAL.equals(treeContext.getTypeLabel(tmp))) {
						return true;
					}
				}
				return false;
			} else {
				System.err.println("Not a block Error if");
			}
		}
		return false;
	}

	/**
	 * 找当前节点的父节点 XXXStatement XXXDelclaration JavaDoc CatchClause
	 * 
	 * @param node
	 * @param treeContext
	 * @return 返回fafafather
	 */
	public static ITree findFafafatherNode(ITree node, TreeContext treeContext) {
		String type = null;
		ITree curNode = node;
		while (!curNode.isRoot()) {
			type = treeContext.getTypeLabel(curNode);
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
						curNode = curNode.getParent();
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

	/**
	 * 根据所传statementType的值，找符合条件的父节点并返回
	 * 
	 * @param node
	 * @param treeContext
	 * @return 返回fafafather
	 */
	public static ITree findFafafatherNodeByStatementType(ITree node, TreeContext treeContext, String statementType) {
		// CatchClause
		String type = null;
		ITree curNode = node;
		String returnType = null;
		while (true) {
			type = treeContext.getTypeLabel(curNode);
			if (type.endsWith(statementType)) {
				returnType = type;
				break;
			} else {
				curNode = curNode.getParent();
			}
		}
		return curNode;
	}

}
