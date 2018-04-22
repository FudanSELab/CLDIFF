package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntity;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import org.eclipse.jdt.core.dom.ASTNode;

public class MatchNonStatement {


    public static void matchNonStatement(MiningActionData fp, Action a) {

        ITree fafather1 = BasicTreeTraversal.findFafatherNode(a.getNode());
        ITree[] fathers = BasicTreeTraversal.getMappedFafatherNode(fp, a, fafather1);
        Tree srcFather = (Tree) fathers[0];
        Tree dstFather = (Tree) fathers[1];
        Tree queryFather = null;
        int treeType = -1;
        if (srcFather == null && dstFather != null) {
            queryFather = dstFather;
            treeType = ChangeEntityDesc.StageITreeType.DST_TREE_NODE;
        } else if (srcFather != null) {
            queryFather = srcFather;
            treeType = ChangeEntityDesc.StageITreeType.SRC_TREE_NODE;
        }

        ChangeEntity changeEntity;
        changeEntity = fp.getEntityByNode(queryFather);
        if(changeEntity!=null &&changeEntity.clusteredActionBean.curAction instanceof Move){
            changeEntity = null;
        }
        if (changeEntity == null || (a instanceof Move)) {
            if (a instanceof Insert) {
                matchNodeNewEntity(fp, a, queryFather,treeType, dstFather);
            } else {
                matchNodeNewEntity(fp, a, queryFather,treeType, srcFather);
            }
        } else {
            if (a instanceof Insert) {
                matchXXXChangeCurEntity(fp, a, changeEntity, dstFather);
            } else {
                matchXXXChangeCurEntity(fp, a, changeEntity, srcFather);
            }
        }
    }


    public static void matchNodeNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType, Tree traverseFather) {
        int nodeType = traverseFather.getAstNode().getNodeType();
        switch (nodeType) {
            case ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassSignatureNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclarationChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.INITIALIZER:
                break;
            case ASTNode.METHOD_DECLARATION:
                if (((Tree) a.getNode()).getAstNode().getNodeType() != ASTNode.BLOCK) {
                    MatchMethod.matchMethodSignatureChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                }
                break;
            case ASTNode.ENUM_DECLARATION:
            case ASTNode.ENUM_CONSTANT_DECLARATION:
                MatchEnum.matchEnumDeclarationNewEntity(fp,a,queryFather,treeType,traverseFather);
                break;
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIfPredicateChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.FOR_STATEMENT:
                MatchForStatement.matchForConditionChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.WHILE_STATEMENT:
                MatchWhileStatement.matchWhileConditionChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.DO_STATEMENT:
                MatchWhileStatement.matchDoConditionChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                MatchForStatement.matchEnhancedForConditionChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclarationNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                MatchExpressionStatement.matchExpressionChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.RETURN_STATEMENT:
                MatchReturnStatement.matchReturnChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.ASSERT_STATEMENT:
                MatchAssert.matchAssertChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.CATCH_CLAUSE:
                MatchTry.matchCatchChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.SYNCHRONIZED_STATEMENT:
                MatchSynchronized.matchSynchronizedChangeNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.SWITCH_STATEMENT:
                MatchSwitch.matchSwitchNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.SWITCH_CASE:
                MatchSwitch.matchSwitchCaseNewEntity(fp, a, queryFather,treeType, traverseFather);
                break;
            case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
//                System.err.println("aaa-----");
                MatchConstructorInvocation.matchSuperConstructorInvocationNewEntity(fp,a,queryFather,treeType,traverseFather);
                break;
            case ASTNode.CONSTRUCTOR_INVOCATION:
                MatchConstructorInvocation.matchConstructorInvocationNewEntity(fp,a,queryFather,treeType,traverseFather);
                break;
            case ASTNode.LABELED_STATEMENT:
                MatchLabeledStatement.matchLabeledStatementNewEntity(fp,a,queryFather,treeType,traverseFather);
                break;
            default:
                break;
        }
    }

    public static void matchXXXChangeCurEntity(MiningActionData fp, Action a, ChangeEntity changeEntity, Tree traverseFather) {
        Tree queryFather = (Tree) changeEntity.clusteredActionBean.fafather;
        int nodeType = queryFather.getAstNode().getNodeType();
        switch (nodeType) {
            case ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassSignatureCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclarationChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.INITIALIZER:
                break;
            case ASTNode.METHOD_DECLARATION:
                if (((Tree) a.getNode()).getAstNode().getNodeType() != ASTNode.BLOCK) {
                    MatchMethod.matchMethodSignatureChangeCurrEntity(fp, a, changeEntity, traverseFather);
                }
                break;
            case ASTNode.ENUM_DECLARATION:
            case ASTNode.ENUM_CONSTANT_DECLARATION:
                MatchEnum.matchEnumDeclarationCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIfPredicateChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.FOR_STATEMENT:
                MatchForStatement.matchForConditionChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.WHILE_STATEMENT:
                MatchWhileStatement.matchWhileConditionChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.DO_STATEMENT:
                MatchWhileStatement.matchDoConditionChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                MatchForStatement.matchEnhancedForConditionChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclarationCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                MatchExpressionStatement.matchExpressionChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;

            case ASTNode.RETURN_STATEMENT:
                MatchReturnStatement.matchReturnChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.ASSERT_STATEMENT:
                MatchAssert.matchAssertChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.CATCH_CLAUSE:
                MatchTry.matchCatchChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.SYNCHRONIZED_STATEMENT:
                MatchSynchronized.matchSynchronizedChangeCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.SWITCH_STATEMENT:
                MatchSwitch.matchSwitchCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.SWITCH_CASE:
                MatchSwitch.matchSwitchCaseCurrEntity(fp, a, changeEntity, traverseFather);
                break;
            case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
                MatchConstructorInvocation.matchSuperConstructorInvocationCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.CONSTRUCTOR_INVOCATION:
                MatchConstructorInvocation.matchConstructorInvocationCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.LABELED_STATEMENT:
                MatchLabeledStatement.matchLabeledStatementCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            default:
                break;
        }


    }

}
