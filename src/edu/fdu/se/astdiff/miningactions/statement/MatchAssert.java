package edu.fdu.se.astdiff.miningactions.statement;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.AssertChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.ExpressionChangeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/5.
 *
 */
public class MatchAssert {

    public static void matchAssert(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_I);
        DefaultUpDownTraversal.traverseTypeIStatements(a,subActions,changePacket);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        AssertChangeEntity code = new AssertChangeEntity(mBean);
        fp.addOneChangeEntity(code);

    }

    public static void matchAssertByFather(MiningActionData fp,Action a,Tree fafather){
        ChangePacket changePacket = new ChangePacket();
        List<Action> sameEdits = new ArrayList<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_STATEMENT_TYPE_I);
        DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(fafather,sameEdits,changePacket);
        fp.setActionTraversedMap(sameEdits);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,fafather);
        AssertChangeEntity code = new AssertChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }
}
