package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

/**
 * Created by huangkaifeng on 2018/1/25.
 */
public class MatchUtil {

    public static void setChangePacketOperationType(Action a, ChangePacket changePacket){
        if(a instanceof Insert){
            changePacket.setOperationType(OperationTypeConstants.INSERT);
        }else if(a instanceof Delete){
            changePacket.setOperationType(OperationTypeConstants.DELETE);
        }else if(a instanceof Move){
            changePacket.setOperationType(OperationTypeConstants.MOVE);
        }else {
            changePacket.setOperationType(OperationTypeConstants.UPDATE);
        }
    }



}
