package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/1/15.
 */
public class OperationTypeConstants {

    final public static int UNKNOWN = -1;

    final public static int INSERT_BODYDECLARATION = 108;
    final public static int DELETE_BODYDECLARATION = 109;

    private static Map<Integer,String> constantName;
    static {
        constantName = new HashMap<>();
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

        constantName.put(100,"INSERT_FIELD_DECLARARION");
        constantName.put(101,"DELETE_FIELD_DECLARATION");
        constantName.put(102,"UPDATE_FIELD_DECLARATION");
        constantName.put(103,"MOVE_FIELD_DECLARATION");
        constantName.put(104,"INSERT_METHOD_DECLARARION");
        constantName.put(105,"DELETE_METHOD_DECLARATION");
        constantName.put(106,"UPDATE_METHOD_DECLARATION");
        constantName.put(107,"MOVE_METHOD_DECLARATION");
        constantName.put(109,"DELETE");
        constantName.put(-1,"UNKNOWN");

        constantName.put(10000,"INSERT");
        constantName.put(10001,"DELETE");
        constantName.put(10002,"MOVE");
        constantName.put(10003,"UPDATE");

    }

    // 操作类型，操作entity，操作子entity

    final public static int INSERT = 0x10001;
    final public static int DELETE = 0x10001;
    final public static int MOVE = 0x10002;
    final public static int UPDATE = 0x10003;
    final public static int MULTIPLE_EDIT = 0x10004;


//    final public static int ENTITY_INITIALIZER = 0x11003;
//    final public static int ENTITY_CLASS = 0x11004;
//    final public static int ENTITY_INTERFACE = 0x11005;
//    final public static int ENTITY_ANNOTATION = 0x11006;
//    final public static int ENTITY_METHOD = 0x11007;
//    final public static int ENTITY_FIELD = 0x11008;
//    final public static int ENTITY_CONSTRUCTOR = 0x11009;

    final public static int ENTITY_MEMBER = 0x11012;
    final public static int ENTITY_STATEMENT_TYPE_I = 0x11010; //无嵌套
    final public static int ENTITY_STATEMENT_TYPE_II = 0x11011; // 有嵌套




    // 整个BodyDeclaration Statement的增删
    // structure
    final public static int SUB_ENTITY_STRUCTURE_WHOLE = 0x12000;
    final public static int SUB_ENTITY_STRUCTURE_REFURNISH = 0x12001;
    final public static int SUB_ENTITY_STRUCTURE_UPGRADE = 0x12001;



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
// 最小变化单位为Statement 变化包括 INS,MOV,UPD,MULTI_EDITS
    // 多个单位如果连续 而且变化相同，组合成cluster



}
