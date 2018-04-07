package edu.fdu.se.astdiff.miningchangeentity;

import com.github.gumtreediff.actions.model.Action;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/15.
 *
 */
public class OperationTypeConstants {

    final public static int UNKNOWN = -1;

    private static Map<Integer,String> constantName;
    static {
        constantName = new HashMap<>();
        constantName.put(-1,"UNKNOWN");

        constantName.put(10000,"Insert");
        constantName.put(10001,"Delete");
        constantName.put(10002,"MOVE");
        constantName.put(10003,"UPDATE");
        constantName.put(10004,"MULTIPLE_EDIT");

        constantName.put(11000,"MEMBER");
        constantName.put(11001,"STATEMENT_I");
        constantName.put(11002,"STATEMENT_II");

        constantName.put(12000,"WHOLE_STRUCTURE");
        constantName.put(12001,"WHOLE_WRAPPER");
        constantName.put(12002,"REFURNISH");
        constantName.put(12003,"STRUCTURE_CHANGE");

        constantName.put(10005,"INSERT_M");
        constantName.put(10006,"DELETE_M");
        constantName.put(10007,"MOVE_M");
        constantName.put(10010,"CHANGE");
    }

    // 操作类型，操作entity，操作子entity

    final public static int INSERT = 10000;
    final public static int DELETE = 10001;
    final public static int MOVE = 10002;
    final public static int UPDATE = 10003;

    final public static int CHANGE = 10010;


//    final public static int MULTIPLE_EDIT = 10004;
//
//    final public static int INSERT_M = 10005;
//    final public static int DELETE_M = 10006;
//    final public static int MOVE_M = 10007;
//







    public static String getKeyNameByValue(int v){
        return constantName.get(v);
    }



    public static int getEditTypeIntCode(Action a){
        switch (a.getClass().getSimpleName()){
            case "Insert":return INSERT;
            case "Move":return MOVE;
            case "Update":return UPDATE;
            case "Delete":return DELETE;
        }
        return UNKNOWN;
    }


    public static int getEditTypeIntCode(String a){
        switch (a){
            case "Insert":return INSERT;
            case "Move":return MOVE;
            case "Update":return UPDATE;
            case "Delete":return DELETE;
        }
        return UNKNOWN;
    }

    public static String getChangeEntityDescString(Action a){
        switch (a.getClass().getSimpleName()){
            case "Insert":return ChangeEntityDesc.StageIIIOpt.OPT_INSERT;
            case "Move":return ChangeEntityDesc.StageIIIOpt.OPT_MOVE;
            case "Delete":return ChangeEntityDesc.StageIIIOpt.OPT_DELETE;
            case "Update":break;
        }
        return null;
    }

// 最小变化单位为Statement 变化包括 INS,MOV,UPD,MULTI_EDITS
    // 多个单位如果连续 而且变化相同，组合成cluster



}
