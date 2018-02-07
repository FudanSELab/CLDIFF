package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.ExpressionChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.ReturnChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchReturnStatement {
    public static void matchReturnStatement(MiningActionData fp, Action a) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_I);
        DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        ReturnChangeEntity code = new ReturnChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }

    public static void matchReturnStatentByFather(MiningActionData fp, Action a, Tree fafatherNode, List<Action> sameEditActions){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_I);
        DefaultUpDownTraversal.traverseClass(a,subActions,changePacket);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,subActions,changePacket,range);
        ExpressionChangeEntity code = new ExpressionChangeEntity(mBean);
        fp.addOneChangeEntity(code);

        String operationEntity = "FATHER-RETURNSTATEMENT";

    }
}
