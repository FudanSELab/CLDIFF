package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.Body.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import org.eclipse.jdt.core.dom.ASTNode;

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

        //big
        new ClusterActions(Insert.class, fpd).doClusterBig();
        new ClusterActions(Delete.class, fpd).doClusterBig();
        new ClusterActions(Move.class, fpd).doClusterBig();
        //small
        new ClusterActions(Insert.class, fpd).doClusterSmall();
        new ClusterActions(Delete.class, fpd).doClusterSmall();
        new ClusterActions(Move.class, fpd).doClusterSmall();
        new ClusterActions(Update.class, fpd).doClusterUpdate();

    }


    private List<Action> actionList;
    private MiningActionData fp;

    public ClusterActions(Class mClazz, MiningActionData mminingActionData) {
        this.fp = mminingActionData;
        Class clazz = mClazz;
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

    public int processSmallAction(Action a,int type) {
        int res = 0;
        switch(type){
            case ASTNode.TAG_ELEMENT:
            case ASTNode.TEXT_ELEMENT:
            case ASTNode.SIMPLE_NAME:
            case ASTNode.SIMPLE_TYPE:
            case ASTNode.STRING_LITERAL:
            case ASTNode.NULL_LITERAL:
            case ASTNode.PREFIX_EXPRESSION:
            case ASTNode.CHARACTER_LITERAL:
            case ASTNode.NUMBER_LITERAL:
            case ASTNode.BOOLEAN_LITERAL:
            case ASTNode.INFIX_EXPRESSION:
            case ASTNode.METHOD_INVOCATION:
            case ASTNode.QUALIFIED_NAME:
            case ASTNode.MODIFIER:
            case ASTNode.MARKER_ANNOTATION:
            case ASTNode.NORMAL_ANNOTATION:
            case ASTNode.SINGLE_MEMBER_ANNOTATION:
            case ASTNode.ASSIGNMENT:
                MatchSimpleNameOrLiteral.matchSimpleNameOrLiteral(fp, a);
                break;
            default:
                res= 1;
                break;
        }
        return res;

    }

    public int processBigAction(Action a,int type) {
        int res = 0;
        switch (type) {
            // 外面
            case ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassDeclaration(fp, a);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclaration(fp, a);
                break;
            case ASTNode.INITIALIZER:
                MatchInitializerBlock.matchInitializerBlock(fp, a);
                break;
            case ASTNode.METHOD_DECLARATION:
                MatchMethod.matchMethdDeclaration(fp, a);
                break;

            // 里面
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIf(fp, a, type);
                break;
            case ASTNode.BLOCK:
                MatchBlock.matchBlock(fp, a);
                break;
            case ASTNode.BREAK_STATEMENT:
                if (AstRelations.isFatherXXXStatement(a, ASTNode.SWITCH_STATEMENT)) {
                    //增加switch语句
                    MatchSwitch.matchSwitchCaseByFather(fp, a);
                } else {
                    System.out.println("Other Condition" + a.getClass().getSimpleName() + " " + type);
                    fp.setActionTraversedMap(a);
                }
                break;
            case ASTNode.RETURN_STATEMENT:
                MatchReturnStatement.matchReturnStatement(fp, a, type);
                break;
            case ASTNode.FOR_STATEMENT:
                //增加for语句
                MatchForStatement.matchForStatement(fp, a);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                //增加for语句
                MatchForStatement.matchEnhancedForStatement(fp, a);
                break;
            case ASTNode.WHILE_STATEMENT:
                //增加while语句
                MatchWhileStatement.matchWhileStatement(fp, a, type);
                break;
            case ASTNode.DO_STATEMENT:
                //增加do while语句
                MatchWhileStatement.matchDoStatement(fp, a, type);
                break;
            case ASTNode.TRY_STATEMENT:
                MatchTry.matchTry(fp, a, type);
                break;
            case ASTNode.THROW_STATEMENT:
                MatchTry.matchThrowStatement(fp, a);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclaration(fp, a, type);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                if (AstRelations.isFatherXXXStatement(a, ASTNode.IF_STATEMENT) && a.getNode().getParent().getChildPosition(a.getNode()) == 2) {
                    // Pattenr 1.2 Match else
                    MatchIfElse.matchElse(fp, a);
                } else {
                    MatchExpressionStatement.matchExpression(fp, a);
                }
                break;
//                case ASTNode.CONDITIONALEXPRESSION:
//                    MatchConditionalExpression.matchConditionalExpression(fp, a, type);
//                    break;
            case ASTNode.SYNCHRONIZED_STATEMENT:
                //同步语句块增加
                MatchSynchronized.matchSynchronized(fp, a, type);
                break;
            case ASTNode.SWITCH_STATEMENT:
                //增加switch语句
                MatchSwitch.matchSwitch(fp, a);
                break;
            case ASTNode.SWITCH_CASE:
                //增加switch语句
                MatchSwitch.matchSwitchCase(fp, a, type);
                break;
//            case ASTNode.JAVADOC:
//                //增加javadoc
//                MatchJavaDoc.matchJavaDoc(fp, a, type);
//                break;
//            case ASTNode.CONSTRUCTORINVOCATION:
//                //构造方法this
//                MatchMethod.matchConstructorInvocation(fp, a);
//                break;
//            case ASTNode.SUPERCONSTRUCTORINVOCATION:
//                //构造方法super
//                MatchMethod.matchSuperConstructorInvocation(fp, a, type);
//                break;
            default:
                res =1;
                break;
        }
        return  res;
    }

    public void doClusterBig() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree insNode = (Tree) a.getNode();
            if(processBigAction(a,insNode.getAstNode().getNodeType())==1) {

            }
        }
    }

    public void doClusterSmall() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree insNode = (Tree) a.getNode();
            if(processSmallAction(a,insNode.getAstNode().getNodeType())==1) {
                System.err.println(SimpleActionPrinter.getMyOneActionString(a));
            }
        }
    }


    public void doClusterUpdate() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            Tree tmp = (Tree) a.getNode();
            if(processSmallAction(a,tmp.getAstNode().getNodeType())==1){
                System.out.println(SimpleActionPrinter.getMyOneActionString(a));
            }
        }
    }

}
