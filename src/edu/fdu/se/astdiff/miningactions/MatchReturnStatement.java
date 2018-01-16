package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchReturnStatement {
    public static void matchReturnStatement(MiningActionData fp, Action a, String nodeType, TreeContext curContext) {
        ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(), curContext);
        String ffFatherNodeType = curContext.getTypeLabel(fafafatherNode);

        ITree fatherNode2 = fafafatherNode.getParent();
        String fatherNodeType2= curContext.getTypeLabel(fatherNode2);

        ClusteredActionBean operationBean;
        if(StatementConstants.SWITCHSTATEMENT.equals(ffFatherNodeType)) {
            operationBean = MatchSwitch.matchSwitchCase(fp, a, nodeType, fafafatherNode, ffFatherNodeType);
            fp.addHighLevelOperationBeanToList(operationBean);
        }else if(StatementConstants.BLOCK.equals(ffFatherNodeType) && StatementConstants.METHODDECLARATION.equals(fatherNodeType2)){
            operationBean = matchMethodReturn(fp, a, nodeType, fafafatherNode, ffFatherNodeType);
            fp.addHighLevelOperationBeanToList(operationBean);
        }else {
            String nextAction = ConsolePrint.getMyOneActionString(a, 0, curContext);
            System.out.print(nextAction);
            System.out.println("Default, ReturnStatement, curNodeType: "+ nodeType+ ", ffFatherNodeType: "+ ffFatherNodeType + "\n");
            fp.setActionTraversedMap(a);
        }
    }

    public static ClusteredActionBean matchMethodReturn(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType){
        String operationEntity = "METHOD-" +nodeType;

        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);

        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }
}
