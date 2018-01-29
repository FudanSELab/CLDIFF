package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/29.
 *
 */
public class DefaultDownUpTraversal extends BasicTreeTraversal{

    public static int traverseFafather(Action a,Tree node, List<Action> result1){
        int flag = 1;
        for(ITree t:node.preOrder()){
            Tree tt = (Tree) t;
            if(tt.getDoAction()==null){
                flag = 0;
                continue;
            }
            List<Action> actions = tt.getDoAction();
            for(Action tmp:actions){
                if(a.getClass().equals(tmp.getClass())){
                    result1.add(tmp);
                }
            }
        }
        return flag;
    }


}
