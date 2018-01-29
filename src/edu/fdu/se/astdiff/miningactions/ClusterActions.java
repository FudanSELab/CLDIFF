package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.Body.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class ClusterActions {
    //		ITree tree = a.getNode();
//		Tree parent = (Tree) tree.getParent();
//		MethodInvocation mi = (MethodInvocation) parent.getAstNode();
//		System.out.println("MethodInvocation:");
//		System.out.println(mi.getName());// method call的方法名
//		System.out.println(mi.getExpression());// variable
//		int pos = tree.getParent().getChildPosition(tree);

    public static void doCluster(MiningActionData fpd) {
        new ClusterActions(Insert.class, fpd).doCluster();
        new ClusterActions(Delete.class, fpd).doCluster();
        new ClusterActions(Move.class, fpd).doCluster();
        new ClusterActions(Update.class, fpd).doClusterUpdate();

    }


    private List<Action> actionList;
    private MiningActionData fp;

    public ClusterActions(Class mClazz, MiningActionData mminingActionData) {
        this.fp = mminingActionData;
        Class clazz = mClazz;
//        this.clazz = mClazz;
        if (Insert.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getInsertActions();
        } else if (Delete.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getDeleteActions();
        } else if (Move.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getMoveActions();
        } else {
            this.actionList = mminingActionData.mGeneratingActionsData.getUpdateActions();
        }
    }


    public int processSmallAction(Action a,String type) {
        int res = 0;
        switch(type){
            case StatementConstants.TAGELEMENT:
            case StatementConstants.TEXTELEMENT:
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
                MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(fp, a);
                break;
            default:
                res= 1 ;
                break;

        }
        return res;

    }

    public int processBigAction(Action a,String type) {
        int res = 0;
        switch (type) {
            // 外面
            case StatementConstants.TYPEDECLARATION:
                MatchClass.matchClassAddOrDelete(fp, a);
                break;
            case StatementConstants.FIELDDECLARATION:
                MatchFieldDeclaration.matchFieldDeclaration(fp, a);
                break;
            case StatementConstants.INITIALIZER:
                MatchInitializerBlock.matchInitializerBlock(fp, a, type);
                break;
            case StatementConstants.METHODDECLARATION:
                MatchMethod.matchNewOrDeleteMethod(fp, a);
                break;


            // 里面
            case StatementConstants.IFSTATEMENT:
                MatchIfElse.matchIf(fp, a, type);
                break;
            case StatementConstants.BLOCK:
                MatchBlock.matchBlock(fp, a, type);
                break;
            case StatementConstants.BREAKSTATEMENT:
                if (AstRelations.isFatherXXXStatement(a, StatementConstants.SWITCHSTATEMENT)) {
                    //增加switch语句
                    MatchSwitch.matchSwitchCaseByFather(fp, a);
                } else {
                    System.out.println("Other Condition" + a.getClass().getSimpleName() + " " + type);
                    fp.setActionTraversedMap(a);
                }
                break;
            case StatementConstants.RETURNSTATEMENT:
                MatchReturnStatement.matchReturnStatement(fp, a, type);
                break;
            case StatementConstants.FORSTATEMENT:
                //增加for语句
                MatchForStatement.matchForStatement(fp, a, type);
                break;
            case StatementConstants.ENHANCEDFORSTATEMENT:
                //增加for语句
                MatchForStatement.matchEnhancedForStatement(fp, a, type);
                break;
            case StatementConstants.WHILESTATEMENT:
                //增加while语句
                MatchWhileStatement.matchWhileStatement(fp, a, type);
                break;
            case StatementConstants.DOSTATEMENT:
                //增加do while语句
                MatchWhileStatement.matchDoStatement(fp, a, type);
                break;
            case StatementConstants.TRYSTATEMENT:
                MatchTry.matchTry(fp, a, type);
                break;
            case StatementConstants.THROWSTATEMENT:
                MatchTry.matchThrowStatement(fp, a, type);
                break;
            case StatementConstants.VARIABLEDECLARATIONSTATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclaration(fp, a, type);
                break;
            case StatementConstants.EXPRESSIONSTATEMENT:
                if (AstRelations.isFatherXXXStatement(a, StatementConstants.IFSTATEMENT) && a.getNode().getParent().getChildPosition(a.getNode()) == 2) {
                    // Pattenr 1.2 Match else
                    MatchIfElse.matchElse(fp, a, type);
                } else {
                    MatchExpressionStatement.matchExpression(fp, a);
                }
                break;
//                case StatementConstants.CONDITIONALEXPRESSION:
//                    MatchConditionalExpression.matchConditionalExpression(fp, a, type);
//                    break;
            case StatementConstants.SYNCHRONIZEDSTATEMENT:
                //同步语句块增加
                MatchSynchronized.matchSynchronized(fp, a, type);
                break;
            case StatementConstants.SWITCHSTATEMENT:
                //增加switch语句
                MatchSwitch.matchSwitch(fp, a, type);
                break;
            case StatementConstants.SWITCHCASE:
                //增加switch语句
                MatchSwitch.matchSwitchCase(fp, a, type);
                break;
//            case StatementConstants.JAVADOC:
//                //增加javadoc
//                MatchJavaDoc.matchJavaDoc(fp, a, type);
//                break;
//            case StatementConstants.CONSTRUCTORINVOCATION:
//                //构造方法this
//                MatchMethod.matchConstructorInvocation(fp, a);
//                break;
//            case StatementConstants.SUPERCONSTRUCTORINVOCATION:
//                //构造方法super
//                MatchMethod.matchSuperConstructorInvocation(fp, a, type);
//                break;
            default:
                res =1;
                break;
        }
        return  res;
    }

    public void doCluster() {
        int actionCnt = this.actionList.size();
        int index = 0;
        while (index != actionCnt) {
            Action a = this.actionList.get(index);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree insNode = (Tree) a.getNode();
            String type = insNode.getAstClass().getSimpleName();
            if(processBigAction(a,type)==1) {
                if(processSmallAction(a,type)==1) {
                    System.err.println(SimpleActionPrinter.getMyOneActionString(a));
                }
            }
        }
    }


    public void doClusterUpdate() {
        int updateActionCount = this.actionList.size();
        int index = 0;
        while (index != updateActionCount) {
            Action a = this.actionList.get(index);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree tmp = (Tree) a.getNode();
            String type = tmp.getAstClass().getSimpleName();
            switch (type) {
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
                case StatementConstants.MODIFIER:
                case StatementConstants.MARKERANNOTATION:
                case StatementConstants.NORMALANNOTATION:
                case StatementConstants.SINGLEMEMBERANNOTATION:
                    MatchSimpleNameOrLiteral.matchSimplenameOrLiteral(fp, a);
                    break;
                default:
                    break;
            }
        }
    }

}
