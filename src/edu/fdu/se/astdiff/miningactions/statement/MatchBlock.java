package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

public class MatchBlock {

    public static void matchBlock(MiningActionData fp, Action a) {
        if(a instanceof Move){
            handleMoveOnBlock(fp,a);
            return;
        }
        Tree fatherNode = (Tree)a.getNode().getParent();
        int type = fatherNode.getAstNode().getNodeType();

        switch (type) {
            case ASTNode.SWITCH_STATEMENT:
//                MatchSwitch.matchSwitchCaseNewEntity(fp,a);
                fp.setActionTraversedMap(a);
                break;
            case ASTNode.IF_STATEMENT:
                //Pattern 1.2 Match else
                MatchIfElse.matchElse(fp, a);
                break;
            case ASTNode.TRY_STATEMENT:
                ////Finally块
                if(fatherNode.getChildPosition(a.getNode()) == fatherNode.getChildren().size()-1){
                    MatchTry.matchFinally(fp, a);
                }
                break;
            default:
                break;
        }
    }

    public static void handleMoveOnBlock(MiningActionData fp,Action a){
        System.err.println("Move block");
        Tree moveTree = (Tree) a.getNode();
        List<ITree> children = moveTree.getChildren();
        for(ITree child:children){
            Tree childd = (Tree) child;
            // 标记被move的节点的children节点
            // 分别有change entity 封装
        }
        // clear 原先的move节点
    }
    // gumtree 原先识别的问题

}
