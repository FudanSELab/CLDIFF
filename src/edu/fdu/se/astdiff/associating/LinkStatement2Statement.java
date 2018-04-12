package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.associating.linkbean.LinkBean;
import edu.fdu.se.astdiff.associating.linkbean.StmtData;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class LinkStatement2Statement {


    public static void checkStmtAssociation(ChangeEntityData changeEntityData, ChangeEntity ce1, ChangeEntity ce2){
        if(isRangeWithin(ce1,ce2)){
            Association association = new Association(ce1,ce1,ChangeEntityDesc.StageIIIAssociationType.TYPE_CONTROL);
            changeEntityData.mAssociations.add(association);
        }
        LinkBean linkBean1 = ce1.linkBean;
        LinkBean linkBean2 = ce2.linkBean;
        if(isContainSameVar(linkBean1,linkBean2)){
            Association association = new Association(ce1,ce1,ChangeEntityDesc.StageIIIAssociationType.TYPE_VARIABLE);
            changeEntityData.mAssociations.add(association);
        }

    }

    private static boolean isRangeWithin(ChangeEntity ce1,ChangeEntity ce2){
        MyRange myRange1 = ce1.getLineRange();
        MyRange myRange2 = ce2.getLineRange();
        if(myRange1.isRangeWithin(myRange2)!=0){
            return true;
        }else{
            return  false;
        }
    }

    private static boolean isContainSameVar(LinkBean linkBean1,LinkBean linkBean2){
        assert linkBean1 instanceof StmtData;
        assert linkBean2 instanceof StmtData;
        StmtData stmtData1 = (StmtData)linkBean1;
        StmtData stmtData2 = (StmtData)linkBean2;
        if(stmtData1.isCommitVar(stmtData2)!=0){
            return true;
        }else{
            return false;
        }



    }
}
