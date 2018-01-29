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


    public static void matchClassAddOrDelete(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CLASS);
        DefaultUpDownTraversal.traverseClass(a,subActions,changePacket);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }

    public static void matchClassSignature(MiningActionData fp, Action a, ITree fafather, ChangeEntity changeEntity) {
        // insert/delete class signature paramter
        // insert/delete class modifier
        // update class modifier / primitive type
        ChangePacket changePacket = null;
        List<Action> signatureChidlren = null;
        if(changeEntity == null) {
            changePacket = new ChangePacket();
            signatureChidlren = new ArrayList<>();
            changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CLASS);
        }else{
            changePacket = changeEntity.clusteredActionBean.changePacket;
            signatureChidlren = changeEntity.clusteredActionBean.actions;
        }


        ITree[] res = TraverseTree.getMappedFafatherNode(fp,a,fafathers);
        //todo
        fp.setActionTraversedMap(signatureChidlren);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,signatureChidlren,changePacket,range,(Tree)srcFafather);
        ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }




}
