package edu.fdu.se.gumtree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.generatingactions.ActionConstants;

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
    public static boolean traverseMethodSignatureChildrenWithoutBlock(Action a,ITree node,List<Action> allEditAction){
    	List<ITree> children = node.getChildren();
    	int len = children.size();
//    	int count = 0;
		boolean isAdded = false;
		boolean isNullExist = false;
		Set<String> actionTypes = new HashSet<String>();
    	//默认最后一个节点为block
    	for(int i=0;i<len-1;i++){
    		ITree child = children.get(i);
    		for(ITree item : child.postOrder()){
    			Tree myTree = (Tree) item;
    			if(myTree.getDoAction()==null) {
					isNullExist = true;
					continue;
				}
    			List<Action> nodeActions = myTree.getDoAction();
        		for(Action aTmp:nodeActions){
//        			if(aTmp.getClass().equals(a.getClass())){
//        				count++;
//        			}
        			allEditAction.add(aTmp);
        			isAdded = true;
					actionTypes.add(aTmp.getClass().toString());
        		}
    		}
    	}
    	if(isAdded){
    		return TYPE_EXIST;
		}else{
    		return TYPE_NO_EXIST;
		}
    }
    

    
    /**
     * 传入参数为action，所有节点都标记了insert
     */
    final public static int TYPE1 = 1;
    final public static String OP_INSERT = "INSERT";
    /**
     * 传入参数为action，所有节点都标记了delete
     */
    final public static int TYPE2 = 2;
	final public static String OP_DELETE = "DELETE";
	/**
	 * 传入参数为action，节点标记为update
	 */
	final public static int TYPE3 = 3;
	final public static String OP_UPDATE = "UPDATE";

	/**
	 * 传入参数为action，节点标记为update
	 */
	final public static int TYPE4 = 4;
	final public static String OP_MOVE = "MOVE";
//    /**
//     * 传入参数为action，部分节点没有标记insert或者delete
//     */
//    final public static int TYPE3 = 3;
    /**
     * 传入参数为action，节点标记为move+insert TODO Bug?
     */
    final public static int TYPE5 = 5;
	final public static String OP_MOVE_INSERT = "INSERT + MOVE";
    /**
     * 传入参数为action，节点标记为move+delete TODO bug?
     */
    final public static int TYPE6 = 6;
	final public static String OP_MOVE_DELETE = "DELETE + MOVE";

    /**
     * 传入参数为fafathernode， 
     */
    final public static int TYPE_FATHERNODE = 0;
	final public static String OP_FATHERNODE = "FAFATHERNODE";

    /**
     * 未知
     */
    final public static int TYPE_UNKNOWN = -1;
	final public static String OP_UNKNOWN = "UNKNOWN";

	/**
	 * 用于标记传入fatherNode时，遍历src、dst树，都有节点增加，isAdded，BOTHTREE
	 */
	final public static int TYPE_FATHERNODE_BOTH = -2;
	final public static String OP_FATHERNODE_BOTH = "FAFATHERNODE_BOTH";

	/**
	 * 用于标记传入fatherNode时，遍历src、dst树，dst有节点增加，src没有，INSERT
	 */
	final public static int TYPE_FATHERNODE_INSERT= -3;
	final public static String OP_FATHERNODE_INSERT = "FAFATHERNODE_INSERT";

	/**
	 * 用于标记传入fatherNode时，遍历src、dst树，src有节点增加，dst没有，非insert
	 */
	final public static int TYPE_FATHERNODE_NOINSERT = -4;
	final public static String OP_FATHERNODE_NOINSERT = "FAFATHERNODE_NOINSERT";

	/**
	 * ERROR
	 */
	final public static String OP_ERROR = "NULL, ERROR";

	/**
	 * 标识是否两棵树都存在节点
	 */
	final public static boolean TYPE_EXIST = true;
	final public static boolean TYPE_NO_EXIST = false;

	public static String getTypeById(int typeId) {
		String operationTypeString;
		switch (typeId){
			case 1:
				operationTypeString = OP_INSERT;
				break;
			case 2:
				operationTypeString = OP_DELETE;
				break;
			case 3:
				operationTypeString = OP_UPDATE;
				break;
			case 4:
				operationTypeString = OP_MOVE;
				break;
			case 5:
				operationTypeString = OP_MOVE_INSERT;
				break;
			case 6:
				operationTypeString = OP_MOVE_DELETE;
				break;
			case 0:
				operationTypeString = OP_FATHERNODE;
				break;
			case -1:
				operationTypeString = OP_UNKNOWN;
				break;
			case -2:
				operationTypeString = OP_FATHERNODE_BOTH;
				break;
			case -3:
				operationTypeString = OP_FATHERNODE_INSERT;
				break;
			case -4:
				operationTypeString = OP_FATHERNODE_NOINSERT;
				break;
			default:
				operationTypeString = OP_ERROR;
				break;
		}
		return operationTypeString;
	}
    
    public static int getTypeCode(Action action,boolean isNullExist,Set<String> actionTypes){
    	if(action instanceof Insert){
    		if(isNullExist){
    			return TYPE5;
    		}else{
    			return TYPE1;
    		}
    	}
    	if(action instanceof Delete){
    		if(isNullExist||actionTypes.contains(ActionConstants.MOVE)){
    			return TYPE6;
    		}else{
    			return TYPE2;
    		}
    	}
		if(action instanceof Update){
			return TYPE3;
		}
    	return TYPE_UNKNOWN;
    }
    
    /**
     * node为fafafather node的情况
     * @param node
     * @param result
     * @return
     */
    public static boolean traverseNodeGetAllEditActions(ITree node,List<Action> result){
    	boolean isNullExist = false;
		boolean isAdded = false;
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
				isAdded = true;
    			actionTypes.add(aTmp.getClass().toString());
    		}
    	}
		if(isAdded){
			return TYPE_EXIST;
		}else{
			return TYPE_NO_EXIST;
		}
    }

	public static int isSrcorDstAdded(boolean srcExist,boolean dstExist) {
    	int status = TYPE_FATHERNODE;
    	if(srcExist && dstExist)
			status = TYPE_FATHERNODE_BOTH;
    	else if(srcExist && !dstExist)
			status =  TYPE_FATHERNODE_NOINSERT;
    	else if(!srcExist && dstExist)
			status =  TYPE_FATHERNODE_INSERT;

    	return  status;
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
    	return getTypeCode(action,isNullExist,actionTypes);
    }
    
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
        			actionTypes.add(aTmp.getClass().toString());
        		}
    		}
    	}
    	return getTypeCode(action,isNullExist,actionTypes);
    }
    

    




}
