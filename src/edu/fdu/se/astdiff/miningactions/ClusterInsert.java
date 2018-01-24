package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.generatingactions.ActionPrinter;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class ClusterInsert{

    public static void findInsert(MiningActionData fp) {
        int insertActionCount = fp.mGeneratingActionsData.getInsertActions().size();
        int insertActionIndex = 0;
        while (true) {
            if (insertActionIndex == insertActionCount) {
                break;
            }
            Action a = fp.mGeneratingActionsData.getInsertActions().get(insertActionIndex);
            String nextAction = SimpleActionPrinter.getMyOneActionString(a);
            insertActionIndex++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                // 标记过的action
                continue;
            }
            Insert ins = (Insert) a;
            Tree insNode = (Tree)ins.getNode();
            String type = insNode.getAstClass().getSimpleName();
            Tree fafafather = AstRelations.findFafafatherNode(insNode);
            if(fafafather == null){
                System.out.println("Father Null Condition: "+ ActionConstants.getInstanceStringName(a) + " " +type);
                fp.setActionTraversedMap(a);
                continue;
            }
            String fatherType = fafafather.getAstClass().getSimpleName();

            ClusteredActionBean operationBean;
            //类签名状态
            if(StatementConstants.TYPEDECLARATION.equals(fatherType)){
                //class signature
                operationBean = MatchClassSignature.matchClassSignature(fp,a,type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            }


            if(StatementConstants.FIELDDECLARATION.equals(type)){
                //insert FieldDeclaration
                operationBean = MatchFieldDeclaration.matchFieldDeclaration(fp,a,type);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            } else if(StatementConstants.FIELDDECLARATION.equals(fatherType)){
                //insert FieldDeclaration body
                operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(fp,a,type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            }

            if(StatementConstants.INITIALIZER.equals(type) && StatementConstants.TYPEDECLARATION.equals( ((Tree)a.getNode().getParent()).getAstClass().getSimpleName())){
                //insert INITIALIZER
                MatchInitializerBlock.matchInitializerBlock(fp, a, type);
                continue;
            }

            if (StatementConstants.METHODDECLARATION.equals(type)) {
                operationBean = MatchMethod.matchNewOrDeleteMethod(fp,a,type);
                fp.mHighLevelOperationBeanList.add(operationBean);
                continue;
            } else if (StatementConstants.METHODDECLARATION.equals(fatherType)) {
//                System.out.println(insNode.getParent().getChildPosition(insNode));
                operationBean = MatchMethodSignatureChange.matchMethodSignatureChange(fp,a, type,fafafather,fatherType);
                fp.mHighLevelOperationBeanList.add(operationBean);
            } else {
                // 方法体
                switch (type) {
                    case StatementConstants.IFSTATEMENT:
                        // Pattern 1. Match If/else if
                        operationBean = MatchIfElse.matchIf(fp,a, type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.BLOCK:
                        MatchBlock.matchBlock(fp,a, type);
                        break;
                    case StatementConstants.BREAKSTATEMENT:
                        if(AstRelations.isFatherSwitchStatement(a)) {
                            //增加switch语句
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
                        //增加for语句
                        operationBean = MatchForStatement.matchForStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.ENHANCEDFORSTATEMENT:
                        //增加for语句
                        operationBean = MatchForStatement.matchEnhancedForStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.WHILESTATEMENT:
                        //增加while语句
                        operationBean = MatchWhileStatement.matchWhileStatement(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.DOSTATEMENT:
                        //增加do while语句
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
                        if(AstRelations.isFatherIfStatement(a) && a.getNode().getParent().getChildPosition(a.getNode())== 2) {
                            // Pattenr 1.2 Match else
                            operationBean = MatchIfElse.matchElse(fp, a,type,fafafather,fatherType);
                            fp.mHighLevelOperationBeanList.add(operationBean);
                        }
                        else {
                            operationBean = MatchExpressionStatement.matchExpression(fp, a,type);
                            fp.mHighLevelOperationBeanList.add(operationBean);
                        }
                        break;
                    case StatementConstants.CONDITIONALEXPRESSION:
                        operationBean = MatchConditionalExpression.matchConditionalExpression(fp, a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.SYNCHRONIZEDSTATEMENT:
                        //同步语句块增加
                        operationBean = MatchSynchronized.matchSynchronized(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.SWITCHSTATEMENT:
                        //增加switch语句
                        operationBean = MatchSwitch.matchSwitch(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.SWITCHCASE:
                        //增加switch语句
                        operationBean = MatchSwitch.matchSwitchCase(fp,a,type);
                        fp.mHighLevelOperationBeanList.add(operationBean);
                        break;
                    case StatementConstants.JAVADOC:
                        //增加javadoc
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
