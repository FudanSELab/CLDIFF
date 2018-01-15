package edu.fdu.se.astdiff.miningoperationbean;

import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/15.
 */
public class OperationTypeConstants {

    final public static int UNKNOWN = -1;

    final public static int INSERT_STATEMENT_AND_BODY = 1;

    final public static int DELETE_STATEMENT_AND_BODY = 2;

    final public static int MOVE_STATEMENT_AND_BODY = 3;

    final public static int INSERT_STATEMENT_WRAPPER = 4;

    final public static int DELETE_STATEMENT_WRAPPER = 5;

    final public static int MOVE_STATEMENT_WRAPPER = 6;

    final public static int INSERT_STATEMENT_CONDITION = 7;

    final public static int DELETE_STATEMENT_CONDITION = 8;

    final public static int UPDATE_STATEMENT_CONDITION = 9;

    final public static int MOVE_STATEMENT_CONDITION = 10;

    final public static int STATEMENT_CONDITION_MISC = 11;

    private static Map<Integer,String> constantName;
    static {
        constantName.put(1,"INSERT_STATEMENT_AND_BODY");
        constantName.put(2,"DELETE_STATEMENT_AND_BODY");
        constantName.put(3,"MOVE_STATEMENT_AND_BODY");
        constantName.put(4,"INSERT_STATEMENT_WRAPPER");
        constantName.put(5,"DELETE_STATEMENT_WRAPPER");
        constantName.put(6,"MOVE_STATEMENT_WRAPPER");
        constantName.put(7,"INSERT_STATEMENT_CONDITION");
        constantName.put(8,"DELETE_STATEMENT_CONDITION");
        constantName.put(9,"UPDATE_STATEMENT_CONDITION");
        constantName.put(10,"MOVE_STATEMENT_CONDITION");
        constantName.put(11,"STATEMENT_CONDITION_MISC");
        constantName.put(-1,"UNKNOWN");

    }

    public static String getKeyNameByValue(int v){
        return constantName.get(v);
    }







}
