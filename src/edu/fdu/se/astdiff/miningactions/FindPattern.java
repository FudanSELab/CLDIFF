package edu.fdu.se.astdiff.miningactions;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

public class FindPattern {
	
	public FindPattern(TreeContext src,TreeContext dst){
		this.srcTC =src ;
		this.dstTC =dst;
	}
	
	
	public TreeContext srcTC;
	public TreeContext dstTC;
	
	public boolean isCheckNull(List<Action> mList){
		if(mList == null){
			return false;
		}
		Action head = mList.get(0);
		if(head instanceof Insert){
			Insert ins = (Insert)head;
			ITree insNode = ins.getNode();
			if(dstTC.getTypeLabel(insNode).equals("IfStatement")){
				System.err.println("aaa");
				ITree child = insNode.getChild(0);
				if(dstTC.getTypeLabel(child).equals("InfixExpression")){
					System.err.println("bbb");
				}
				for(ITree tmp:insNode.preOrder()){
					Tree t = (Tree) tmp;
//					if(t.getActionType())
//					System.out.println(dstTC.getTypeLabel(tmp));
				}
			}
		}
		return false;
	}

}
