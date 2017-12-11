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
    /**
     * a的所有直接非直接孩子，并且标记同样的action操作
     * @param a
     * @return
     */
    public static List<Action> traverseActionNodeGetSameEditActions(Action a){
    	List<Action> result = new ArrayList<Action>();
    	ITree node = a.getNode();
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) continue;
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			if(aTmp.getClass().equals(a.getClass())){
    				result.add(aTmp);
    			}
    		}
    	}
    	return result;
    }
    
    /**
     * a的所有直接非直接孩子，并且标记同样的action操作
     * @param a
     * @return
     */
    public static List<Action> traverseNodeGetSameEditActions(Action a,ITree node){
    	List<Action> result = new ArrayList<Action>();
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) continue;
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			if(aTmp.getClass().equals(a.getClass())){
    				result.add(aTmp);
    			}
    		}
    	}
    	return result;
    }
    /**
     * a的直接孩子，而且是标记同样action操作
     * @param a
     * @return
     */
    public static List<Action> traverseNodeChildrenGetSameEditAction(Action a){
    	List<Action> result = new ArrayList<Action>();
    	ITree node  = a.getNode();
    	ITree parent = node.getParent();
    	int pos = parent.getChildPosition(node);
    	if(pos <= 1) return result;
    	for(int i=pos;i<parent.getChildren().size();i++){
    		Tree myTree = (Tree) parent.getChildren().get(i);
    		if(myTree.getDoAction()==null) continue;
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			if(aTmp.getClass().equals(a.getClass())){
    				result.add(aTmp);
    			}
    		}
    	}
    	return result;
    }
    /**
     * 返回值表示是否所有节点都是相同action 
     * result变量返回action集合
     * @param a
     * @param result
     * @return
     */
    public static boolean traverseAllChilrenCheckIfSameAction(Action a,List<Action> result){
    	if(result == null){
    		System.err.println("ERR");
    	}
    	ITree node = a.getNode();
    	boolean isAllSameAction = true;
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) continue;
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			if(aTmp.getClass().equals(a.getClass())){
    				result.add(aTmp);
    			}else{
    				isAllSameAction = false;
    			}
    		}
    	}
    	return isAllSameAction;
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
//    			if(a.getClass().equals(myTree.getDoAction().getClass())){
//    				count++;
//    			}
//        		allEditAction.add(myTree.getDoAction());
    		}
    	}
    	
    	return count;
    }
    
    /**
     * Node下所有孩子，只要是被标记action的都加入list
     * @param node
     * @return
     */
    public static List<Action> traverseNodeGetAllEditAction(ITree node){
    	List<Action> result = new ArrayList<Action>();
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) continue;
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			result.add(aTmp);
    		}
    	}
    	return result;
    }
}
