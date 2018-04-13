package edu.fdu.se.astdiff.associating;

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
        if(method.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
            // method 是否在一个range 以及parameter
        }else{
            // method name是否在invoke的list里面
        }

    }

    public static void checkStmtFieldAssociation(ChangeEntityData changeEntityData, ChangeEntity stmt,FieldChangeEntity field){
        StmtData stmtData = (StmtData) stmt.linkBean;

    }
}
