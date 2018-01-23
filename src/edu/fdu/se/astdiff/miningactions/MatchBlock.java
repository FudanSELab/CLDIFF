package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ActionPrinter;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MatchBlock {
    public static void matchBlock(MiningActionData fp, Action a, String nodeType, TreeContext curContext) {
        ClusteredActionBean operationBean;

        Tree fatherNode = (Tree)a.getNode().getParent();
        String fatherNodeType = fatherNode.getAstClass().getSimpleName();
        switch (fatherNodeType) {
            case StatementConstants.SWITCHSTATEMENT:
                operationBean = MatchSwitch.matchSwitchCaseByFather(fp,a,nodeType, fatherNode, fatherNodeType);
                fp.addHighLevelOperationBeanToList(operationBean);
                break;
            case StatementConstants.IFSTATEMENT:
                //Pattern 1.2 Match else
                operationBean = MatchIfElse.matchElse(fp, a, nodeType, fatherNode, fatherNodeType);
                fp.addHighLevelOperationBeanToList(operationBean);
                break;
            case StatementConstants.TRYSTATEMENT:
                ////Finally块
                operationBean = MatchTry.matchFinally(fp, a, nodeType, fatherNode, fatherNodeType);
                fp.addHighLevelOperationBeanToList(operationBean);
                break;
            default:
                String nextAction = ActionPrinter.getMyOneActionString(a, 0, curContext);
                System.out.print(nextAction);
                System.out.println("Default, Block, curNodeType: "+nodeType+", "+"fatherNodeType: " + fatherNodeType +"\n");
                fp.setActionTraversedMap(a);
                break;
        }


//						}else if(AstRelations.isChildCotainSynchronizedStatement(a,this.mMiningActionData.mDstTree)) {
//							//同步语句块增加
//							MatchSynchronized.matchSynchronized(this,a);

    }
    public static void matchBlockByChild(MiningActionData fp, Action a, String nodeType, TreeContext curContext) {
        ClusteredActionBean operationBean;

        Tree fatherNode = (Tree)a.getNode().getParent();
        String fatherNodeType = fatherNode.getAstClass().getSimpleName();

        switch (fatherNodeType) {
            case StatementConstants.SWITCHSTATEMENT:
                operationBean = MatchSwitch.matchSwitchCaseByFather(fp,a,nodeType, fatherNode, fatherNodeType);
                fp.addHighLevelOperationBeanToList(operationBean);
                break;
            case StatementConstants.IFSTATEMENT:
                //Pattern 1.2 Match else
                operationBean = MatchIfElse.matchElse(fp, a, nodeType, fatherNode, fatherNodeType);
                fp.addHighLevelOperationBeanToList(operationBean);
                break;
            case StatementConstants.TRYSTATEMENT:
                ////Finally块
                operationBean = MatchTry.matchFinally(fp, a, nodeType, fatherNode, fatherNodeType);
                fp.addHighLevelOperationBeanToList(operationBean);
                break;
            default:
                String nextAction = ActionPrinter.getMyOneActionString(a, 0, curContext);
                System.out.print(nextAction);
                System.out.println("Default, Block: " + fatherNodeType +"\n");
                fp.setActionTraversedMap(a);
                break;
        }


//						}else if(AstRelations.isChildCotainSynchronizedStatement(a,this.mMiningActionData.mDstTree)) {
//							//同步语句块增加
//							MatchSynchronized.matchSynchronized(this,a);

    }
}
