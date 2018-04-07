package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.StatementPlusChangeEntity;
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
        System.err.println("[WARNING]Move block");
        Tree movedBlock = (Tree)a.getNode();
        List<ITree> children = movedBlock.getChildren();
        for(int i =0;i<children.size();i++){
            Tree childd = (Tree) children.get(i);
            if(fp.getEntityByNode(childd)!=null){
                continue;
            }
            ChangePacket changePacket = new ChangePacket();
            changePacket.initChangeSet1();
            List<Action> subActions = new ArrayList<>();
            ClusteredActionBean clusteredActionBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,new Move(childd,null,0),subActions,changePacket,childd,ChangeEntityDesc.StageITreeType.SRC_TREE_NODE);
            ChangeEntity changeEntity = new StatementPlusChangeEntity(clusteredActionBean);
            changeEntity.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
            changeEntity.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
            changeEntity.stageIIBean.setChangeEntity(childd.getAstNode().getClass().getSimpleName());
            changeEntity.stageIIBean.setOpt(ChangeEntityDesc.StageIIIOpt.OPT_MOVE);
            changeEntity.stageIIBean.setLineRange(AstRelations.getMyRange(childd, ChangeEntityDesc.StageITreeType.SRC_TREE_NODE).toString());
            fp.addOneChangeEntity(changeEntity);
        }
    }

}
