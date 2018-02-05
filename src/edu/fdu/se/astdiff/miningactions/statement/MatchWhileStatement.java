package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.WhileChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchWhileStatement {
    public static void matchWhileStatement(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        DefaultUpDownTraversal.traverseTypeIIStatements(a,subActions,changePacket);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        WhileChangeEntity code = new WhileChangeEntity(mBean);
        fp.addOneChangeEntity(code);
        code.changeEntity = WhileChangeEntity.WHILE;

    }

    public static void matchDoStatement(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        DefaultUpDownTraversal.traverseClass(a,subActions,changePacket);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        WhileChangeEntity code = new WhileChangeEntity(mBean);
        fp.addOneChangeEntity(code);
        code.changeEntity = WhileChangeEntity.DO_WHILE;
    }
}
