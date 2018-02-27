package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.AbstractTree;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;

import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.BasicTreeTraversal;
import edu.fdu.se.astdiff.miningactions.util.DefaultDownUpTraversal;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
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
            matchNodeNewEntity(fp, a, queryFather);
        } else {
            matchXXXChangeCurEntity(fp,changeEntity);
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


    public static void matchNodeNewEntity(MiningActionData fp,Action a,Tree fafather){
        int nodeType = fafather.getAstNode().getNodeType();
        String nodeStr = fafather.getAstClass().getSimpleName();
        switch (nodeType) {
            case  ASTNode.TYPE_DECLARATION:
                MatchClass.matchClassSignatureNewEntity(fp, a, fafather);
                break;
            case ASTNode.FIELD_DECLARATION:
                MatchFieldDeclaration.matchFieldDeclarationChangeNewEntity(fp, a, fafather);
                break;
            case ASTNode.INITIALIZER:
                break;
            case ASTNode.METHOD_DECLARATION:
                if (((Tree)a.getNode()).getAstNode().getNodeType()!=ASTNode.BLOCK) {
                    MatchMethod.matchMethodSignatureChangeNewEntity(fp, a, fafather);
                }
                break;
            case ASTNode.IF_STATEMENT:
                MatchIfElse.matchIfPredicateChangeNewEntity(fp, a, fafather);
                break;
            case ASTNode.FOR_STATEMENT:
                MatchForStatement.matchForPredicate(fp, a, fafather);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                MatchForStatement.matchEnhancedForPredicate(fp, a, fafather);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                MatchVariableDeclarationExpression.matchVariableDeclarationNewEntity(fp, a, fafather);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                MatchExpressionStatement.matchExpressionByFather(fp, a, fafather);
                break;
            case ASTNode.SWITCH_CASE:
                MatchSwitch.matchSwitchCaseByFather(fp, a);
                break;
            case ASTNode.RETURN_STATEMENT:
                MatchReturnStatement.matchReturnStatentByFather(fp, a, fafather);
                break;
            default:
                break;
        }

    }

    public static void matchXXXChangeCurEntity(MiningActionData fp,ChangeEntity changeEntity) {
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> signatureChildren = changeEntity.clusteredActionBean.actions;
        List<Action> sameEditActions = new ArrayList<>();
        Tree fatherNode;
        if(changeEntity.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
            fatherNode = (Tree) changeEntity.clusteredActionBean.getCurAction().getNode();
        }else{
            fatherNode = (Tree) changeEntity.clusteredActionBean.getFatherNode();
        }
        DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(fatherNode,sameEditActions,changePacket);
        for(Action tmp:sameEditActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            signatureChildren.add(tmp);
        }
        fp.setActionTraversedMap(sameEditActions);

    }

}
