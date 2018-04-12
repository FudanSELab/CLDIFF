package edu.fdu.se.astdiff.associating.linkbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.util.MyList;
import edu.fdu.se.astdiff.miningchangeentity.base.StatementPlusChangeEntity;
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


    public StmtData(StatementPlusChangeEntity ce) {
        if (ce.clusteredActionBean.curAction == null) {
//            ce.linkBean = new LinkBean(ce.clusteredActionBean.fafather);
        } else if (ce.clusteredActionBean.curAction instanceof Move) {
//            ce.linkBean = new LinkBean(ce.clusteredActionBean.curAction);
        } else {
//            ce.linkBean = new LinkBean(ce.clusteredActionBean.actions);
        }
        this.variableField = new MyList<>();
        this.variableLocal = new MyList<>();
        this.methodInvocation = new MyList<>();
    }

    public int isCommitVar(StmtData stmtData){
        int flag = 0;
        for(String tmp:stmtData.variableLocal){
            if(this.variableLocal.contains(tmp)){
                flag ++;
            }
        }

        for(String tmp:stmtData.variableLocal){
            if(this.variableLocal.contains(tmp)){
                flag ++;
            }
        }
        return flag;
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

    private void setInfixExpression(InfixExpression infixExpression, List<String> methodInvocationSet, List<String> varNameSet){
        ASTNode leftOp = infixExpression.getLeftOperand();
        ASTNode rightOp = infixExpression.getRightOperand();
        List<ASTNode> tmp = new ArrayList<>();
        tmp.add(leftOp);
        tmp.add(rightOp);
        traverseASTNodeList(tmp,methodInvocationSet,varNameSet);
    }

    public void traverseASTNodeList(List<ASTNode> list,List<String> methodInvocationSet,List<String> varNameSet) {
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

    private void setMethodInvocation(MethodInvocation methodInvocation,List<String> methodInvocationSet,List<String> varNameSet) {
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
        MyList<String> simpleNameList = new MyList<>();
        for(Action a:actions){
            Tree tree = (Tree)a.getNode();
            if(tree.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME|| tree.getAstNode().getClass().getSimpleName().endsWith("Literal")){
                simpleNameList.add(tree.getLabel());
            }
        }
        MyList<String> tmpMethodInvocations = new MyList<>();
        MyList<String> tmpVars = new MyList<>();
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

//        this.methodNames.addAll(addCommonNames(tmpMethodInvocations,simpleNameList));
//        this.variables.addAll(addCommonNames(tmpVars,simpleNameList));
    }

    private List<String> addCommonNames(MyList<String> a,MyList<String> b){
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
    public void move(Action moveAction){
        Tree moveTree = (Tree)moveAction.getNode();
        for(ITree t :moveTree.preOrder()){
            Tree tree = (Tree)t;
            if(tree.getAstNode().getNodeType()== ASTNode.METHOD_INVOCATION){
                ASTNode methodInvocation = tree.getAstNode();
//                setMethodInvocation((MethodInvocation) methodInvocation,this.methodNames,this.variables);
            }
        }
    }

    public void move(Tree moveTree){
        for(ITree t :moveTree.preOrder()){
            Tree tree = (Tree)t;
            if(tree.getAstNode().getNodeType()== ASTNode.METHOD_INVOCATION){
                ASTNode methodInvocation = tree.getAstNode();
//                setMethodInvocation((MethodInvocation) methodInvocation,this.methodNames,this.variables);
            }
        }
    }
}
