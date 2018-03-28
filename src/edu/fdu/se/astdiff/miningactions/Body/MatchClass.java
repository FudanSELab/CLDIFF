package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.util.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.member.ClassOrInterfaceDeclarationChangeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MatchClass {


    public static void matchClassDeclaration(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
            DefaultUpDownTraversal.traverseClass(a, subActions, changePacket);
        }
        fp.setActionTraversedMap(subActions);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIIENTITY.ENTITY_INNER_CLASS);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setLineRange("("+code.lineRange.startLineNo +","+ code.lineRange.endLineNo+")");
        fp.addOneChangeEntity(code);
    }

    public static void matchClassSignatureNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType,Tree traverseFather) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
            DefaultDownUpTraversal.traverseClassSignature(traverseFather,subActions,changePacket);
        }
        fp.setActionTraversedMap(subActions);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,subActions,changePacket,queryFather,treeType);
        ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean,ChangeEntityDesc.StageIIIENTITY.ENTITY_CLASS);
        code.changeEntity = ClassOrInterfaceDeclarationChangeEntity.CLASS_SIGNATURE;
        fp.addOneChangeEntity(code);
    }

    public static void matchClassSignatureCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather) {
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> actions = changeEntity.clusteredActionBean.actions;
        List<Action> newActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,actions,changePacket,false)){
            DefaultDownUpTraversal.traverseClassSignature(traverseFather,newActions,changePacket);
        }

        for(Action tmp:newActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            actions.add(tmp);
        }
        changeEntity.linkBean.addAppendedActions(newActions);
        fp.setActionTraversedMap(newActions);
    }





}
