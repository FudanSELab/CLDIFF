package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
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

//    public static void traverseClass(Action a, List<Action> result1, ChangePacket changePacket,Set<String> changeType1,Set<String> changeType2S){
//        ITree node = a.getNode();
//        result1.add(a);
//        List<ITree> children = node.getChildren();
//        int i=0;
//        for(;i<children.size();i++){
//            Tree t = (Tree) children.get(i);
//            if(t.getAstClass().getSimpleName().endsWith("Block")){
//                break;
//            }
//        }
//        Set<String> type1 = new HashSet<>();
//        Set<String> type2 = new HashSet<>();
//        type1.add(a.getClass().getSimpleName());
//        traverseNodeInRange(node,0,i-1,result1,type1);
//        traverseNodeInRange(node,i,children.size()-1,result1,type2);
//        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CLASS);
//        MatchUtil.setChangePacket(changePacket,type1,type2);
//    }


}
