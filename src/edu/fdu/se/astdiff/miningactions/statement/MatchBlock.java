package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
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
//        Tree parent = (Tree)a.getNode().getParent();
//        System.err.println(parent.getAstNode().getClass().getSimpleName());
//        Move move = (Move)a;
        Tree movedBlock = (Tree)a.getNode();
//        Tree nextParent =(Tree) move.getParent();
        List<ITree> children = movedBlock.getChildren();
        for(int i =0;i<children.size();i++){
            Tree childd = (Tree) children.get(i);
            ClusteredActionBean clusteredActionBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,null,null,null,childd,ClusteredActionBean.SRC_TREE_NODE);
            ChangeEntity changeEntity = new StatementPlusChangeEntity(clusteredActionBean);
            changeEntity.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
            changeEntity.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
            changeEntity.stageIIBean.setChangeEntity(childd.getAstNode().getClass().getSimpleName());
            changeEntity.stageIIBean.setOpt(ChangeEntityDesc.StageIIIOpt.OPT_MOVE);
            changeEntity.stageIIBean.setLineRange(AstRelations.getMyRange(childd, ClusteredActionBean.SRC_TREE_NODE).toString());
            fp.addOneChangeEntity(changeEntity);
        }
    }
    // gumtree 原先识别的问题

}
