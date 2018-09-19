package edu.fdu.se.base.links;

import edu.fdu.se.base.links.linkbean.FieldData;
import edu.fdu.se.base.links.linkbean.MethodData;
import edu.fdu.se.base.links.linkbean.StmtData;
import edu.fdu.se.base.miningchangeentity.ChangeEntityData;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.member.FieldChangeEntity;
import edu.fdu.se.base.miningchangeentity.member.MethodChangeEntity;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class LinkStatement2Member {


    public static void checkStmtMethodAssociation(ChangeEntityData changeEntityData, ChangeEntity stmt,MethodChangeEntity method){
        StmtData stmtData = (StmtData)stmt.linkBean;
        MethodData methodData = (MethodData) method.linkBean;
        if(method.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
            // method 是否在一个range 以及parameter
            if(LinkUtil.isRangeWithin(stmt,method)==1){
                for(String params:methodData.parameterName){
                    for(String vars:stmtData.variableLocal){
                        if(params.equals(vars)){

                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_USE,vars,null);
                            Association association = new Association(stmt,method, desc,vars,changeEntityData.fileName);
                            changeEntityData.mAssociations.add(association);
                            return;
                        }
                    }
                }
            }
        }else{
            // method name是否在invoke的list里面
            for(String methodInvokes:stmtData.methodInvocation){
                if(methodData.methodName.contains(methodInvokes)){

                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_USE,methodInvokes, null);
                    Association association = new Association(stmt,method, desc,methodInvokes,changeEntityData.fileName);
                    changeEntityData.mAssociations.add(association);
                }
            }

        }

    }

    public static void checkStmtFieldAssociation(ChangeEntityData changeEntityData, ChangeEntity stmt,FieldChangeEntity field){
        StmtData stmtData = (StmtData) stmt.linkBean;
        FieldData fieldData = (FieldData) field.linkBean;
        for(String a:fieldData.fieldName){
            for(String b : stmtData.variableField){
                if(a.equals(b)){
                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_USE,a,null);
                    Association association = new Association(stmt,field,desc,b,changeEntityData.fileName);
                    changeEntityData.mAssociations.add(association);
                }
            }
        }

    }
}
