package edu.fdu.se.astdiff.linkpool;



import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/3/10.
 *
 */
public class LinkBean {

    public Set<String> variables;

    public Set<String> methodNames;

    public Set<String> methodDeclarations;

    /**
     * 针对PreDiff BodyDeclaraiton 的情况
     */
    public LinkBean(){
        initField();
    }
    public void initField(){
        this.methodNames = new HashSet<>();
        this.variables = new HashSet<>();
        this.methodDeclarations = new HashSet<>();
    }
    /**
     * 针对Bean的情况 !Move情况
     */
    public LinkBean(List<Action> actions){
        initField();
        addAppendedActions(actions);
    }

    private List<String> addCommonNames(Set<String> a,Set<String> b){
        List<String> result = new ArrayList<>();
        for(String tmp:a){
            if(b.contains(tmp)){
                result.add(tmp);
            }
        }
        return result;
    }

    /**
     * Bean Move情况
     * @param moveAction
     */
    public LinkBean(Action moveAction){
        initField();
        Tree moveTree = (Tree)moveAction.getNode();
        for(ITree t :moveTree.preOrder()){
            Tree tree = (Tree)t;
            if(tree.getAstNode().getNodeType()== ASTNode.METHOD_INVOCATION){
                ASTNode methodInvocation = tree.getAstNode();
                setMethodInvocation((MethodInvocation) methodInvocation,this.methodNames,this.variables);
            }
        }
    }

    private ASTNode findMethodInvoation(Tree tree){
        while(!tree.getAstNode().getClass().getSimpleName().endsWith("Declaration")){
            tree = (Tree)tree.getParent();
            if(tree.getAstNode().getNodeType() == ASTNode.METHOD_INVOCATION){
                return tree.getAstNode();
            }
        }
        return null;
    }

    private void setInfixExpression(InfixExpression infixExpression,Set<String> methodInvocationSet,Set<String> varNameSet){
        ASTNode leftOp = infixExpression.getLeftOperand();
        ASTNode rightOp = infixExpression.getRightOperand();
        List<ASTNode> tmp = new ArrayList<>();
        tmp.add(leftOp);
        tmp.add(rightOp);
        traverseASTNodeList(tmp,methodInvocationSet,varNameSet);

    }

    public void traverseASTNodeList(List<ASTNode> list,Set<String> methodInvocationSet,Set<String> varNameSet) {
        for (int i = 0; i < list.size(); i++) {
            ASTNode tmp = list.get(i);
            if(tmp.getNodeType() == ASTNode.METHOD_INVOCATION) {
                setMethodInvocation((MethodInvocation)tmp,methodInvocationSet,varNameSet);
            }else if(tmp.getNodeType() == ASTNode.INFIX_EXPRESSION) {
                setInfixExpression((InfixExpression)tmp,methodInvocationSet,varNameSet);
            }else {
                varNameSet.add(tmp.toString());
            }
        }
    }

    private void setMethodInvocation(MethodInvocation methodInvocation,Set<String> methodInvocationSet,Set<String> varNameSet) {
        String methodName = methodInvocation.getName().toString();
        methodInvocationSet.add(methodName);
        Expression exp = methodInvocation.getExpression();
        if(exp!=null){
            if(exp instanceof ThisExpression){
                //this
            }else if(exp instanceof SimpleName){
                // 应该是field
                varNameSet.add(exp.toString());
            }
        }
        List arguments = methodInvocation.arguments();
        if(arguments!=null) {
            traverseASTNodeList(arguments, methodInvocationSet, varNameSet);
        }

    }


    public void addAppendedActions(List<Action> actions){
        Set<String> simpleNameList = new HashSet<>();
        for(Action a:actions){
            Tree tree = (Tree)a.getNode();
            if(tree.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME|| tree.getAstNode().getClass().getSimpleName().endsWith("Literal")){
                simpleNameList.add(tree.getLabel());
            }
        }
        Set<String> tmpMethodInvocations = new HashSet<>();
        Set<String> tmpVars = new HashSet<>();
        for(Action a:actions){
            Tree tree = (Tree)a.getNode();
            if(tree.getAstNode().getClass().getSimpleName().equals("SimpleName")
                    || tree.getAstNode().getClass().getSimpleName().endsWith("Literal")){
                ASTNode methodInvocation = findMethodInvoation(tree);
                if(methodInvocation == null){
                    continue;
                }
                setMethodInvocation((MethodInvocation) methodInvocation,tmpMethodInvocations,tmpVars);
            }
        }

        this.methodNames.addAll(addCommonNames(tmpMethodInvocations,simpleNameList));
        this.variables.addAll(addCommonNames(tmpVars,simpleNameList));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(this.variables.size()!=0) {
            sb.append("\nVariables:[");
            for (String tmp : this.variables) {
                sb.append(tmp);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("] \n");
        }

        if(this.methodNames.size()!=0) {
            sb.append("MethodNames:[");
            for (String tmp : this.methodNames) {
                sb.append(tmp);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("] \n");
        }

        if(this.methodDeclarations.size()!=0){
            sb.append("MethodDeclaration:[");
            for (String tmp : this.methodDeclarations) {
                sb.append(tmp);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("] ");
        }
        return sb.toString();
    }
}
