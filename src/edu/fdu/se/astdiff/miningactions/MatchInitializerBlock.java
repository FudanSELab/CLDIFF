package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchInitializerBlock {
    public static void matchInitializerBlock(MiningActionData fp, Action a, String nodeType, TreeContext curContext) {
        List<ITree> child = a.getNode().getChildren();

        String firstChildType = curContext.getTypeLabel(child.get(0));

        String secondChildType = "";
        if(child.size()>1) {
            secondChildType = curContext.getTypeLabel(child.get(1));
        }
//        String secondChildType = curContext.getTypeLabel(secondChild);
        ClusteredActionBean operationBean;

        if(StatementConstants.BLOCK.equals(firstChildType)){
            operationBean = matchStatementBlock(fp,a,nodeType);
            fp.addHighLevelOperationBeanToList(operationBean);
        }else if(StatementConstants.BLOCK.equals(secondChildType) && StatementConstants.STATIC.equals(child.get(0).getLabel())){
            operationBean = matchStaticBlock(fp,a,nodeType);
            fp.addHighLevelOperationBeanToList(operationBean);
        }else{
            System.out.println("Other Condition"+ ActionConstants.getInstanceStringName(a) + " " +nodeType);
            fp.setActionTraversedMap(a);
            // TODO剩下的情况
        }
    }

    public static ClusteredActionBean matchStaticBlock(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "INITIALIZER-STATICBLOCK";
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);
        TreeContext con = null;
        if (a instanceof Insert) {
            con = fp.getDstTree();
        } else{
            con = fp.getSrcTree();
        }
        Range nodeLinePosition = AstRelations.getnodeLinePosition(a,con);

        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,nodeLinePosition,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
    public static ClusteredActionBean matchStatementBlock(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "INITIALIZER-ONLYBLOCK";
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);
        TreeContext con = null;
        if (a instanceof Insert) {
            con = fp.getDstTree();
        } else{
            con = fp.getSrcTree();
        }
        Range nodeLinePosition = AstRelations.getnodeLinePosition(a,con);

        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,nodeLinePosition,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
}

