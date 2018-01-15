package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MatchReturnStatement {
    public static void matchReturnStatement(MiningActionData fp, Action a, String nodeType, TreeContext curContext) {
        ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(), curContext);
        String ffFatherNodeType = curContext.getTypeLabel(fafafatherNode);
        switch (ffFatherNodeType) {
            case StatementConstants.SWITCHSTATEMENT:
                ClusteredActionBean operationBean = MatchSwitch.matchSwitchCase(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
                fp.addHighLevelOperationBeanToList(operationBean);
                break;
            default:
                String nextAction = ConsolePrint.getMyOneActionString(a, 0, curContext);
                System.out.print(nextAction);
                System.out.println("Default, ReturnStatement: " + ffFatherNodeType +"\n");
                fp.setActionTraversedMap(a);
                break;
        }
    }
}
