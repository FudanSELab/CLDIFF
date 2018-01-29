package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import org.eclipse.jdt.core.dom.ASTNode;

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











}
