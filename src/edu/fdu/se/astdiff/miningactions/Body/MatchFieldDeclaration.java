package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.FieldChangeEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchFieldDeclaration {


    public static void matchFieldDeclaration(MiningActionData fp, Action a) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        DefaultUpDownTraversal.traverseField(a,subActions,changePacket);
        Range range = AstRelations.getRangeOfAstNode(a);
        fp.setActionTraversedMap(subActions);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        FieldChangeEntity code = new FieldChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }


    public static ClusteredActionBean matchFieldDeclarationChangeNewEntity(MiningActionData fp, Action a, Tree queryFather,Tree traverseFather) {
        ChangePacket changePacket = new ChangePacket();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        List<Action> sameEdits = new ArrayList<>();
        DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,sameEdits,changePacket);
        fp.setActionTraversedMap(sameEdits);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,queryFather);
        return mHighLevelOperationBean;
    }

    public static ClusteredActionBean matchFieldDeclarationChangeCurrEntity(MiningActionData fp,Action a){
        return null;
    }


}
