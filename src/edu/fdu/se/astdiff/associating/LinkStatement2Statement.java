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
        if(LinkUtil.isRangeWithin(ce1,ce2)){
            Association association = new Association(ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.TYPE_CONTROL);
            changeEntityData.mAssociations.add(association);
        }
        LinkBean linkBean1 = ce1.linkBean;
        LinkBean linkBean2 = ce2.linkBean;
        if(isContainSameVar(linkBean1,linkBean2)!=0){
            Association association = new Association(ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.TYPE_SAME_VARIABLE);
            changeEntityData.mAssociations.add(association);
        }

    }

    public static void checkStmtShareField(ChangeEntityData changeEntityData,ChangeEntity ce1,ChangeEntity ce2){
        StmtData linkBean1 = (StmtData) ce1.linkBean;
        StmtData linkBean2 = (StmtData) ce2.linkBean;
        for(String l1:linkBean1.variableField){
            for(String l2:linkBean2.variableField){
                if(l1.equals(l2)){
                    Association association = new Association(ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.TYPE_SHARE_FIELD);
                    changeEntityData.mAssociations.add(association);
                }
            }
        }
    }



    private static int isContainSameVar(LinkBean linkBean1,LinkBean linkBean2){
        assert linkBean1 instanceof StmtData;
        assert linkBean2 instanceof StmtData;
        StmtData stmtData1 = (StmtData)linkBean1;
        StmtData stmtData2 = (StmtData)linkBean2;
        return stmtData1.isContainSameVar(stmtData2);

    }
}
