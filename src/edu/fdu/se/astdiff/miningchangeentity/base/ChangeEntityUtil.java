package edu.fdu.se.astdiff.miningchangeentity.base;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;

/**
 * Created by huangkaifeng on 2018/3/31.
 */
public class ChangeEntityUtil {

    public static boolean isTypeIIEntity(String entityName){
        switch(entityName){
            case ChangeEntityDesc.StageIIENTITY.ENTITY_IF_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_FOR_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_ENHANCED_FOR_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_WHILE_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_DO_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_SYNCHRONIZED_STMT:
                return true;
            default:break;
        }
        return false;
    }

    public static int checkEntityCode(ChangeEntity ce){
        if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD)
                && ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_MOVE)){
            return 1;
        }
        if(ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD)
                && (ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_INSERT)|| ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_DELETE))){
            if(isTypeIIEntity(ce.stageIIBean.getChangeEntity())){
                return 2;
            }
        }
        return 0;
    }

    public static boolean isMoveInWrapper(MiningActionData fp,ChangeEntity wrapper, ChangeEntity move){
        Integer[] range = null;
        if(wrapper.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_INSERT)){
            ITree dstITree = fp.getMappedDstOfSrcNode(move.clusteredActionBean.curAction.getNode());
            if(dstITree == null){
                return false;
            }
            Tree dstTree = (Tree) dstITree;
            range = dstTree.getRange();
        }else if(wrapper.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIIOpt.OPT_DELETE)){
            range = ((Tree)move.clusteredActionBean.curAction.getNode()).getRange();
        }else{
            return false;
        }
        Integer[] wrapperRange =((Tree) wrapper.clusteredActionBean.curAction.getNode()).getRange();
        if(wrapperRange[0]<=range[0] && wrapperRange[1]>=range[1]){
            return true;
        }
        return false;

    }


    public static void mergeMoveAndWrapper(ChangeEntity wrapper,ChangeEntity move){

        wrapper.stageIIBean.setSubEntity(ChangeEntityDesc.StageIIISub.SUB_CONDITION);
    }

}
