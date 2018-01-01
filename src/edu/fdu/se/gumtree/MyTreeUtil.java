package edu.fdu.se.gumtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;

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
    
    /**
     * 遍历从MethodDeclaration 遍历下来的节点 添加有doAction引用的节点
     * 遍历node下的所有的孩子，把所有有action引用的添加到alleditaction ，返回值表示与action a相同修改类型的个数
     * @param a
     * @param node
     * @param allEditAction
     * @return
     */
    public static int traverseMethodSignatureChildrenWithoutBlock(Action a,ITree node,List<Action> allEditAction){
    	List<ITree> children = node.getChildren();
    	int len = children.size();
    	int count = 0;
    	//默认最后一个节点为block
    	for(int i=0;i<len-1;i++){
    		ITree child = children.get(i);
    		for(ITree item : child.postOrder()){
    			Tree myTree = (Tree) item;
    			if(myTree.getDoAction()==null) continue;
    			List<Action> nodeActions = myTree.getDoAction();
        		for(Action aTmp:nodeActions){
        			if(aTmp.getClass().equals(a.getClass())){        			
        				count++;
        			}
        			allEditAction.add(aTmp);
        		}
    		}
    	}
    	return count;
    }
    
    
    /**
     * 
     */
    
    /**
     * 传入参数为action，所有节点都标记了insert
     */
    final public static int TYPE1 = 1;
    /**
     * 传入参数为action，所有节点都标记了delete
     */
    final public static int TYPE2 = 2;
//    /**
//     * 传入参数为action，部分节点没有标记insert或者delete
//     */
//    final public static int TYPE3 = 3; 
    /**
     * 传入参数为action，节点标记为move+insert TODO Bug?
     */
    final public static int TYPE4 = 4;
    /**
     * 传入参数为action，节点标记为move+delete TODO bug?
     */
    final public static int TYPE5 = 5;
    
    /**
     * 传入参数为fafathernode， 
     */
    final public static int TYPE0 = 0;
    
    /**
     * 位置
     */
    final public static int TYPE_UNKNOWN = -1;
    
    
    /**
     * node为fafafather node的情况
     * @param node
     * @param result
     * @return
     */
    public static int traverseNodeGetAllEditActions(ITree node,List<Action> result){
    	boolean isNullExist = false;
    	Set<String> actionTypes = new HashSet<String>();
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) {
    			isNullExist = true;
    			continue;
    		}
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			result.add(aTmp);
    			actionTypes.add(aTmp.getClass().toString());
    		}
    	}
    	if(isNullExist == true){
    		return TYPE0;
    	}else{
    		System.err.println("ERROR");
    	}
    	return 0;
    }
    
    public static int traverseNodeGetAllEditActions(Action action,List<Action> result){
    	boolean isNullExist = false;
    	ITree node = action.getNode();
    	Set<String> actionTypes = new HashSet<String>();
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) {
    			isNullExist = true;
    			continue;
    		}
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			result.add(aTmp);
    			actionTypes.add(aTmp.getClass().toString());
    		}
    	}
    	if(isNullExist == true){
    		return 0;
    	}else{
    		if(actionTypes.size() == 1){
    			if(actionTypes.contains(ActionConstants.INSERT)){
    				return TYPE1;// insert all
    			}else if(actionTypes.contains(ActionConstants.DELETE)){
    				return TYPE2;//delete all
    			}
    		} else if(actionTypes.size() == 2){
    			if(actionTypes.contains(ActionConstants.INSERT)&&actionTypes.contains(ActionConstants.MOVE)){
    				return TYPE4; //insert wrapper
    			}else if(actionTypes.contains(ActionConstants.DELETE)&&actionTypes.contains(ActionConstants.MOVE)){
    				return TYPE5;// delete wrapper
    			}
    		} else {
    			return TYPE_UNKNOWN;
    		}
    	}
    	return Integer.MAX_VALUE;
    }
    




}
