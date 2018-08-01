package edu.fdu.se.base.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/1.
 *
 */
public class MiningExpressionSubOpts {

    public List<String> miningActions(List<Action> actions){
        List<String> result = new ArrayList<>();
        for(Action a:actions){
            String optName = a.getClass().getSimpleName();
            Tree t = (Tree) a.getNode();
//            if (t.getAstNode().getNodeType())
        }
        return null;
    }

    public String expressionName(int nodeType){
        switch(nodeType){
            case ASTNode.NORMAL_ANNOTATION:
            case ASTNode.MARKER_ANNOTATION:
            case ASTNode.SINGLE_MEMBER_ANNOTATION:
            case ASTNode.ARRAY_CREATION:
            case ASTNode.ARRAY_INITIALIZER:
            case ASTNode.ASSIGNMENT:
            case ASTNode.BOOLEAN_LITERAL:
            case ASTNode.CAST_EXPRESSION:
            case ASTNode.CHARACTER_LITERAL:
            case ASTNode.CLASS_INSTANCE_CREATION:
            case ASTNode.CONDITIONAL_EXPRESSION:
            case ASTNode.CREATION_REFERENCE:
            case ASTNode.EXPRESSION_METHOD_REFERENCE:
            case ASTNode.FIELD_ACCESS:
            case ASTNode.INFIX_EXPRESSION:
            case ASTNode.INSTANCEOF_EXPRESSION:
            case ASTNode.LAMBDA_EXPRESSION:
            case ASTNode.METHOD_INVOCATION:
            case ASTNode.SIMPLE_NAME:
            case ASTNode.QUALIFIED_NAME:
            case ASTNode.NULL_LITERAL:
            case ASTNode.NUMBER_LITERAL:
            case ASTNode.PARENTHESIZED_EXPRESSION:
            case ASTNode.POSTFIX_EXPRESSION:
            case ASTNode.PREFIX_EXPRESSION:
            case ASTNode.STRING_LITERAL:
            case ASTNode.SUPER_FIELD_ACCESS:
            case ASTNode.SUPER_METHOD_INVOCATION:
            case ASTNode.SUPER_METHOD_REFERENCE:
            case ASTNode.THIS_EXPRESSION:
            case ASTNode.TYPE_LITERAL:
            case ASTNode.TYPE_METHOD_REFERENCE:
            case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
                 break;
        }
        return null;
    }
}
