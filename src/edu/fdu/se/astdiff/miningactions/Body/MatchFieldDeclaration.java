package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.util.MatchUtil;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.MyTreeUtil;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.FieldChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    public static ClusteredActionBean matchFieldDeclarationChangeNewEntity(MiningActionData fp, Action a, ITree faFather,List<Action> sameEdits) {
        ChangePacket changePacket = new ChangePacket();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        fp.setActionTraversedMap(sameEdits);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,range,(Tree)faFather);
        return mHighLevelOperationBean;
    }


}
