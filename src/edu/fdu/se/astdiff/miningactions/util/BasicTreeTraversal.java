package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;

import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/26.
 */
public class BasicTreeTraversal {

    /**
     * traverseNode
     * @param tree
     * @param resultActions
     * @param resultTypes
     */
    public void traverseNode(ITree tree, List<Action> resultActions, Set<String> resultTypes){
        for(ITree node:tree.preOrder()){
            Tree tmp = (Tree)node;
            if(tmp.getDoAction()==null){
                resultTypes.add(ActionConstants.NULLACTION);
            }else{
                tmp.getDoAction().forEach(a -> {
                    resultActions.add(a);
                    resultTypes.add(a.getClass().getSimpleName());
                });
            }
        }
    }
}
