package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ClusterInsertOrDelete {

    private List<Action> actionList;
    private MiningActionData fp;
    private Class clazz;

    public ClusterInsertOrDelete(Class mClazz, MiningActionData mminingActionData) {
        this.fp = mminingActionData;
        this.clazz = mClazz;
        if (Insert.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getInsertActions();
        } else if (Delete.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getDeleteActions();
        }
    }


    public void doCluster() {
        int insertActionCount = this.actionList.size();
        int insertActionIndex = 0;
        while (true) {
            if (insertActionIndex == insertActionCount) {
                break;
            }
            Action a = this.actionList.get(insertActionIndex);
            insertActionIndex++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                // 标记过的action
                continue;
            }
            Tree insNode = (Tree) a.getNode();
            String type = insNode.getAstClass().getSimpleName();
            Tree fafafather = AstRelations.findFafafatherNode(insNode);
            if (fafafather == null) {
                System.out.println("Father Null Condition: " + ActionConstants.getInstanceStringName(a) + " " + type);
                fp.setActionTraversedMap(a);
                continue;
            }
            String fatherType = fafafather.getAstClass().getSimpleName();

            ClusteredActionBean operationBean = null;
            boolean isBody = true;
            switch (type) {
                case StatementConstants.TYPEDECLARATION:
                    break;
                case StatementConstants.FIELDDECLARATION:
                    operationBean = MatchFieldDeclaration.matchFieldDeclaration(fp, a, type);
                    break;
                case StatementConstants.INITIALIZER:
                    MatchInitializerBlock.matchInitializerBlock(fp, a, type);
                    break;
                case StatementConstants.METHODDECLARATION:
                    operationBean = MatchMethod.matchNewOrDeleteMethod(fp, a, type);
                    break;
                default:
                    isBody = false;
            }
            if (isBody)
                continue;
            boolean isSignature = true;

            switch (fatherType) {
                case StatementConstants.TYPEDECLARATION:
                    operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(fp, a, type, fafafather, fatherType);
                    break;
                case StatementConstants.FIELDDECLARATION:
                    operationBean = MatchFieldDeclaration.matchFieldDeclarationByFather(fp, a, type, fafafather, fatherType);
                    break;
                case StatementConstants.INITIALIZER:
                    break;
                case StatementConstants.METHODDECLARATION:
                    operationBean = MatchMethodSignatureChange.matchMethodSignatureChange(fp, a, type, fafafather, fatherType);
                    break;
                default:
                    isSignature = false;
            }
            if (isSignature)
                continue;

            // body 内部
            switch (type) {
                case StatementConstants.IFSTATEMENT:
                    operationBean = MatchIfElse.matchIf(fp, a, type);
                    break;
                case StatementConstants.BLOCK:
                    MatchBlock.matchBlock(fp, a, type);
                    break;
                case StatementConstants.BREAKSTATEMENT:
                    if (AstRelations.isFatherSwitchStatement(a)) {
                        //增加switch语句
                        operationBean = MatchSwitch.matchSwitchCaseByFather(fp, a, type, fafafather, fatherType);
                    } else {
                        System.out.println("Other Condition" + ActionConstants.getInstanceStringName(a) + " " + type);
                        fp.setActionTraversedMap(a);
                    }
                    break;
                case StatementConstants.RETURNSTATEMENT:
                    operationBean = MatchReturnStatement.matchReturnStatement(fp, a, type);
                    break;
                case StatementConstants.FORSTATEMENT:
                    //增加for语句
                    operationBean = MatchForStatement.matchForStatement(fp, a, type);
                    break;
                case StatementConstants.ENHANCEDFORSTATEMENT:
                    //增加for语句
                    operationBean = MatchForStatement.matchEnhancedForStatement(fp, a, type);
                    break;
                case StatementConstants.WHILESTATEMENT:
                    //增加while语句
                    operationBean = MatchWhileStatement.matchWhileStatement(fp, a, type);
                    break;
                case StatementConstants.DOSTATEMENT:
                    //增加do while语句
                    operationBean = MatchWhileStatement.matchDoStatement(fp, a, type);
                    break;
                case StatementConstants.TRYSTATEMENT:
                    operationBean = MatchTry.matchTry(fp, a, type);
                    break;
                case StatementConstants.THROWSTATEMENT:
                    operationBean = MatchTry.matchThrowStatement(fp, a, type, fafafather, fatherType);
                    break;
                case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
                    operationBean = MatchVariableDeclarationExpression.matchVariableDeclaration(fp, a, type);
                    break;
                case StatementConstants.EXPRESSIONSTATEMENT:
                    if (AstRelations.isFatherIfStatement(a) && a.getNode().getParent().getChildPosition(a.getNode()) == 2) {
                        // Pattenr 1.2 Match else
                        operationBean = MatchIfElse.matchElse(fp, a, type, fafafather, fatherType);
                    } else {
                        operationBean = MatchExpressionStatement.matchExpression(fp, a, type);
                    }
                    break;
                case StatementConstants.CONDITIONALEXPRESSION:
                    operationBean = MatchConditionalExpression.matchConditionalExpression(fp, a, type);
                    break;
                case StatementConstants.SYNCHRONIZEDSTATEMENT:
                    //同步语句块增加
                    operationBean = MatchSynchronized.matchSynchronized(fp, a, type);
                    break;
                case StatementConstants.SWITCHSTATEMENT:
                    //增加switch语句
                    operationBean = MatchSwitch.matchSwitch(fp, a, type);
                    break;
                case StatementConstants.SWITCHCASE:
                    //增加switch语句
                    operationBean = MatchSwitch.matchSwitchCase(fp, a, type);
                    break;
                case StatementConstants.JAVADOC:
                    //增加javadoc
                    operationBean = MatchJavaDoc.matchJavaDoc(fp, a, type);
                    break;
                case StatementConstants.CONSTRUCTORINVOCATION:
                    //构造方法this
                    operationBean = MatchMethod.matchConstructorInvocation(fp, a, type);
                    break;
                case StatementConstants.SUPERCONSTRUCTORINVOCATION:
                    //构造方法super
                    operationBean = MatchMethod.matchSuperConstructorInvocation(fp, a, type);
                    break;
                case StatementConstants.TAGELEMENT:
                case StatementConstants.TEXTELEMENT:
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
                    MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(fp, a, type);
                    break;
                default:
                    String operationEntity = "DEFAULT: " + ActionConstants.getInstanceStringName(a) + " " + type;
                    Range nodeLinePosition = AstRelations.getnodeLinePosition(a);
                    operationBean = new ClusteredActionBean(a, type, null, nodeLinePosition, -1, operationEntity, fafafather, fatherType);
                    fp.setActionTraversedMap(a);
                    break;
            }
            if(operationBean!=null)
                fp.mHighLevelOperationBeanList.add(operationBean);
        }
    }
}
