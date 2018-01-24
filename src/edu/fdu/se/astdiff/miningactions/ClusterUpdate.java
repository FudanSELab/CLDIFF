package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ActionPrinter;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class ClusterUpdate {

    /**
     * Sub level-II
     */
    public static void findUpdate(MiningActionData fp) {
        int updateActionCount = fp.mGeneratingActionsData.getUpdateActions().size();
        int index = 0;
        while (true) {
            if (index == updateActionCount) {
                break;
            }
            Action a = fp.mGeneratingActionsData.getUpdateActions().get(index);
            String nextAction = ActionPrinter.getMyOneActionString(a, 0, fp.mSrcTree);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                // 标记过的 update action
                continue;
            }
            Update up = (Update) a;
            Tree tmp = (Tree)a.getNode();
            String type = tmp.getAstClass().getSimpleName();
            Tree fafafather = AstRelations.findFafafatherNode(tmp);
            if(fafafather == null){
                System.out.println("Father Null Condition: "+ ActionConstants.getInstanceStringName(a) + " " +type);
                fp.setActionTraversedMap(a);
                continue;
            }
            String fatherType = fafafather.getAstClass().getSimpleName();
//			System.out.print(nextAction);
            ClusteredActionBean operationBean;
            if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
                //insert FieldDeclaration body
                operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(fp,a,type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            }

           if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
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
                    //JAVADOC参数
                    case StatementConstants.TAGELEMENT:
                    case StatementConstants.TEXTELEMENT:
                        //参数
                    case StatementConstants.SIMPLENAME:
                    case StatementConstants.SIMPLETYPE:
                    case StatementConstants.STRINGLITERAL:
                    case StatementConstants.NULLLITERAL:
                    case StatementConstants.CHARACTERLITERAL:
                    case StatementConstants.NUMBERLITERAL:
                    case StatementConstants.BOOLEANLITERAL:
                    case StatementConstants.INFIXEXPRESSION:
                    case StatementConstants.METHODINVOCATION:
                    case StatementConstants.QUALIFIEDNAME:
                        MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(fp,a, type);
                        break;
                    default:
                        String operationEntity = "DEFAULT: "+ ActionConstants.getInstanceStringName(a) + " " +type;
                        Range nodeLinePosition = AstRelations.getnodeLinePosition(a);
                        operationBean = new ClusteredActionBean(a,type,null,nodeLinePosition,-1,operationEntity,fafafather,fatherType);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        fp.setActionTraversedMap(a);
                        break;

                }
            }
        }

    }
}
