package edu.fdu.se.gumtree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.StatementConstants;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

public class MyTreeUtil{
	/**
     * Returns a list of every subtrees and the tree ordered using a breadth-first order.
     * @param root a Tree.
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
    public static Set<String> traverseMethodSignatureChildrenWithoutBlock(Action a,ITree node,List<Action> allEditAction){
		List<ITree> children = node.getChildren();
		int len = children.size();

		Set<String> actionTypes = new HashSet<String>();

		Tree nodeTree = (Tree)node;
		if(nodeTree.getDoAction()==null) {
			actionTypes.add(ActionConstants.NULLACTION);
		}
		//默认最后一个节点为block
		for(int i=0;i<len-1;i++){
			ITree child = children.get(i);
			for(ITree item : child.postOrder()){
				Tree myTree = (Tree) item;
				if(myTree.getDoAction()==null) {
					actionTypes.add(ActionConstants.NULLACTION);
					continue;
				}
				List<Action> nodeActions = myTree.getDoAction();
				for(Action aTmp:nodeActions){
//        			if(aTmp.getClass().equals(a.getClass())){
//        				count++;
//        			}
					allEditAction.add(aTmp);
					actionTypes.add(ActionConstants.getInstanceStringName(aTmp));
				}
			}
		}
		return actionTypes;
	}

	public static Set<String> traverseFirstChildren(ITree node,List<Action> allEditAction){
		List<ITree> children = node.getChildren();

		Set<String> actionTypes = new HashSet<String>();

		Tree nodeTree = (Tree)node;
		if(nodeTree.getDoAction()==null) {
			actionTypes.add(ActionConstants.NULLACTION);
		}
		//仅遍历第一个孩子节点
		for(int i=0;i<1;i++){
			ITree child = children.get(i);
			for(ITree item : child.postOrder()){
				Tree myTree = (Tree) item;
				if(myTree.getDoAction()==null) {
					actionTypes.add(ActionConstants.NULLACTION);
					continue;
				}
				List<Action> nodeActions = myTree.getDoAction();
				for(Action aTmp:nodeActions){
					allEditAction.add(aTmp);
					actionTypes.add(ActionConstants.getInstanceStringName(aTmp));
				}
			}
		}
		return actionTypes;
	}

	public static Set<String> traverseClassSignatureChildren(Action a, ITree node, List<Action> allEditAction){
		List<ITree> children = node.getChildren();
//		ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode(), con);
//		String ffFatherNodeType = con.getTypeLabel(fafafatherNode);

//		int len = children.size();
		int position = 0;
		boolean isContinue = true;
		while (isContinue && ( position < children.size() )){
//			String nodeType = con.getTypeLabel(children.get(position));
			Tree childNode = (Tree)children.get(position);
			String nodeType = childNode.getAstClass().getSimpleName();
			switch (nodeType){
				case StatementConstants.MODIFIER:
				case StatementConstants.SIMPLENAME:
				case StatementConstants.MARKERANNOTATION:
				case StatementConstants.NORMALANNOTATION:
				case StatementConstants.SINGLEMEMBERANNOTATION:
					position++;
					break;
				default:
					isContinue = false;
					break;
			}
		}

		Set<String> actionTypes = new HashSet<String>();
		Tree nodeTree = (Tree)node;
		if(nodeTree.getDoAction()==null) {
			actionTypes.add(ActionConstants.NULLACTION);
		}

		for(int i=0;i<position;i++){
			ITree child = children.get(i);
			for(ITree item : child.postOrder()){
				Tree myTree = (Tree) item;
				if(myTree.getDoAction()==null) {
					actionTypes.add(ActionConstants.NULLACTION);
					continue;
				}
				List<Action> nodeActions = myTree.getDoAction();
				for(Action aTmp:nodeActions){
//        			if(aTmp.getClass().equals(a.getClass())){
//        				count++;
//        			}
					allEditAction.add(aTmp);
					actionTypes.add(ActionConstants.getInstanceStringName(aTmp));
				}
			}
		}
		return actionTypes;
	}

    public static int getTypeCode(Action action,boolean isNullExist,Set<String> actionTypes){
    	if(action instanceof Insert){
    		if(isNullExist){
    			return OperationTypeConstants.INSERT_STATEMENT_WRAPPER;
    		}else{
    			return OperationTypeConstants.INSERT_STATEMENT_AND_BODY;
    		}
    	}
    	if(action instanceof Delete){
    		if(isNullExist||actionTypes.contains(ActionConstants.MOVE)){
    			return OperationTypeConstants.DELETE_STATEMENT_WRAPPER;
    		}else{
    			return OperationTypeConstants.DELETE_STATEMENT_AND_BODY;
    		}
    	}
		if(action instanceof Move){
			if(isNullExist){
				return OperationTypeConstants.MOVE_STATEMENT_WRAPPER;
			}else{
				return OperationTypeConstants.MOVE_STATEMENT_AND_BODY;
			}
		}
    	return OperationTypeConstants.UNKNOWN;
    }
    
    /**
     * node为fafafather node的情况
     * @param node
     * @param result
     * @return
     */
    public static Set<String> traverseNodeGetAllEditActions(ITree node,List<Action> result){
    	Set<String> actionTypes = new HashSet<String>();
    	for(ITree tmp:node.preOrder()){
    		Tree myTree = (Tree) tmp;
    		if(myTree.getDoAction()==null) {
				actionTypes.add(ActionConstants.NULLACTION);
    			continue;
    		}
    		List<Action> nodeActions = myTree.getDoAction();
    		for(Action aTmp:nodeActions){
    			result.add(aTmp);
    			actionTypes.add(ActionConstants.getInstanceStringName(aTmp));
    		}
    	}
		return actionTypes;
    }

	/**
	 * statement 节点
     * @return
     */
	public static int isSrcOrDstAdded(Set<String> srcTypes,Set<String> dstTypes) {
		if(srcTypes.size()==1&&srcTypes.contains(ActionConstants.NULLACTION)){
			//src tree为null
			if(dstTypes.size() == 2 && dstTypes.contains(ActionConstants.INSERT)) {
				return OperationTypeConstants.INSERT_STATEMENT_CONDITION;
			} else{
				return OperationTypeConstants.UNKNOWN;
			}
		}
		if(dstTypes.size()==1&&dstTypes.contains(ActionConstants.NULLACTION)){
			//dst 为空
			if(srcTypes.contains(ActionConstants.NULLACTION)){
				if(srcTypes.size()==2){
					if(srcTypes.contains(ActionConstants.MOVE)) {
						return OperationTypeConstants.MOVE_STATEMENT_CONDITION;
					}else if(srcTypes.contains(ActionConstants.UPDATE)) {
						return OperationTypeConstants.UPDATE_STATEMENT_CONDITION;
					}else if(srcTypes.contains(ActionConstants.DELETE)){
						return OperationTypeConstants.DELETE_STATEMENT_CONDITION;
					}else{
						return OperationTypeConstants.UNKNOWN;
					}
				}else{
					return OperationTypeConstants.STATEMENT_CONDITION_OR_DECLARATION_MISC;
				}
			}else{
				return OperationTypeConstants.UNKNOWN;
			}

		}
    	return  OperationTypeConstants.STATEMENT_CONDITION_OR_DECLARATION_MISC;
	}

	/**
	 * statement节点
	 * @param action
	 * @param result
	 * @return
     */
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
    			actionTypes.add(ActionConstants.getInstanceStringName(aTmp));
    		}
    	}
    	return getTypeCode(action,isNullExist,actionTypes);
    }

	/**
	 * statment 节点
	 * @param action
	 * @param startIndex
	 * @param endIndex
	 * @param result
     * @return
     */
    public static int traverseNodeGetAllEditActions(Action action,int startIndex,int endIndex,List<Action> result){
    	boolean isNullExist = false;
    	ITree node = action.getNode();
    	Set<String> actionTypes = new HashSet<String>();
    	List<ITree> children = node.getChildren();
    	for(int i = startIndex;i<=endIndex;i++){
    		ITree temp = children.get(i);
    		for(ITree sub:temp.preOrder()){
    			Tree myTree = (Tree) sub;
        		if(myTree.getDoAction()==null) {
        			isNullExist = true;
        			continue;
        		}
        		List<Action> nodeActions = myTree.getDoAction();
        		for(Action aTmp:nodeActions){
        			result.add(aTmp);
        			actionTypes.add(ActionConstants.getInstanceStringName(aTmp));
        		}
    		}
    	}
    	return getTypeCode(action,isNullExist,actionTypes);
    }
    

}
