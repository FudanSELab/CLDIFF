package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.HighLevelOperationBean;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class ClusterUpdate {

    /**
     * Sub level-II
     */
    public static void findUpdate(FindPatternData fp) {
        int updateActionCount = fp.mMiningActionBean.mActionGeneratorBean.getUpdateActions().size();
        int index = 0;
        while (true) {
            if (index == updateActionCount) {
                break;
            }
            Action a = fp.mMiningActionBean.mActionGeneratorBean.getUpdateActions().get(index);
            index++;
            if (fp.mMiningActionBean.mActionGeneratorBean.getAllActionMap().get(a) == 1) {
                // 标记过的 update action
                continue;
            }
            Update up = (Update) a;
            ITree tmp = a.getNode();
            String type = fp.mMiningActionBean.mSrcTree.getTypeLabel(tmp);
            String nextAction = ConsolePrint.getMyOneActionString(a, 0, fp.mMiningActionBean.mSrcTree);
            ITree fafafather = AstRelations.findFafafatherNode(tmp, fp.mMiningActionBean.mSrcTree);
            String fatherType = fp.mMiningActionBean.mSrcTree.getTypeLabel(fafafather);
//			System.out.print(nextAction);
            HighLevelOperationBean operationBean;
            if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
                //insert FieldDeclaration body
                operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(fp,a,type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            }

            if (StatementConstants.METHODDECLARATION.equals(type)) {
                // 新增方法
                System.out.println("Update 应该不会是method declaration");
            } else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
                // 方法签名update
                if (StatementConstants.BLOCK.equals(type)) {
                    System.out.print(nextAction);
                    System.out.println("Not considered\n");
                } else {
                    operationBean = MatchMethodSignatureChange.matchMethodSignatureChange(fp,a, type,fafafather,fatherType);
                    fp.mHighLevelOperationBeanList.add(operationBean);
                }
            } else {
                // 方法体
                switch (type) {
                    case StatementConstants.SIMPLENAME:
                    case StatementConstants.STRINGLITERAL:
                    case StatementConstants.NULLLITERAL:
                    case StatementConstants.CHARACTERLITERAL:
                    case StatementConstants.NUMBERLITERAL:
                    case StatementConstants.BOOLEANLITERAL:
                    case StatementConstants.INFIXEXPRESSION:
                    case StatementConstants.METHODINVOCATION:
                        MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(fp,a, type,fp.mMiningActionBean.mSrcTree);
                        break;
                    default:
                        break;
                }
            }
        }

    }
}
