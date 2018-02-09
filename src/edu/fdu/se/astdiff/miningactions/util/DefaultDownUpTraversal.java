package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/29.
 *
 */
public class DefaultDownUpTraversal extends BasicTreeTraversal{



    public static void traverseClassSignature(Tree node, List<Action> result1,ChangePacket changePacket){
        assert node.getDoAction() ==null;
        Set<String> type = new HashSet<>();
        type.add(ActionConstants.NULLACTION);
        List<ITree> children = node.getChildren();
        for(ITree child :children){
            Tree tmp = (Tree) child;
            if(tmp.getAstClass().getSimpleName().endsWith("Declaration")||tmp.getAstNode().getNodeType()== ASTNode.INITIALIZER){
                break;
            }
            traverseNode(tmp,result1,type);
        }
        changePacket.changeSet1 = type;


    }


}
