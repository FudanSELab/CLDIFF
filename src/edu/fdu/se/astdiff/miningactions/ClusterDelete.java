package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ActionPrinter;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/13.
 * @Deprecated
 */
public class ClusterDelete {

    public static void findDelete(MiningActionData fp) {
        int deleteActionCount = fp.mGeneratingActionsData.getDeleteActions().size();
        int index = 0;
        int count = 0;
        String resultStr = null;
        while (true) {
            if (index == deleteActionCount) {
                break;
            }
            Action a = fp.mGeneratingActionsData.getDeleteActions().get(index);
            String nextAction = ActionPrinter.getMyOneActionString(a, 0, fp.mSrcTree);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Delete del = (Delete) a;
            Tree tmp = (Tree)a.getNode();
            String type = tmp.getAstClass().getSimpleName();
            Tree fafafather = (Tree)AstRelations.findFafafatherNode(tmp);
            if(fafafather == null){
                System.out.println("Father Null Condition: "+ ActionConstants.getInstanceStringName(a) + " " +type);
                fp.setActionTraversedMap(a);
                continue;
            }
            String fatherType = fafafather.getAstClass().getSimpleName();
//			System.out.print(nextAction);
            ClusteredActionBean operationBean;
            //类签名状态
            if(StatementConstants.TYPEDECLARATION.equals(fatherType)){
                //class signature
                operationBean = MatchClassSignature.matchClassSignature(fp,a,type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            }

            if(StatementConstants.FIELDDECLARATION.equals(type)){
                //delete FieldDeclaration
                operationBean = MatchFieldDeclaration.matchFieldDeclaration(fp,a,type);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            } else if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
                //delete FieldDeclaration body
                operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(fp,a,type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            }

            if(StatementConstants.INITIALIZER.equals(type) && StatementConstants.TYPEDECLARATION.equals(((Tree)a.getNode().getParent()).getAstClass().getSimpleName())){
                //insert INITIALIZER
                MatchInitializerBlock.matchInitializerBlock(fp, a,type);
                continue;
            }

            if (StatementConstants.METHODDECLARATION.equals(type)) {
                // 删除方法体
                //ClusteredActionBean bean  = MatchMethod.matchNewOrDeleteMethod(fp,a,type);
                operationBean = MatchMethod.matchNewOrDeleteMethod(fp,a,type);
                fp.mHighLevelOperationBeanList.add(operationBean);
            }else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
                // 删除方法参数
                operationBean = MatchMethodSignatureChange.matchMethodSignatureChange(fp,a, type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
            } else {
                // 方法体内部
                switch (type) {
                    case StatementConstants.IFSTATEMENT:
                        operationBean = MatchIfElse.matchIf(fp,a, type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.BLOCK:
                        MatchBlock.matchBlock(fp,a, type);
                        break;
                    case StatementConstants.BREAKSTATEMENT:
                        if(AstRelations.isFatherSwitchStatement(a)) {
                            //删除switch语句
                            operationBean = MatchSwitch.matchSwitchCaseByFather(fp, a, type, fafafather, fatherType);
                            fp.mHighLevelOperationBeanList.add(operationBean);
                        }else {
                            System.out.println("Other Condition"+ActionConstants.getInstanceStringName(a) + " " +type);
                            fp.setActionTraversedMap(a);
                            // TODO剩下的情况
                        }
                        break;
                    case StatementConstants.RETURNSTATEMENT:
                        operationBean = MatchReturnStatement.matchReturnStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.FORSTATEMENT:
                        //算出for语句
                        operationBean = MatchForStatement.matchForStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.ENHANCEDFORSTATEMENT:
                        //删除for语句
                        operationBean = MatchForStatement.matchEnhancedForStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.WHILESTATEMENT:
                        //删除while语句
                        operationBean = MatchWhileStatement.matchWhileStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.DOSTATEMENT:
                        //删除do while语句
                        operationBean = MatchWhileStatement.matchDoStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.TRYSTATEMENT:
                        operationBean = MatchTry.matchTry(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.THROWSTATEMENT:
                        operationBean = MatchTry.matchThrowStatement(fp,a,type,fafafather,fatherType);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
                        operationBean = MatchVariableDeclarationExpression.matchVariableDeclaration(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.EXPRESSIONSTATEMENT:
                        // Pattern 1.2 Match else
                        if (AstRelations.isFatherIfStatement(a) && a.getNode().getParent().getChildPosition(a.getNode())== 2) {
                            operationBean = MatchIfElse.matchElse(fp, a, type, fafafather, fatherType);
                            fp.mHighLevelOperationBeanList.add(operationBean);
                        }
                        else {
                            operationBean = MatchExpressionStatement.matchExpression(fp, a, type);
                            fp.mHighLevelOperationBeanList.add(operationBean);
                        }
                        break;
                    case StatementConstants.CONDITIONALEXPRESSION:
                        operationBean = MatchConditionalExpression.matchConditionalExpression(fp, a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.SYNCHRONIZEDSTATEMENT:
                        //同步语句块删除
                        operationBean = MatchSynchronized.matchSynchronized(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.SWITCHSTATEMENT:
                        //switch语句
                        operationBean = MatchSwitch.matchSwitch(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.SWITCHCASE:
                        //删除switchcase语句
                        operationBean = MatchSwitch.matchSwitchCase(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.JAVADOC:
                        //删除javadoc
                        operationBean = MatchJavaDoc.matchJavaDoc(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.CONSTRUCTORINVOCATION:
                        //构造方法this
                        operationBean = MatchMethod.matchConstructorInvocation(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.SUPERCONSTRUCTORINVOCATION:
                        //构造方法super
                        operationBean = MatchMethod.matchSuperConstructorInvocation(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    //JAVADOC参数
                    case StatementConstants.TAGELEMENT:
                    case StatementConstants.TEXTELEMENT:
                        // 方法参数
                    case StatementConstants.SIMPLENAME:
                    case StatementConstants.SIMPLETYPE:
                    case StatementConstants.STRINGLITERAL:
                    case StatementConstants.NULLLITERAL:
                    case StatementConstants.PREFIXEXPRESSION:
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
                    case StatementConstants.ASSIGNMENT:
                        MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(fp,a, type);
                        break;
                    default:
                        String operationEntity = "DEFAULT: "+ActionConstants.getInstanceStringName(a) + " " +type;
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
