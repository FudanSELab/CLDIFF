package edu.fdu.se.base.miningactions.util;

import com.github.gumtreediff.tree.ITree;

import java.util.ArrayList;
import java.util.List;

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

}
