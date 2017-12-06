package edu.fdu.se.astdiff.generatingactions;

import org.eclipse.jdt.core.dom.ASTNode;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

public class ConsolePrint {
	private static String tabs(int tabNum){
		String res = "";
		for(int i=0;i<tabNum;i++){
			res+="\t";
		}
		return res;
	}
	
	public static String printMyOneActionString(Action a,int tabNum,TreeContext dstTC){
		StringBuilder sb = new StringBuilder();
		String tabStr = tabs(tabNum);
		if(a instanceof Delete){
			sb.append(tabStr+"Delete > ");
			Delete delete = (Delete)a;
			ITree deleteNode = delete.getNode();
			sb.append(prettyString(dstTC,deleteNode)+" from "+prettyString(dstTC,deleteNode.getParent())+"\n");
			sb.append(delete.toString()+"\n");
		}
		if(a instanceof Insert){
			sb.append(tabStr+"Insert > ");
			Insert insert = (Insert)a;
			ITree insertNode = insert.getNode();
			sb.append(prettyString(dstTC,insertNode)+" to "+prettyString(dstTC,insertNode.getParent())+" at "+ insert.getPosition()+"\n");
		}
		if(a instanceof Move){
			sb.append(tabStr+"Move > ");
			Move move = (Move)a;
			ITree moveNode = move.getNode();
			sb.append(prettyString(dstTC,moveNode)+" to "+prettyString(dstTC,move.getParent())+" at "+ move.getPosition()+"\n");//old
		}
		if(a instanceof Update){
			sb.append(tabStr+"Update > ");
			Update update = (Update)a;
			ITree updateNode = update.getNode();
			sb.append("from "+updateNode.getLabel()+" to "+update.getValue()+"\n");//old
		}
		return sb.toString();
	}
	
	public static String prettyString(TreeContext con, ITree node){
		return node.getId()+". "+con.getTypeLabel(node)+":"+node.getLabel()+"("+getStartLineNum(con,node)+"-"+getEndLineNum(con,node)+")";
	}
	
    public static int getStartLineNum(TreeContext con, ITree node){
    	ASTNode n = ((Tree) node).getAstNode();
		return con.getCu().getLineNumber(n.getStartPosition());
	}
    public static int getEndLineNum(TreeContext con, ITree node){
    	ASTNode n = ((Tree) node).getAstNode();
		return con.getCu().getLineNumber(n.getStartPosition()+n.getLength()-1);
	}
    


}
