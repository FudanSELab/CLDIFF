package edu.fdu.se.base.links.linkbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.base.miningactions.util.MyList;
import edu.fdu.se.base.miningchangeentity.base.StatementPlusChangeEntity;
import edu.fdu.se.base.preprocessingfile.data.PreprocessedData;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 */
public class StmtData extends LinkBean {

    public List<String> variableLocal;

    public List<String> variableField;

    public List<String> methodInvocation;

    public List<String> classCreation;


    public StmtData(StatementPlusChangeEntity ce, PreprocessedData preprocessedData) {
        this.variableLocal = new MyList<>();
        this.methodInvocation = new MyList<>();
        this.variableField = new MyList<>();
        this.classCreation = new MyList<>();
        if (ce.clusteredActionBean.curAction instanceof Move) {
            parseMove(ce.clusteredActionBean.curAction,preprocessedData);
        } else {
            parseNonMove(ce.clusteredActionBean.actions,preprocessedData);
        }
    }

    public int isContainSameVar(StmtData stmtData) {
        int flagA = 0;
        int flagB = 0;
        for (String tmp : stmtData.variableLocal) {
            if (this.variableLocal.contains(tmp)) {
                flagA++;
            }
        }
        for(String tmp:stmtData.variableField){
            if(this.variableField.contains(tmp)){
                flagB ++;
            }
        }
        if(flagA !=0 &&flagB!=0){
            return 3;
        }
        if(flagA !=0 && flagA==0){
            return 1;
        }
        if(flagA ==0 && flagB!=0){
            return 2;
        }
        return 0;
    }


    private void parseMove(Action a, PreprocessedData preprocessedData) {
        Tree tree = (Tree) a.getNode();
        List<Tree> simpleNames = new ArrayList<>();
        for (ITree tmp : tree.preOrder()) {
            Tree t = (Tree) tmp;
            if (t.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME
                    || t.getAstNode().getClass().getSimpleName().endsWith("Literal")) {
                simpleNames.add(t);
            }
        }
        for (Tree aa : simpleNames) {
            if (aa.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME
                    || aa.getAstNode().getClass().getSimpleName().endsWith("Literal")) {
                ASTNode exp = findExpression(tree);
                if (exp == null || !(exp instanceof MethodInvocation)) {
                    if(preprocessedData.prevCurrFieldNames.contains(tree.getLabel())) {
                        this.variableField.add(tree.getLabel());
                    }else {
                        variableLocal.add(tree.getLabel());
                    }
                    continue;
                }
                if (isMethodInvocationName((MethodInvocation) exp, tree.getLabel())) {
                    methodInvocation.add(tree.getLabel());
                }
            }
        }
    }

    private void parseNonMove(List<Action> actions,PreprocessedData preprocessedData) {
        for (Action a : actions) {
            Tree tree = (Tree) a.getNode();
            String updateVal = null;
            if(a instanceof Update){
                updateVal = ((Update)a).getValue();
            }
            if (tree.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME
                    || tree.getAstNode().getClass().getSimpleName().endsWith("Literal")) {
                ASTNode exp = findExpression(tree);
                boolean flag = true;
                if(exp !=null && exp instanceof MethodInvocation){
                    if (isMethodInvocationName((MethodInvocation) exp, tree.getLabel())) {
                        methodInvocation.add(tree.getLabel());
                        flag = false;
                    }
                }
                if(exp !=null&& exp instanceof ClassInstanceCreation){
                    if(isClassCreationName((ClassInstanceCreation)exp,tree.getLabel())){
                        this.classCreation.add(tree.getLabel());
                        flag = false;
                    }
                }
                if(flag)
                    if(preprocessedData.prevCurrFieldNames.contains(tree.getLabel())){
                        variableField.add(tree.getLabel());
                    }else {
                        variableLocal.add(tree.getLabel());
                    }
                if(updateVal!=null) {
                    if (preprocessedData.prevCurrFieldNames.contains(updateVal)){
                        variableField.add(updateVal);
                    }else {
                        variableLocal.add(updateVal);
                    }
                }

            }

        }
    }


    private ASTNode findExpression(Tree tree) {
        int flag = 0;
        while (!tree.getAstNode().getClass().getSimpleName().endsWith("Declaration")) {
            tree = (Tree) tree.getParent();
            switch (tree.getAstNode().getNodeType()) {
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
                    flag = 1;
                    break;
            }
            if (flag == 1) {
                return tree.getAstNode();
            }
        }
        return null;
    }

//    private void setInfixExpression(InfixExpression infixExpression, List<String> methodInvocationSet, List<String> varNameSet){
//        ASTNode leftOp = infixExpression.getLeftOperand();
//        ASTNode rightOp = infixExpression.getRightOperand();
//        List<ASTNode> tmp = new ArrayList<>();
//        tmp.add(leftOp);
//        tmp.add(rightOp);
////        traverseASTNodeList(tmp,methodInvocationSet,varNameSet);
//    }

//    public void traverseASTNodeList(List<ASTNode> list,List<String> methodInvocationSet,List<String> varNameSet) {
//        for (int i = 0; i < list.size(); i++) {
//            ASTNode tmp = list.get(i);
//            if(tmp.getNodeType() == ASTNode.METHOD_INVOCATION) {
//                setMethodInvocation((MethodInvocation)tmp,methodInvocationSet,varNameSet);
//            }else if(tmp.getNodeType() == ASTNode.INFIX_EXPRESSION) {
//                setInfixExpression((InfixExpression)tmp,methodInvocationSet,varNameSet);
//            }else {
//                varNameSet.add(tmp.toString());
//            }
//        }
//    }

    public boolean isClassCreationName(ClassInstanceCreation classInstanceCreation,String clazzName){
        String clazz = classInstanceCreation.getType().toString();
        if(clazzName.equals(clazz)){
            return true;
        }
        return false;
    }

    private boolean isMethodInvocationName(MethodInvocation methodInvocation, String methodName) {
        String methodName1 = methodInvocation.getName().toString();
        if (methodName.equals(methodName1)) {
            return true;
        }
//        methodInvocationSet.add(methodName);
//        Expression exp = methodInvocation.getExpression();
//        if(exp!=null){
//            if(exp instanceof ThisExpression){
//                //this
//            }else if(exp instanceof SimpleName){
//                // 应该是field
//                varNameSet.add(exp.toString());
//            }
//        }
//        List arguments = methodInvocation.arguments();
//        for (int i = 0; i < arguments.size(); i++) {
//            ASTNode tmp = (ASTNode) arguments.get(i);
//            if(tmp.getNodeType() == ASTNode.METHOD_INVOCATION) {
////                setMethodInvocation((MethodInvocation)tmp,methodInvocationSet,varNameSet);
//            }else if(tmp.getNodeType() == ASTNode.INFIX_EXPRESSION) {
////                setInfixExpression((InfixExpression)tmp,methodInvocationSet,varNameSet);
//            }else {
//                varNameSet.add(tmp.toString());
//            }
//        }

//        if(arguments!=null) {
//            traverseASTNodeList(arguments, methodInvocationSet, varNameSet);
//        }
        return false;

    }


    private List<String> addCommonNames(MyList<String> a, MyList<String> b) {
        List<String> result = new ArrayList<>();
        for (String tmp : a) {
            if (b.contains(tmp)) {
                result.add(tmp);
            }
        }
        return result;
    }

    /**
     * Bean Move情况
     *
     * @param moveAction
     */
    public void move(Action moveAction) {
        Tree moveTree = (Tree) moveAction.getNode();
        for (ITree t : moveTree.preOrder()) {
            Tree tree = (Tree) t;
            if (tree.getAstNode().getNodeType() == ASTNode.METHOD_INVOCATION) {
                ASTNode methodInvocation = tree.getAstNode();
//                setMethodInvocation((MethodInvocation) methodInvocation,this.methodNames,this.variables);
            }
        }
    }

    public void move(Tree moveTree) {
        for (ITree t : moveTree.preOrder()) {
            Tree tree = (Tree) t;
            if (tree.getAstNode().getNodeType() == ASTNode.METHOD_INVOCATION) {
                ASTNode methodInvocation = tree.getAstNode();
//                setMethodInvocation((MethodInvocation) methodInvocation,this.methodNames,this.variables);
            }
        }
    }
}
