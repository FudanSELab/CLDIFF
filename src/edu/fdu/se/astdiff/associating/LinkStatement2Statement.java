package edu.fdu.se.astdiff.associating;

import edu.fdu.se.astdiff.associating.linkbean.LinkBean;
import edu.fdu.se.astdiff.associating.linkbean.StmtData;
import edu.fdu.se.astdiff.miningchangeentity.ChangeEntityData;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.statement.ForChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.IfChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.SynchronizedChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.statement.WhileChangeEntity;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class LinkStatement2Statement {


    public static void checkStmtAssociation(ChangeEntityData changeEntityData, ChangeEntity ce1, ChangeEntity ce2){

        StmtData linkBean1 = (StmtData) ce1.linkBean;
        StmtData linkBean2 = (StmtData) ce2.linkBean;
//        for(String tmp:linkBean1.variableField){
//            if(linkBean2.variableField.contains(tmp)){
//                Association association = new Association(ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.TYPE_SHARE_FIELD,tmp);
//                changeEntityData.mAssociations.add(association);
//                break;
//            }
//        }
//        for(String tmp:linkBean1.variableLocal) {
//            if(linkBean2.variableLocal.contains(tmp)){
//                if("".equals(tmp)){
//                    continue;
//                }
//                Association association = new Association(ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.TYPE_SAME_VARIABLE,tmp);
//                changeEntityData.mAssociations.add(association);
//                break;
//            }
//        }


    }

    public static void checkStmtShareField(ChangeEntityData changeEntityData,ChangeEntity ce1,ChangeEntity ce2){
        StmtData linkBean1 = (StmtData) ce1.linkBean;
        StmtData linkBean2 = (StmtData) ce2.linkBean;
        //pass
//        for(String l1:linkBean1.variableField){
//            for(String l2:linkBean2.variableField){
//                if(l1.equals(l2)){
//                    Association association = new Association(ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.TYPE_SHARE_FIELD,l2);
//                    changeEntityData.mAssociations.add(association);
//                }
//            }
//        }
    }



}
