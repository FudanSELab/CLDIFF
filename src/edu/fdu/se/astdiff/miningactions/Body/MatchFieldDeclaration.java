package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.util.MatchUtil;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.MyTreeUtil;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchFieldDeclaration {


    public static void matchFieldDeclaration(MiningActionData fp, Action a) {
        // insert field declaration
        // remove field declaration
        ChangePacket changePacket = new ChangePacket();
        List<Action> allActions = new ArrayList<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_FIELD);
        Set<String> treeStatus = 

        MatchUtil.setChangePacketOperationType(a,changePacket);
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, allActions);
        fp.setActionTraversedMap(allActions);

//        ITree nodeContainVariableDeclarationFragment = AstRelations.isSubChildContainXXXStatement(a,StatementConstants.VARIABLEDECLARATIONFRAGMENT);
//        if (nodeContainVariableDeclarationFragment != null && nodeContainVariableDeclarationFragment.getChildren().size()>1) {
//            operationEntity += "-OBJECT-INITIALIZING";
//            if (AstRelations.isClassCreation(allActions)) {
//                operationEntity += "-NEW";
//            }
//        }
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(
                a,allActions,nodeLinePosition,status,operationEntity);
    }
    public static ClusteredActionBean matchFieldDeclarationByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
        String operationEntity  = "FATHER-FIELDDECLARATION";

        List<Action> allActions = new ArrayList<>();
        ITree srcfafafather = null;
        ITree dstfafafather = null;
        if (a instanceof Insert) {
            dstfafafather = fafafatherNode;
            srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
            if (srcfafafather == null) {
                System.err.println("err null mapping");
            }
        } else {
            srcfafafather = fafafatherNode;
            dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
            if (dstfafafather == null) {
                System.err.println("err null mapping");
            }
        }

        Set<String> srcT = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
        Set<String> dstT = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
        int status = MyTreeUtil.isSrcOrDstAdded(srcT,dstT);
        fp.setActionTraversedMap(allActions);
        ITree nodeContainVariableDeclarationFragment = AstRelations.isChildContainVariableDeclarationFragment(fafafatherNode);
        if (nodeContainVariableDeclarationFragment != null && nodeContainVariableDeclarationFragment.getChildren().size()>1) {
            operationEntity += "-OBJECT-INITIALIZING";
            if (AstRelations.isClassCreation(allActions)) {
                operationEntity += "-NEW";
            }
        }
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }
}
