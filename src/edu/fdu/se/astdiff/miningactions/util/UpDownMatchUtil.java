package edu.fdu.se.astdiff.miningactions.util;


import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.Set;

/**
 * Created by huangkaifeng on 2018/2/5.
 *
 */
public class UpDownMatchUtil {

    public static void setChangePacket(ClusteredActionBean bean){
        if(bean.changePacket.changeSet1 == null){
            return;
        }else if(bean.changePacket.changeSet2 == null){
            setChangePacket(bean.changePacket,bean.changePacket.changeSet1);
        }else{
            setChangePacket(bean.changePacket,bean.changePacket.changeSet1,bean.changePacket.changeSet2);
        }
    }

    private static void setChangePacket(ChangePacket changePacket, Set<String> types1, Set<String> types2){
        if(BaseMatchUtil.oneItemInsert(types1)&& BaseMatchUtil.oneItemInsert(types2)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        }else if(BaseMatchUtil.oneItemDelete(types1)&& BaseMatchUtil.oneItemDelete(types2)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        }else if(BaseMatchUtil.oneItemMoveOrTwoItemMoveAndNullAction(types1)&& BaseMatchUtil.oneItemNullAction(types2)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        }else if(BaseMatchUtil.oneItemInsert(types1)&& BaseMatchUtil.oneItemNullAction(types2)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_WRAPPER);
        }else if(BaseMatchUtil.oneItemDelete(types1)&& BaseMatchUtil.oneItemMoveOrTwoItemMoveAndNullAction(types2)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_WRAPPER);
        }
    }

    private static void setChangePacket(ChangePacket changePacket, Set<String> types1){
        if(BaseMatchUtil.oneItemInsert(types1)|| BaseMatchUtil.oneItemDelete(types1)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        }else if(BaseMatchUtil.oneItemMoveOrTwoItemMoveAndNullAction(types1)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        }else if(BaseMatchUtil.oneItemMoveOrTwoItemMoveAndNullAction(types1)){
            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        }


    }


}
