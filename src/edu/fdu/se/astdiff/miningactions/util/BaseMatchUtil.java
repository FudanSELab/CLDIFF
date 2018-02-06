package edu.fdu.se.astdiff.miningactions.util;


import edu.fdu.se.astdiff.generatingactions.ActionConstants;

import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/25.
 *
 */
public class BaseMatchUtil {


    public static boolean oneItemInsert(Set<String> type){
        if(type.size()==1&&type.contains(ActionConstants.INSERT)){
            return true;
        }
        return false;
    }

    public static boolean oneItemDelete(Set<String> type){
        if(type.size()==1&&type.contains(ActionConstants.DELETE)){
            return true;
        }
        return false;

    }

    public static boolean oneItemMoveOrTwoItemMoveAndNullAction(Set<String> type){
        if(type.size()==2&&type.contains(ActionConstants.MOVE)&&type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        if(type.size()==1&&type.contains(ActionConstants.MOVE)){
            return true;
        }
        return false;
    }
    public static boolean oneItemNullAction(Set<String> type){
        if(type.size()==1&&type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        return false;
    }





}
