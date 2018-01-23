package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

public class MatchReturnStatement {
    public static ClusteredActionBean matchReturnStatement(MiningActionData fp, Action a, String nodeType) {
        String operationEntity = "RETURNSTATEMENT";

        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        return mHighLevelOperationBean;

//        ITree fafafatherNode = AstRelations.findFafafatherNode(a.getNode().getParent(), curContext);
//        String ffFatherNodeType = curContext.getTypeLabel(fafafatherNode);
//
//        ITree fatherNode2 = fafafatherNode.getParent();
//        String fatherNodeType2= curContext.getTypeLabel(fatherNode2);
//
//        ClusteredActionBean operationBean;
//        if(StatementConstants.SWITCHSTATEMENT.equals(ffFatherNodeType)) {
//            operationBean = MatchSwitch.matchSwitchCase(fp, a, nodeType, fafafatherNode, ffFatherNodeType);
//            fp.addHighLevelOperationBeanToList(operationBean);
//        }else if(StatementConstants.BLOCK.equals(ffFatherNodeType) && StatementConstants.METHODDECLARATION.equals(fatherNodeType2)){
//            operationBean = matchMethodReturn(fp, a, nodeType, fafafatherNode, ffFatherNodeType);
//            fp.addHighLevelOperationBeanToList(operationBean);
//        }else if(StatementConstants.BLOCK.equals(ffFatherNodeType)){
//            operationBean = matchMethodReturn(fp, a, nodeType, fafafatherNode, ffFatherNodeType);
//            fp.addHighLevelOperationBeanToList(operationBean);
//        }else {
//            String nextAction = ActionPrinter.getMyOneActionString(a, 0, curContext);
//            System.out.print(nextAction);
//            System.out.println("Default, ReturnStatement, curNodeType: "+ nodeType+ ", ffFatherNodeType: "+ ffFatherNodeType + "\n");
//            fp.setActionTraversedMap(a);
//        }
    }

    public static ClusteredActionBean matchReturnStatentByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType){
        String operationEntity = "FATHER-RETURNSTATEMENT";

        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }
}
