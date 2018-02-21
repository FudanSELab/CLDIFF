package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class MatchSimpleNameOrLiteral {

    //todo
    public static void matchSimpleNameOrLiteral(MiningActionData fp, Action a) {
        ITree[] fathers = BasicTreeTraversal.getMappedFafatherNode(fp,a,a.getNode());
//        Tree fafather = BasicTreeTraversal.findFafatherNode(a.getNode());
//        Tree queryFather = BasicTreeTraversal.getQueryFafatherNode(fp, a, fafather);
        Tree srcFather = fathers[0]==null ? null:(Tree)fathers[0];
        Tree dstFather = fathers[1]==null ? null:(Tree)fathers[1];
        Tree queryFather =null;
        if(srcFather == null && dstFather!=null){
            queryFather = dstFather;
        }else if(srcFather !=null){
            queryFather = srcFather;
        }

        ChangeEntity changeEntity = null;
        changeEntity = MiningActionData.getEntityByNode(fp, queryFather);
        if(changeEntity==null){
            Tree moveNode = checkIfMoveActionInUppperNode(queryFather);
            if(moveNode!=null)
                changeEntity = MiningActionData.getEntityByNode(fp,queryFather);
        }
        // 如果节点往上有move标记那么找到move标记的ChangeEntity
        List<Action> sameEditAction = new ArrayList<>();
        if(changeEntity==null){
            matchNodeNewEntity(fp,a,queryFather,sameEditAction);
        }else{
            matchXXXChangeCurEntity(fp,a,changeEntity,sameEditAction);
        }
    }

    public static Tree checkIfMoveActionInUppperNode(ITree node){
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
            parent = (Tree)parent.getParent();
        }
        return null;
    }


    public static void matchNodeNewEntity(MiningActionData fp,Action a,Tree fafather,List<Action> sameEdits){
        int nodeType = fafather.getAstNode().getNodeType();
        String nodeStr = fafather.getAstClass().getSimpleName();
        switch (nodeType) {
            case  ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassSignatureNewEntity(fp, a, fafather,sameEdits);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclarationChangeNewEntity(fp, a, fafather,sameEdits);
                break;
            case ASTNode.INITIALIZER:
                break;
            case ASTNode.METHOD_DECLARATION:
                if (((Tree)a.getNode()).getAstNode().getNodeType()!=ASTNode.BLOCK) {
                    MatchMethod.matchMethodSignatureChangeNewEntity(fp, a, fafather,sameEdits);
                }
                break;
            case ASTNode.IF_STATEMENT:
//			System.out.println("If predicate");
                MatchIfElse.matchIfPredicateChangeNewEntity(fp, a, fafather,sameEdits);
                break;
            case ASTNode.FOR_STATEMENT:
//			System.out.println("For predicate");
                MatchForStatement.matchForPredicate(fp, a, fafather,sameEdits);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
//			System.out.println("Enhanced For predicate");
                MatchForStatement.matchEnhancedForPredicate(fp, a, fafather,sameEdits);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclarationByFather(fp, a, fafather,sameEdits);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
//			System.out.println("variable/expression");
                MatchExpressionStatement.matchExpressionByFather(fp, a, fafather,sameEdits);
                break;

            case ASTNode.SWITCH_CASE:
                //switchcase
                MatchSwitch.matchSwitchCaseByFather(fp, a);
                break;
            case ASTNode.RETURN_STATEMENT:
                //return statement
                MatchReturnStatement.matchReturnStatentByFather(fp, a, fafather,sameEdits);
                break;
//            case ASTNode.CONSTRUCTOR_INVOCATION:
//                //构造方法this
//                MatchMethod.matchConstructorInvocationByFather(fp, a, fafather);
//                break;
//            case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
//                //构造方法super
//                MatchMethod.matchSuperConstructorInvocationByFather(fp, a, fafather);
//                break;
            default:
                break;
        }

    }

    public static void matchXXXChangeCurEntity(MiningActionData fp, Action a,ChangeEntity changeEntity,List<Action> sameEditActions) {
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> signatureChildren = changeEntity.clusteredActionBean.actions;
        for(Action tmp:sameEditActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            signatureChildren.add(tmp);
        }
        fp.setActionTraversedMap(sameEditActions);

    }

}
