package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/26.
 */
public class DefaultOneTreeTraversal extends BasicTreeTraversal{

    public void traverseClassSignature(ITree node, List<Action> result1,Set<String> result2){
        List<ITree> list = node.getChildren();
        for(int i=0;i<list.size();i++){
            Tree t = (Tree)list.get(i);
            if(t.get)
        }
        traverseNode();
    }

}
