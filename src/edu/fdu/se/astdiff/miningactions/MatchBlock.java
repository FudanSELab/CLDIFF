package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MatchBlock {
    private static ClusteredActionBean operationBean;
    public static void matchBlock(MiningActionData fp, Action a, String nodeType, TreeContext curContext) {
//        ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(), curContext);
//        String ffFatherNodeType = curContext.getTypeLabel(fafafatherNode);

        ITree fatherNode = a.getNode().getParent();
        String fatherNodeType = curContext.getTypeLabel(fatherNode);

        switch (fatherNodeType) {
            case StatementConstants.SWITCHSTATEMENT:
                operationBean = MatchSwitch.matchSwitchCase(fp,a,nodeType, fatherNode, fatherNodeType);
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
                String nextAction = ConsolePrint.getMyOneActionString(a, 0, curContext);
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
//        ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(), curContext);
//        String ffFatherNodeType = curContext.getTypeLabel(fafafatherNode);

        ITree fatherNode = a.getNode().getParent();
        String fatherNodeType = curContext.getTypeLabel(fatherNode);

        switch (fatherNodeType) {
            case StatementConstants.SWITCHSTATEMENT:
                operationBean = MatchSwitch.matchSwitchCase(fp,a,nodeType, fatherNode, fatherNodeType);
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
                String nextAction = ConsolePrint.getMyOneActionString(a, 0, curContext);
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
