package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.associating.linkbean.FieldData;
import edu.fdu.se.astdiff.associating.linkbean.MethodData;
import edu.fdu.se.astdiff.associating.linkbean.StmtData;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.member.FieldChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.member.MethodChangeEntity;

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
            if(LinkUtil.isRangeWithin(stmt,method)){
                for(String params:methodData.parameterName){
                    for(String vars:stmtData.variableLocal){
                        if(params.equals(vars)){
                            Association association = new Association(stmt,method,ChangeEntityDesc.StageIIIAssociationType.TYPE_PARAMETER_CHANGE_VAR_CHANGE);
                            changeEntityData.mAssociations.add(association);
                            return;
                        }
                    }
                }
            }
        }else{
            // method name是否在invoke的list里面
            for(String methodInvokes:stmtData.methodInvocation){
                if(methodInvokes.equals(methodData.methodName)){
                    Association association = new Association(stmt,method,ChangeEntityDesc.StageIIIAssociationType.TYPE_CALL_METHOD);
                    changeEntityData.mAssociations.add(association);
                }
            }

        }

    }

    public static void checkStmtFieldAssociation(ChangeEntityData changeEntityData, ChangeEntity stmt,FieldChangeEntity field){
        StmtData stmtData = (StmtData) stmt.linkBean;
        FieldData fieldData = (FieldData) field.linkBean;
        for(String a:fieldData.fieldName){
            for(String b:stmtData.variableField){
                if(a.equals(b)){
                    Association association = new Association(stmt,field,ChangeEntityDesc.StageIIIAssociationType.TYPE_FIELD_ACCESS);
                    changeEntityData.mAssociations.add(association);
                }
            }
        }

    }
}
