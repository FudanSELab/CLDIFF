package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;

public class MatchReturnStatement {
    private static HighLevelOperationBean operationBean;
    public static void matchReturnStatement(FindPattern fp, Action a, String nodeType, TreeContext curContext) {
        ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(), curContext);
        String ffFatherNodeType = curContext.getTypeLabel(fafafatherNode);
        switch (ffFatherNodeType) {
            case StatementConstants.SWITCHSTATEMENT:
                operationBean = MatchSwitch.matchSwitchCase(fp,a,nodeType, fafafatherNode, ffFatherNodeType);
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
