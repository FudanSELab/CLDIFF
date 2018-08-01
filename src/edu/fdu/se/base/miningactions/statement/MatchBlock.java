package edu.fdu.se.base.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.miningactions.bean.ChangePacket;
import edu.fdu.se.base.miningactions.bean.MiningActionData;
import edu.fdu.se.base.miningactions.util.AstRelations;
import edu.fdu.se.base.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.base.StatementPlusChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class MatchBlock {

    public static void matchBlock(MiningActionData fp, Action a) {

        Tree fatherNode = (Tree) a.getNode().getParent();
        int type = fatherNode.getAstNode().getNodeType();
        if (a instanceof Move && type != ASTNode.IF_STATEMENT) {
            handleMoveOnBlock(fp, a);
            fp.setActionTraversedMap(a);
            return;
        }
        switch (type) {
            case ASTNode.SWITCH_STATEMENT:
//                MatchSwitch.matchSwitchCaseNewEntity(fp,a);
                fp.setActionTraversedMap(a);
                break;
            case ASTNode.IF_STATEMENT:
                //Pattern 1.2 Match else
                if (fatherNode.getChildPosition(a.getNode()) == 2) {
                    MatchIfElse.matchElse(fp, a);
                }
                fp.setActionTraversedMap(a);
                break;
            case ASTNode.TRY_STATEMENT:
                ////Finally块
                if (fatherNode.getChildPosition(a.getNode()) == fatherNode.getChildren().size() - 1) {
                    MatchTry.matchFinally(fp, a);
                }
                fp.setActionTraversedMap(a);
                break;
            default:
                fp.setActionTraversedMap(a);
                break;
        }
    }

    public static void handleMoveOnBlock(MiningActionData fp, Action a) {
        System.err.println("[WARNING]Move block");
        Tree movedBlock = (Tree) a.getNode();
//        List<ITree> children = movedBlock.getChildren();
//        for(int i =0;i<children.size();i++){
//            Tree childd = (Tree) children.get(i);
//            if(fp.getEntityByNode(childd)!=null){
//                continue;
//            }
//            if(childd.getDoAction()!=null && childd.getDoAction().size()!=0){
//                continue;
//            }
        ChangePacket changePacket = new ChangePacket();
        changePacket.initChangeSet1();
        List<Action> subActions = new ArrayList<>();
        subActions.add(a);
        ClusteredActionBean clusteredActionBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN, a, subActions, changePacket, movedBlock, ChangeEntityDesc.StageITreeType.SRC_TREE_NODE);
        ChangeEntity changeEntity = new StatementPlusChangeEntity(clusteredActionBean);
        changeEntity.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        changeEntity.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        changeEntity.stageIIBean.setChangeEntity(movedBlock.getAstNode().getClass().getSimpleName());
        changeEntity.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_MOVE);
        changeEntity.stageIIBean.setLineRange(AstRelations.getMyRange(movedBlock, ChangeEntityDesc.StageITreeType.SRC_TREE_NODE).toString());
        fp.addOneChangeEntity(changeEntity);
//        }
    }

}
