package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import org.eclipse.jdt.core.dom.ASTNode;

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

        constantName.put(0x10000,"INSERT");
        constantName.put(0x10001,"DELETE");
        constantName.put(0x10002,"MOVE");
        constantName.put(0x10003,"UPDATE");
        constantName.put(0x10004,"MULTIPLE_EDIT");

        constantName.put(0x11012,"ENTITY_MEMBER");
        constantName.put(0x11010,"ENTITY_STATEMENT_TYPE_I");
        constantName.put(0x11011,"ENTITY_STATEMENT_TYPE_II");

        constantName.put(0x12000,"SUB_ENTITY_STRUCTURE_WHOLE");
        constantName.put(0x12001,"SUB_ENTITY_STRUCTURE_REFURNISH");
        constantName.put(0x12002,"SUB_ENTITY_STRUCTURE_UPGRADE");

    }

    // 操作类型，操作entity，操作子entity

    final public static int INSERT = 0x10000;
    final public static int DELETE = 0x10001;
    final public static int MOVE = 0x10002;
    final public static int UPDATE = 0x10003;
    final public static int MULTIPLE_EDIT = 0x10004;




    final public static int ENTITY_MEMBER = 0x11012;
    final public static int ENTITY_STATEMENT_TYPE_I = 0x11010; //无嵌套
    final public static int ENTITY_STATEMENT_TYPE_II = 0x11011; // 有嵌套




    // 整个BodyDeclaration Statement的增删
    // structure
    final public static int SUB_ENTITY_STRUCTURE_WHOLE = 0x12000;
    final public static int SUB_ENTITY_STRUCTURE_REFURNISH = 0x12001;
    final public static int SUB_ENTITY_STRUCTURE_UPGRADE = 0x12002;



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
