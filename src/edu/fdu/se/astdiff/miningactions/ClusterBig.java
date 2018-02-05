package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.Body.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/2.
 *
 */
public class ClusterBig extends AbstractCluster{

    public ClusterBig(Class mClazz, MiningActionData mminingActionData) {
        super(mClazz, mminingActionData);
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
            case ASTNode.ASSERT_STATEMENT:
                MatchAssert.matchAssert(fp,a);
                break;
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIf(fp, a);
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
                MatchReturnStatement.matchReturnStatement(fp, a);
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
                MatchWhileStatement.matchDoStatement(fp, a);
                break;
            case ASTNode.TRY_STATEMENT:
                MatchTry.matchTry(fp, a);
                break;
            case ASTNode.THROW_STATEMENT:
                MatchTry.matchThrowStatement(fp, a);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclaration(fp, a);
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
                MatchSynchronized.matchSynchronized(fp, a);
                break;
            case ASTNode.SWITCH_STATEMENT:
                //增加switch语句
                MatchSwitch.matchSwitch(fp, a);
                break;
            case ASTNode.SWITCH_CASE:
                //增加switch语句
                MatchSwitch.matchSwitchCase(fp, a);
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
}
