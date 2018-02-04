package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.statement.MatchTry;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.util.DefaultUpDownTraversal;
import edu.fdu.se.astdiff.miningactions.util.StatementConstants;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.InitializerChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class MatchInitializerBlock {

    public static void matchInitializerBlock(MiningActionData fp, Action a) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        changePacket.setOperationType(OperationTypeConstants.getEditTypeIntCode(a));
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        DefaultUpDownTraversal.traverseInitializer(a,subActions,changePacket);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,changePacket,range);
        InitializerChangeEntity code = new InitializerChangeEntity(mBean);
        fp.addOneChangeEntity(code);
        fp.setActionTraversedMap(subActions);

        Tree tt = (Tree) a.getNode();
        if(tt.getChildren().size()==1){
            Tree child = (Tree)tt.getChild(0);
            if(child.getAstNode().getNodeType()== ASTNode.BLOCK){
                code.staticOrNonStatic = "Non-static";
            }
        }else if(tt.getChildren().size()==2){
            Tree child1 = (Tree)tt.getChild(0);
            Tree child2 = (Tree)tt.getChild(1);
            if(child1.getAstNode().getNodeType()==ASTNode.MODIFIER && child2.getAstNode().getNodeType()==ASTNode.BLOCK
                    &&child1.getLabel()=="static"){
                code.staticOrNonStatic = "static";
            }
        }
    }

}

