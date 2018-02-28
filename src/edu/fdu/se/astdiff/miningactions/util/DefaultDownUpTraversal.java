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

    public static void traverseMethodSignature(Tree node,List<Action> result1,ChangePacket changePacket){
        assert node.getDoAction() == null;
        Set<String> type = new HashSet<>();
        type.add(ActionConstants.NULLACTION);
        List<ITree> children = node.getChildren();
        for(ITree child:children){
            Tree tmp = (Tree) child;
            if(tmp.getAstNode().getNodeType() == ASTNode.BLOCK){
                break;
            }
            traverseNode(tmp,result1,type);
        }
        changePacket.changeSet1 = type;
    }

    /**
     * 最简单的从father node往下找edit 节点，然后标记
     * @param fafather root节点
     * @param editAction editAction
     * @param changePacket changePacket
     */
    public static void traverseFatherNodeGetSameNodeActions(Tree fafather,List<Action> editAction,ChangePacket changePacket){
        Set<String> type = new HashSet<>();
        for(ITree node:fafather.preOrder()){
            Tree tmp = (Tree) node;
            traverseNode(tmp,editAction,type);
        }
        changePacket.changeSet1 = type;
    }

    public static void traverseIfPredicate(Tree node,List<Action> result1,ChangePacket changePacket){
        assert node.getDoAction() == null;
        Set<String> type = new HashSet<>();
        type.add(ActionConstants.NULLACTION);
        List<ITree> children = node.getChildren();
        int i;
        for(i =0;i<children.size();i++){
            Tree tmp = (Tree) children.get(i);
            if(tmp.getAstNode().getNodeType() == ASTNode.BLOCK){
                break;
            }
        }
        int bound;
        if(i!=children.size()){
            bound=i;

        }else{
            bound=children.size()-1;
        }
        for(int j=0;j<bound;j++){
            Tree tmp = (Tree) children.get(j);
            traverseNode(tmp,result1,type);
        }
        changePacket.changeSet1 = type;
    }

    public static void traverseDoWhileCondition(Tree node,List<Action> result,ChangePacket changePacket){
        assert node.getDoAction() == null;
        assert node.getChildren().size()==2;
        Set<String> type = new HashSet<>();
        Tree secondChild = (Tree) node.getChild(1);
        traverseNode(secondChild,result,type);
        changePacket.changeSet1 = type;
    }


}
