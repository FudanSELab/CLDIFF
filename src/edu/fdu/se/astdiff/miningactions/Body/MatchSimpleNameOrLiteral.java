package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import com.github.gumtreediff.utils.Pair;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class MatchSimpleNameOrLiteral {


    public static void matchSimplenameOrLiteral(MiningActionData fp, Action a) {
        Tree fafather = BasicTreeTraversal.findFafatherNode(a.getNode());
        ITree[] fatherPair = BasicTreeTraversal.getMappedFafatherNode(fp, a, fafather);
        Tree srcFafather = (Tree) fatherPair[0];
        Tree dstFafather = (Tree) fatherPair[1];
        Tree queryFather = null;
        if (srcFafather == null && dstFafather != null) {
            queryFather = dstFafather;
        } else if (srcFafather != null && dstFafather == null) {
            queryFather = srcFafather;
        } else if (srcFafather != null && dstFafather != null) {
            queryFather = srcFafather;
        }
        ChangeEntity changeEntity = MiningActionData.getEntityByNode(fp, queryFather);
        List<Action> sameEditAction = new ArrayList<>();
        DefaultDownUpTraversal.traverseFafather(a,fafather,sameEditAction);

        int nodeType = fafather.getAstNode().getNodeType();
        switch (nodeType) {
            case  ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassSignature(fp, a, fafather);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclarationByFather(fp, a, fafather);
                break;
            case ASTNode.INITIALIZER:
                break;
            case ASTNode.METHOD_DECLARATION:
                if (((Tree)a.getNode()).getAstNode().getNodeType()!=ASTNode.BLOCK) {
                    MatchMethod.matchMethodSignatureChange(fp, a, fafather);
                }
                break;
            case ASTNode.IF_STATEMENT:
//			System.out.println("If predicate");
                MatchIfElse.matchIfPredicate(fp, a, fafather, changeEntity);break;
            case ASTNode.FOR_STATEMENT:
//			System.out.println("For predicate");
                MatchForStatement.matchForPredicate(fp, a, fafather, changeEntity);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
//			System.out.println("Enhanced For predicate");
                MatchForStatement.matchEnhancedForPredicate(fp, a, fafather, changeEntity);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclarationByFather(fp, a, fafather, changeEntity);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
//			System.out.println("variable/expression");
                MatchExpressionStatement.matchExpressionByFather(fp, a, fafather, changeEntity);
                break;
//		case StatementConstants.JAVADOC:
//			operationBean = MatchJavaDoc.matchJavaDocByFather(fp,a,nodeType, fafafatherNode);
//			break;
            case ASTNode.SWITCH_CASE:
                //switchcase
                MatchSwitch.matchSwitchCaseByFather(fp, a, fafather, changeEntity);
                break;
            case ASTNode.RETURN_STATEMENT:
                //return statement
                MatchReturnStatement.matchReturnStatentByFather(fp, a, fafather);
                break;
            case ASTNode.CONSTRUCTOR_INVOCATION:
                //构造方法this
                MatchMethod.matchConstructorInvocationByFather(fp, a, fafather);
                break;
            case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
                //构造方法super
                MatchMethod.matchSuperConstructorInvocationByFather(fp, a, fafather);
                break;
            default:
                break;
        }
    }

}
