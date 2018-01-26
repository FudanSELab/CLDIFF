package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.MatchIfElse;
import edu.fdu.se.astdiff.miningactions.statement.MatchSwitch;
import edu.fdu.se.astdiff.miningactions.statement.MatchTry;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;

public class MatchBlock {

    public static void matchBlock(MiningActionData fp, Action a, String nodeType) {
        Tree fatherNode = (Tree)a.getNode().getParent();
        String fatherNodeType = fatherNode.getAstClass().getSimpleName();
        switch (fatherNodeType) {
            case StatementConstants.SWITCHSTATEMENT:
                MatchSwitch.matchSwitchCaseByFather(fp,a, fatherNode);
                break;
            case StatementConstants.IFSTATEMENT:
                //Pattern 1.2 Match else
                MatchIfElse.matchElse(fp, a, nodeType, fatherNode, fatherNodeType);
                break;
            case StatementConstants.TRYSTATEMENT:
                ////Finally块
                MatchTry.matchFinally(fp, a, nodeType, fatherNode, fatherNodeType);
                break;
            default:
                String nextAction = SimpleActionPrinter.getMyOneActionString(a);
                System.out.print(nextAction);
                System.out.println("Default, Block, curNodeType: "+nodeType+", "+"fatherNodeType: " + fatherNodeType +"\n");
                fp.setActionTraversedMap(a);
                break;
        }
    }
//    public static void matchBlockByChild(MiningActionData fp, Action a, String nodeType, TreeContext curContext) {
//        ClusteredActionBean operationBean;
//
//        Tree fatherNode = (Tree)a.getNode().getParent();
//        String fatherNodeType = fatherNode.getAstClass().getSimpleName();
//
//        switch (fatherNodeType) {
//            case StatementConstants.SWITCHSTATEMENT:
//                MatchSwitch.matchSwitchCaseByFather(fp,a,nodeType, fatherNode, fatherNodeType);
//                break;
//            case StatementConstants.IFSTATEMENT:
//                //Pattern 1.2 Match else
//                MatchIfElse.matchElse(fp, a, nodeType, fatherNode, fatherNodeType);
//                break;
//            case StatementConstants.TRYSTATEMENT:
//                ////Finally块
//                MatchTry.matchFinally(fp, a, nodeType, fatherNode, fatherNodeType);
//                break;
//            default:
//                String nextAction = ActionPrinter.getMyOneActionString(a, 0, curContext);
//                System.out.print(nextAction);
//                System.out.println("Default, Block: " + fatherNodeType +"\n");
//                fp.setActionTraversedMap(a);
//                break;
//        }
//    }
}
