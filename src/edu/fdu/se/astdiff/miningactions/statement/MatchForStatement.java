package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.ForChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.SwitchChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchForStatement {
    public static void matchForStatement(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
        DefaultUpDownTraversal.traverseClass(a,subActions,changePacket);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        ForChangeEntity code = new ForChangeEntity(mBean);
        fp.addOneChangeEntity(code);
        code.changeEntity = ForChangeEntity.FOR;

    }

    public static void matchEnhancedForStatement(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
        DefaultUpDownTraversal.traverseClass(a,subActions,changePacket);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        ForChangeEntity code = new ForChangeEntity(mBean);
        fp.addOneChangeEntity(code);
        code.changeEntity = ForChangeEntity.FOR_EACH;
    }

    public static void matchForPredicate(MiningActionData fp, Action a,Tree fafather, List<Action> sameEdits) {
        ChangePacket changePacket = new ChangePacket();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
        fp.setActionTraversedMap(sameEdits);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,fafather);
        ForChangeEntity code = new ForChangeEntity(mBean);
        fp.addOneChangeEntity(code);

    }

    public static void matchEnhancedForPredicate(MiningActionData fp, Action a, Tree fafather,List<Action> sameEdits) {
        ChangePacket changePacket = new ChangePacket();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_II);
        fp.setActionTraversedMap(sameEdits);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,fafather);
        ForChangeEntity code = new ForChangeEntity(mBean);
        fp.addOneChangeEntity(code);


    }
}
