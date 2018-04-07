package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.OperationTypeConstants;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.member.EnumChangeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/3/21.
 * enum 增和删在预处理   enum内部的增删改，看作是enum的改，
 * constant的增删改 -> EnumConstantDeclaration- > EnumDeclaration
 * method field 的增删改 - > 其自身的增删改 （不另加考虑）
 */
public class MatchEnum {

    public static void matchEnum(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true)){
            DefaultUpDownTraversal.traverseTypeIStatements(a, subActions, changePacket);
        }
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        EnumChangeEntity code = new EnumChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
//        code.stageIIBean.setOpt2(null);
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.setActionTraversedMap(subActions);
        fp.addOneChangeEntity(code);
    }

    public static void matchEnumDeclarationNewEntity(MiningActionData fp, Action a, Tree queryFather, int treeType, Tree traverseFather){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
            DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,subActions,changePacket);
        }
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,subActions,changePacket,queryFather,treeType);
        EnumChangeEntity code = new EnumChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
//        code.stageIIBean.setOpt2(null);// 暂时不设置
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.setActionTraversedMap(subActions);
        fp.addOneChangeEntity(code);
    }

    public static void matchEnumDeclarationCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity,Tree traverseFather){
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> actions = changeEntity.clusteredActionBean.actions;
        List<Action> newActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,actions,changePacket,false)){
            DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,newActions,changePacket);
        }
        for(Action tmp:newActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            actions.add(tmp);
        }
//        changeEntity.linkBean.addAppendedActions(newActions);
        fp.setActionTraversedMap(newActions);
    }
}
