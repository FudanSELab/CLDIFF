package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.AbstractTree;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

public class MatchSimpleNameOrLiteral {

    //todo
    public static void matchSimpleNameOrLiteral(MiningActionData fp, Action a) {
        ITree fafather1 = BasicTreeTraversal.findFafatherNode(a.getNode());
        ITree[] fathers = BasicTreeTraversal.getMappedFafatherNode(fp,a,fafather1);
        Tree srcFather = (Tree)fathers[0];
        Tree dstFather = (Tree)fathers[1];
        Tree queryFather = null;
        if(srcFather == null && dstFather!=null){
            queryFather = dstFather;
        }else if(srcFather !=null){
            queryFather = srcFather;
        }

        ChangeEntity changeEntity;
        changeEntity = MiningActionData.getEntityByNode(fp, queryFather);
        if(changeEntity==null){
            Tree moveNode = checkIfMoveActionInUppperNode(queryFather);
            if(moveNode!=null)
                changeEntity = MiningActionData.getEntityByNode(fp,queryFather);
        }
        // 如果节点往上有move标记那么找到move标记的ChangeEntity
        if (changeEntity == null) {
            if(a instanceof Insert) {
                matchNodeNewEntity(fp, a, queryFather,dstFather);
            }else{
                matchNodeNewEntity(fp,a,queryFather,srcFather);
            }
        } else {
            if(a instanceof Insert){
                matchXXXChangeCurEntity(fp,a,changeEntity,dstFather);
            }else{
                matchXXXChangeCurEntity(fp,a,changeEntity,srcFather);
            }
        }
    }

    private static Tree checkIfMoveActionInUppperNode(ITree node){
        Tree parent = (Tree)node;
        while(!parent.isRoot()){
            if(parent.getDoAction()!=null){
                List<Action> subActions = parent.getDoAction();
                for(Action tmp:subActions){
                    if(tmp instanceof Move){
                        return parent;
                    }
                }
            }
            ITree tree = parent.getParent();
            if(tree instanceof AbstractTree.FakeTree){
                break;
            }
            parent = (Tree)parent.getParent();
        }
        return null;
    }


    public static void matchNodeNewEntity(MiningActionData fp,Action a,Tree queryFather,Tree traverseFather){
        int nodeType = traverseFather.getAstNode().getNodeType();
        switch (nodeType) {
            case  ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassSignatureNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclarationChangeNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.INITIALIZER:
                break;
            case ASTNode.METHOD_DECLARATION:
                if (((Tree)a.getNode()).getAstNode().getNodeType()!=ASTNode.BLOCK) {
                    MatchMethod.matchMethodSignatureChangeNewEntity(fp, a, queryFather,traverseFather);
                }
                break;
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIfPredicateChangeNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.FOR_STATEMENT:
                MatchForStatement.matchForConditionChangeNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.WHILE_STATEMENT:
                MatchWhileStatement.matchWhileConditionChangeNewEntity(fp,a,queryFather,traverseFather);
                break;
            case ASTNode.DO_STATEMENT:
                MatchWhileStatement.matchDoConditionChangeNewEntity(fp,a,queryFather,traverseFather);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                MatchForStatement.matchEnhancedForConditionChangeNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclarationNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                MatchExpressionStatement.matchExpressionChangeNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.RETURN_STATEMENT:
                MatchReturnStatement.matchReturnChangeNewEntity(fp, a, queryFather,traverseFather);
                break;
            case ASTNode.ASSERT_STATEMENT:
                MatchAssert.matchAssertChangeNewEntity(fp,a,queryFather,traverseFather);
                break;
            case ASTNode.CATCH_CLAUSE:
                MatchTry.matchCatchChangeNewEntity(fp,a,queryFather,traverseFather);
                break;
            case ASTNode.SYNCHRONIZED_STATEMENT:
                MatchSynchronized.matchSynchronizedChangeNewEntity(fp,a,queryFather,traverseFather);
                break;
            case ASTNode.SWITCH_STATEMENT:
                MatchSwitch.matchSwitchNewEntity(fp,a);
                break;
            case ASTNode.SWITCH_CASE:
                MatchSwitch.matchSwitchCaseNewEntity(fp,a);
                break;
            default:
                break;
        }
    }

    public static void matchXXXChangeCurEntity(MiningActionData fp,Action a,ChangeEntity changeEntity,Tree traverseFather) {
        Tree queryFather = (Tree) changeEntity.clusteredActionBean.getFatherNode();
        int nodeType = queryFather.getAstNode().getNodeType();
        switch (nodeType) {
            case ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassSignatureCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclarationChangeCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.INITIALIZER:
                break;
            case ASTNode.METHOD_DECLARATION:
                if (((Tree)a.getNode()).getAstNode().getNodeType()!=ASTNode.BLOCK) {
                    MatchMethod.matchMethodSignatureChangeCurrEntity(fp, a, changeEntity,traverseFather);
                }
                break;
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIfPredicateChangeCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.FOR_STATEMENT:
                MatchForStatement.matchForConditionChangeCurrEntity(fp, a, changeEntity,traverseFather);
                break;
            case ASTNode.WHILE_STATEMENT:
                MatchWhileStatement.matchWhileConditionChangeCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.DO_STATEMENT:
                MatchWhileStatement.matchDoConditionChangeCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                MatchForStatement.matchEnhancedForConditionChangeCurrEntity(fp, a, changeEntity,traverseFather);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclarationCurrEntity(fp, a, changeEntity,traverseFather);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                MatchExpressionStatement.matchExpressionChangeCurrEntity(fp, a,changeEntity,traverseFather);
                break;

            case ASTNode.RETURN_STATEMENT:
                MatchReturnStatement.matchReturnChangeCurrEntity(fp, a, changeEntity,traverseFather);
                break;
            case ASTNode.ASSERT_STATEMENT:
                MatchAssert.matchAssertChangeCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.CATCH_CLAUSE:
                MatchTry.matchCatchChangeCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.SYNCHRONIZED_STATEMENT:
                MatchSynchronized.matchSynchronizedChangeCurrEntity(fp,a,changeEntity,traverseFather);
                break;
            case ASTNode.SWITCH_STATEMENT:
                MatchSwitch.matchSwitchCurrEntity(fp,a);
                break;
            case ASTNode.SWITCH_CASE:
                MatchSwitch.matchSwitchCaseCurrEntity(fp, a,changeEntity,traverseFather);
                break;
            default:
                break;
        }


    }

}
