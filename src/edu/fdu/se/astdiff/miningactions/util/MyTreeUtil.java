package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/25.
 *
 */
public class MyTreeUtil {
    /**
     * Returns a list of every subtrees and the tree ordered using a breadth-first order.
     *
     * @param root a Tree.
     */
    public static List<ITree> layeredBreadthFirst(ITree root, List<Integer> layerIndex) {
        List<ITree> trees = new ArrayList<>();
        List<ITree> currents = new ArrayList<>();
        currents.add(root);
        int queueCounter = 0;
        int currentLayerCountDown = 1;
        int nextLayerChildrenSum = 0;
        while (currents.size() > 0) {
            queueCounter++;
            currentLayerCountDown--;

            ITree c = currents.remove(0);
            nextLayerChildrenSum += c.getChildren().size();
            if (currentLayerCountDown == 0) {
                layerIndex.add(queueCounter);
                currentLayerCountDown = nextLayerChildrenSum;
                nextLayerChildrenSum = 0;
            }
            trees.add(c);
            currents.addAll(c.getChildren());
        }
        return trees;
    }



    public static Set<String> traverseClassSignatureChildren(ITree node, List<Action> allEditAction) {
        List<ITree> children = node.getChildren();
        Set<String> actionTypes = new HashSet<>();
        for(ITree t : children){
            Tree tree = (Tree) t;
            if(!tree.getAstClass().getSimpleName().endsWith("Declaration")){
                for (ITree item : tree.postOrder()) {
                    Tree myTree = (Tree) item;
                    if (myTree.getDoAction() == null) {
                        actionTypes.add(ActionConstants.NULLACTION);
                        continue;
                    }
                    List<Action> nodeActions = myTree.getDoAction();
                    for (Action aTmp : nodeActions) {
                        allEditAction.add(aTmp);
                        actionTypes.add(aTmp.getClass().getSimpleName());
                    }
                }

            }
        }
        return actionTypes;
    }

    public static int getTypeCode(Action action, boolean isNullExist, Set<String> actionTypes) {
        if (action instanceof Insert) {
            if (isNullExist) {
                return OperationTypeConstants.INSERT_STATEMENT_WRAPPER;
            } else {
                return OperationTypeConstants.INSERT_STATEMENT_AND_BODY;
            }
        }
        if (action instanceof Delete) {
            if (isNullExist || actionTypes.contains(ActionConstants.MOVE)) {
                return OperationTypeConstants.DELETE_STATEMENT_WRAPPER;
            } else {
                return OperationTypeConstants.DELETE_STATEMENT_AND_BODY;
            }
        }
        if (action instanceof Move) {
            if (isNullExist) {
                return OperationTypeConstants.MOVE_STATEMENT_WRAPPER;
            } else {
                return OperationTypeConstants.MOVE_STATEMENT_AND_BODY;
            }
        }
        return OperationTypeConstants.UNKNOWN;
    }

    /**
     * node为fafafather node的情况
     *
     * @param node
     * @param result
     * @return
     */
    public static Set<String> traverseNodeGetAllEditActions(ITree node,List<Action> result) {
        Set<String> actionTypes = new HashSet<>();
        for (ITree tmp : node.preOrder()) {
            Tree myTree = (Tree) tmp;
            if (myTree.getDoAction() == null) {
                actionTypes.add(ActionConstants.NULLACTION);
                continue;
            }
            List<Action> nodeActions = myTree.getDoAction();
            for (Action aTmp : nodeActions) {
                result.add(aTmp);
                actionTypes.add(aTmp.getClass().getSimpleName());
            }
        }
        return actionTypes;
    }

    /**
     * statement 节点
     *
     * @return
     */
    public static int isSrcOrDstAdded(Set<String> srcTypes, Set<String> dstTypes) {
        if (srcTypes.size() == 1 && srcTypes.contains(ActionConstants.NULLACTION)) {
            //src tree为null
            if (dstTypes.size() == 2 && dstTypes.contains(ActionConstants.INSERT)) {
                return OperationTypeConstants.INSERT_STATEMENT_CONDITION;
            } else {
                return OperationTypeConstants.UNKNOWN;
            }
        }
        if (dstTypes.size() == 1 && dstTypes.contains(ActionConstants.NULLACTION)) {
            //dst 为空
            if (srcTypes.contains(ActionConstants.NULLACTION)) {
                if (srcTypes.size() == 2) {
                    if (srcTypes.contains(ActionConstants.MOVE)) {
                        return OperationTypeConstants.MOVE_STATEMENT_CONDITION;
                    } else if (srcTypes.contains(ActionConstants.UPDATE)) {
                        return OperationTypeConstants.UPDATE_STATEMENT_CONDITION;
                    } else if (srcTypes.contains(ActionConstants.DELETE)) {
                        return OperationTypeConstants.DELETE_STATEMENT_CONDITION;
                    } else {
                        return OperationTypeConstants.UNKNOWN;
                    }
                } else {
                    return OperationTypeConstants.STATEMENT_CONDITION_OR_DECLARATION_MISC;
                }
            } else {
                return OperationTypeConstants.UNKNOWN;
            }
        }
        return OperationTypeConstants.STATEMENT_CONDITION_OR_DECLARATION_MISC;
    }








}
