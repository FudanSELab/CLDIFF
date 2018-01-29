package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
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

    public static void setChangePacket(ChangePacket changePacket, Set<String> types1,Set<String> types2){
        if(types1.contains(ActionConstants.INSERT)){
            changePacket.setOperationType(OperationTypeConstants.INSERT);
            if(types1.size() ==1 && types2.size()==1 &&types2.contains(ActionConstants.INSERT)){
                // ins + ins
                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
            }else if(types1.size() ==1 && types2.size()==1 &&types2.contains(ActionConstants.NULLACTION)){
                // ins + null
//                changePacket.setOperationSubEntity();
            }else if(types1.size() ==1 && types2.size()==2){
                // ins + ins_null
            }else if(types1.size() == 2 && types2.size()==1 && types2.contains(ActionConstants.INSERT)){
                // ins_null + ins
            }else if(types1.size() == 2 && types2.size()==1 && types2.contains(ActionConstants.NULLACTION)){
                // ins_null + null
            }else if(types1.size() == 2 && types2.size()== 2){
                // ins_null + ins_null
            }
        }else if(types1.contains(ActionConstants.DELETE)){
            changePacket.setOperationType(OperationTypeConstants.DELETE);
            if(types1.size() ==1 && types2.size()==1 &&types2.contains(ActionConstants.DELETE)){
                // del + del
                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
            }else if(types1.size() ==1 && types2.size()==1 &&types2.contains(ActionConstants.NULLACTION)){
                // del + null
            }else if(types1.size() ==1 && types2.size()==2){
                // del + del_null
            }else if(types1.size() == 2 && types2.size()==1 && types2.contains(ActionConstants.DELETE)){
                // del_null + del
            }else if(types1.size() == 2 && types2.size()==1 && types2.contains(ActionConstants.NULLACTION)){
                // del_null + null
            }else if(types1.size() == 2 && types2.size()== 2){
                // del_null + del_null
            }

        }else if(types1.contains(ActionConstants.MOVE)){
            changePacket.setOperationType(OperationTypeConstants.MOVE);
            if(types1.size()==2 && types1.contains(ActionConstants.NULLACTION) && types2.size() ==1 && types2.contains(ActionConstants.NULLACTION)){
                // move_null + null
            }else if(types1.size() ==3 && types2.size() ==1 && types2.contains(ActionConstants.NULLACTION)){
                // move_null_ + null
            }else if(types1.size() == 3){
                //
            }
        }


    }


    public static void setChangePacket(ChangePacket changePacket, Set<String> types1){
        if(types1.contains(ActionConstants.INSERT)){
            changePacket.setOperationType(OperationTypeConstants.INSERT);
            if(types1.size() ==1){
                // ins
                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
                // ins + ins_null
            }else if(types1.size() == 2) {
                // ins_null + ins

            }
        }else if(types1.contains(ActionConstants.DELETE)){
            changePacket.setOperationType(OperationTypeConstants.DELETE);
            if(types1.size() ==1){
                // ins
                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
                // ins + ins_null
            }else if(types1.size() == 2) {
                // ins_null + ins

            }
        }else if(types1.contains(ActionConstants.MOVE)){
            changePacket.setOperationType(OperationTypeConstants.MOVE);
            if(types1.size() ==1){
                // ins
                changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_WHOLE);
                // ins + ins_null
            }else if(types1.size() == 2) {
                // ins_null + ins

            }
        }


    }








}
