package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
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
            String nextAction = ConsolePrint.getMyOneActionString(a, 0, fp.mSrcTree);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                // 标记过的 update action
                continue;
            }
            Update up = (Update) a;
            ITree tmp = a.getNode();
            String type = fp.mSrcTree.getTypeLabel(tmp);
            ITree fafafather = AstRelations.findFafafatherNode(tmp, fp.mSrcTree);
            if(fafafather == null){
                System.out.println("Father Null Condition: "+ ActionConstants.getInstanceStringName(a) + " " +type);
                fp.setActionTraversedMap(a);
                continue;
            }
            String fatherType = fp.mSrcTree.getTypeLabel(fafafather);
//			System.out.print(nextAction);
            ClusteredActionBean operationBean;
            //类签名状态
            if(StatementConstants.TYPEDECLARATION.equals(fatherType)){
                //class signature
                operationBean = MatchClassSignature.matchClassSignature(fp,a,type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            }

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
                    case StatementConstants.MODIFIER:
                    case StatementConstants.MARKERANNOTATION:
                    case StatementConstants.NORMALANNOTATION:
                    case StatementConstants.SINGLEMEMBERANNOTATION:
                        MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(fp,a, type,fp.mSrcTree);
                        break;
                    default:
                        String operationEntity = "DEFAULT: "+ ActionConstants.getInstanceStringName(a) + " " +type;
                        Range nodeLinePosition = AstRelations.getnodeLinePosition(a,fp.mSrcTree);
                        operationBean = new ClusteredActionBean(a,type,null,nodeLinePosition,-1,operationEntity,fafafather,fatherType);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        fp.setActionTraversedMap(a);
                        break;

                }
            }
        }

    }
}
