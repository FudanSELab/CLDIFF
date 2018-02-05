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
        if(bean.changePacket.changeSet2 == null){
            setChangePacket(bean.changePacket,bean.changePacket.changeSet1);
        }else{
            setChangePacket(bean.changePacket,bean.changePacket.changeSet1,bean.changePacket.changeSet2);
        }
    }

    public static void setChangePacket(ChangePacket changePacket, Set<String> types1, Set<String> types2){
//        if(types1.size() == 1){
//            if(types1.contains(ActionConstants.INSERT)||types1.contains(ActionConstants.DELETE)){
//                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE);
//            }
//        }else if(types1.size()==2){
//            if(types1.contains(ActionConstants.MOVE)&&types1.contains(ActionConstants.NULLACTION)){
//                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE);
//            }
//        }

    }

    public static void setChangePacket(ChangePacket changePacket, Set<String> types1){
        if(types1.size() == 1){
            if(types1.contains(ActionConstants.INSERT)||types1.contains(ActionConstants.DELETE)){
                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE);
            }
        }else if(types1.size()==2){
            if(types1.contains(ActionConstants.MOVE)&&types1.contains(ActionConstants.NULLACTION)){
                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE);
            }
        }

    }
}
