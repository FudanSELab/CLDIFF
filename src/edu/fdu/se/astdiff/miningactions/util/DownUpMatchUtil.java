package edu.fdu.se.astdiff.miningactions.util;

import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.Set;

/**
 * Created by huangkaifeng on 2018/2/6.
 *
 */
public class DownUpMatchUtil {

    public static void setChangePacket(ClusteredActionBean bean){
        if(bean.changePacket.changeSet1==null){
            return;
        }else if(bean.changePacket.changeSet2 == null){
            setChangePacket(bean.changePacket,bean.changePacket.changeSet1);
        }else{
            //不存在
            setChangePacket(bean.changePacket,bean.changePacket.changeSet1,bean.changePacket.changeSet2);
        }

    }

    private static void setChangePacket(ChangePacket changePacket, Set<String> type1,Set<String> type2){

    }

    private static void setChangePacket(ChangePacket changePacket,Set<String> type1){
        if(BaseMatchUtil.twoItemInsertAndNullAction(type1)){
            changePacket.setOperationType(OperationTypeConstants.INSERT);
        }else if(BaseMatchUtil.twoItemDeleteAndNullAction(type1)){
            changePacket.setOperationType(OperationTypeConstants.DELETE);
        }else if(BaseMatchUtil.twoItemMoveAndNullAction(type1)){
            changePacket.setOperationType(OperationTypeConstants.MOVE);
        }else if(BaseMatchUtil.twoItemUpdateAndNullAction(type1)){
            changePacket.setOperationType(OperationTypeConstants.UPDATE);
        }
        changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_REFURNISH);

    }
}
