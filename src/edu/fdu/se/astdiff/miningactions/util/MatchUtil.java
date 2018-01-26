package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/25.
 *
 */
public class MatchUtil {

    public static void setChangePacket(ChangePacket changePacket, Set<String> types){
        if(types.contains(ActionConstants.INSERT)){
            if(types.contains(ActionConstants.NULLACTION)){
                changePacket.setOperationType(OperationTypeConstants.INSERT);
            }else{
                changePacket.setOperationType(OperationTypeConstants.INSERT);
            }
        } else{

        }
        if(a instanceof Insert){
        }else if(a instanceof Delete){
            changePacket.setOperationType(OperationTypeConstants.DELETE);
        }else if(a instanceof Move){
            changePacket.setOperationType(OperationTypeConstants.MOVE);
        }else {
            changePacket.setOperationType(OperationTypeConstants.UPDATE);
        }

    }

    /**
     * down up
     * @param changePacket
     * @param srcTypes
     * @param dstTypes
     */
    public static void setChangePackgeDownUpOperationType(ChangePacket changePacket,Set<String> srcTypes,Set<String> dstTypes){
        if (srcTypes.size() == 1 && srcTypes.contains(ActionConstants.NULLACTION)) {
            //src tree为null
            if (dstTypes.size() == 2 && dstTypes.contains(ActionConstants.INSERT)) {
                changePacket.setOperationType(OperationTypeConstants.INSERT);
            } else {
                changePacket.setOperationType(OperationTypeConstants.UNKNOWN);
            }
            return;
        }
        if (dstTypes.size() == 1 && dstTypes.contains(ActionConstants.NULLACTION)) {
            //dst 为空
            if (srcTypes.contains(ActionConstants.NULLACTION)) {
                if (srcTypes.size() == 2) {
                    if (srcTypes.contains(ActionConstants.MOVE)) {
                        changePacket.setOperationType(OperationTypeConstants.MOVE);
                    } else if (srcTypes.contains(ActionConstants.UPDATE)) {
                        changePacket.setOperationType(OperationTypeConstants.UPDATE);
                    } else if (srcTypes.contains(ActionConstants.DELETE)) {
                        changePacket.setOperationType(OperationTypeConstants.DELETE);
                    }
                    return;
                } else {
                    changePacket.setOperationType(OperationTypeConstants.MULTIPLE_EDIT);
                }
            } else {
                changePacket.setOperationType(OperationTypeConstants.UNKNOWN);
            }
            return;

        }
        changePacket.setOperationType(OperationTypeConstants.MULTIPLE_EDIT);
        return ;

    }

    public static void setChangePackgeUpDownOperationType(ChangePacket changePacket,Set<String> actionTypes){
        if (srcTypes.size() == 1 && srcTypes.contains(ActionConstants.NULLACTION)) {
            //src tree为null
            if (dstTypes.size() == 2 && dstTypes.contains(ActionConstants.INSERT)) {
                changePacket.setOperationType(OperationTypeConstants.INSERT);
            } else {
                changePacket.setOperationType(OperationTypeConstants.UNKNOWN);
            }
            return;
        }
        if (dstTypes.size() == 1 && dstTypes.contains(ActionConstants.NULLACTION)) {
            //dst 为空
            if (srcTypes.contains(ActionConstants.NULLACTION)) {
                if (srcTypes.size() == 2) {
                    if (srcTypes.contains(ActionConstants.MOVE)) {
                        changePacket.setOperationType(OperationTypeConstants.MOVE);
                    } else if (srcTypes.contains(ActionConstants.UPDATE)) {
                        changePacket.setOperationType(OperationTypeConstants.UPDATE);
                    } else if (srcTypes.contains(ActionConstants.DELETE)) {
                        changePacket.setOperationType(OperationTypeConstants.DELETE);
                    }
                    return;
                } else {
                    changePacket.setOperationType(OperationTypeConstants.MULTIPLE_EDIT);
                }
            } else {
                changePacket.setOperationType(OperationTypeConstants.UNKNOWN);
            }
            return;

        }
        changePacket.setOperationType(OperationTypeConstants.MULTIPLE_EDIT);
        return;

    }






}
