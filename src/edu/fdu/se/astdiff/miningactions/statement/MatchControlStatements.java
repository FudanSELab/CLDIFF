package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.statement.BreakContinueEntity;

/**
 * Created by huangkaifeng on 2018/2/7.
 *
 */
public class MatchControlStatements {

    public static void matchBreakStatements(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_I);
        changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        fp.setActionTraversedMap(a);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,null,changePacket,range);
        BreakContinueEntity code = new BreakContinueEntity(mBean);
        fp.addOneChangeEntity(code);
        code.changeEntity = BreakContinueEntity.breakStatement;
    }
    public static void matchContinueStatements(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_I);
        changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
        fp.setActionTraversedMap(a);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,null,changePacket,range);
        BreakContinueEntity code = new BreakContinueEntity(mBean);
        fp.addOneChangeEntity(code);
        code.changeEntity = BreakContinueEntity.continueStatement;

    }
}
