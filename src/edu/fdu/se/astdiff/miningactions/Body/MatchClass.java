package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import edu.fdu.se.astdiff.miningactions.util.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class MatchClass {


    public static void matchClassDeclaration(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CLASS);
        DefaultUpDownTraversal.traverseClass(a,subActions,changePacket);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }

    public static void matchClassSignatureNewEntity(MiningActionData fp, Action a, ITree fafather,List<Action> sameEditActions ) {
        ChangePacket changePacket = new ChangePacket();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CLASS);
        fp.setActionTraversedMap(sameEditActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEditActions,changePacket,range,(Tree)fafather);
        ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }

    public static void matchClassSignatureCurEntity(MiningActionData fp, Action a,ChangeEntity changeEntity,List<Action> sameEditActions) {
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> signatureChildren = changeEntity.clusteredActionBean.actions;
        for(Action tmp:sameEditActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            signatureChildren.add(tmp);
        }
        fp.setActionTraversedMap(sameEditActions);

    }




}
