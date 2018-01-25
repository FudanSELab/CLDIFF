package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

import java.util.ArrayList;
import java.util.List;

public class MatchInitializerBlock {
    public static void matchInitializerBlock(MiningActionData fp, Action a, String nodeType) {
        List<ITree> child = a.getNode().getChildren();
        Tree child0 = (Tree)child.get(0);

        String firstChildType = child0.getAstClass().getSimpleName();

        String secondChildType = "";
        if(child.size()>1) {
            Tree child1 = (Tree) child.get(1);
            secondChildType = child1.getAstClass().getSimpleName();
        }
        ClusteredActionBean operationBean;

        if(StatementConstants.BLOCK.equals(firstChildType)){
            operationBean = matchStatementBlock(fp,a,nodeType);
        }else if(StatementConstants.BLOCK.equals(secondChildType) && StatementConstants.STATIC.equals(child.get(0).getLabel())){
            operationBean = matchStaticBlock(fp,a,nodeType);
        }else{
            System.out.println("Other Condition"+ ActionConstants.getInstanceStringName(a) + " " +nodeType);
            fp.setActionTraversedMap(a);
            // TODO剩下的情况
        }
    }

    public static ClusteredActionBean matchStaticBlock(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "INITIALIZER-STATICBLOCK";
        List<Action> subActions = new ArrayList<Action>();
        int status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);

        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,nodeLinePosition,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
    public static ClusteredActionBean matchStatementBlock(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "INITIALIZER-ONLYBLOCK";
        List<Action> subActions = new ArrayList<>();
        int status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);

        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,nodeLinePosition,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
}

