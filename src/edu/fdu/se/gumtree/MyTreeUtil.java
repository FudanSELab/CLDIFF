package edu.fdu.se.gumtree;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

public class MyTreeUtil{
	/**
     * Returns a list of every subtrees and the tree ordered using a breadth-first order.
     * @param tree a Tree.
     */
    public static List<ITree> layeredBreadthFirst(ITree root,List<Integer> layerIndex) {
        List<ITree> trees = new ArrayList<>();
        List<ITree> currents = new ArrayList<>();
        currents.add(root);
        int queueCounter=0;
        int currentLayerCountDown=1;
        int nextLayerChildrenSum=0;
        while (currents.size() > 0) {
        	queueCounter++;
        	currentLayerCountDown--;
        	
            ITree c = currents.remove(0);
            nextLayerChildrenSum += c.getChildren().size();
            if(currentLayerCountDown==0){
        		layerIndex.add(queueCounter);
        		currentLayerCountDown = nextLayerChildrenSum;
        		nextLayerChildrenSum = 0;
        	}
            trees.add(c);
            currents.addAll(c.getChildren());
        }
        return trees;
    }
    
    public static List<Action> traverseNodeGetSameEditActions(Action a){
    	List<Action> result = new ArrayList<Action>();
    	ITree node = a.getNode();
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) continue;
    		if(myTree.getDoAction().getClass().equals(a.getClass())){
    			result.add(myTree.getDoAction());
    		}
    	}
    	return result;
    }
    
    public static List<Action> traverseNodeChildrenGetSameEditAction(Action a){
    	List<Action> result = new ArrayList<Action>();
    	ITree node  = a.getNode();
    	ITree parent = node.getParent();
    	int pos = parent.getChildPosition(node);
    	if(pos <= 1) return result;
    	for(int i=pos;i<parent.getChildren().size();i++){
    		Tree myTree = (Tree) parent.getChildren().get(i);
    		if(myTree.getDoAction()==null) continue;
    		if(myTree.getDoAction().getClass().equals(a.getClass())){
    			result.add(myTree.getDoAction());
    		}
    	}
    	return result;
    }

}
