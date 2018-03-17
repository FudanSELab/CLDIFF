package edu.fdu.se.astdiff.miningactions.util;


import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class AstRelations {




	public static boolean isFatherXXXStatement(Action a,int astNodeType) {
		Tree parentTree = (Tree) a.getNode().getParent();
		int type = parentTree.getAstNode().getNodeType();
		if (astNodeType == type) {
			return true;
		}
		return false;
	}

	public static boolean isFatherXXXStatement(Tree node,int astNodeType){
		int type =((Tree) node.getParent()).getAstNode().getNodeType();
		if (astNodeType == type) {
			return true;
		}
		return false;
	}


	public static CompilationUnit cuPrev;
	public static CompilationUnit cuCurr;

	public static MyRange getMyRange(Tree tree, int treeType){
		int start = tree.getAstNode().getStartPosition();
		int end = tree.getAstNode().getStartPosition() + tree.getAstNode().getLength();
		MyRange myRange = null;
		if(treeType == ClusteredActionBean.SRC_TREE_NODE){
			int s = cuPrev.getLineNumber(start);
			int e = cuPrev.getLineNumber(end);
			myRange = new MyRange(s,e,ClusteredActionBean.SRC_TREE_NODE);
		}else{
			int s = cuCurr.getLineNumber(start);
			int e = cuCurr.getLineNumber(end);
			myRange = new MyRange(s,e,ClusteredActionBean.DST_TREE_NODE);
		}
		return myRange;
	}





}
