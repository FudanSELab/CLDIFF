package edu.fdu.se.astdiff.linkpool;



import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
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
     * 针对预处理的BodyDeclaraiton 的情况
     */
    public LinkBean(){

    }

    /**
     * 针对Bean的情况
     */
    public LinkBean(ClusteredActionBean bean){
        List<Action> actions = bean.actions;
        Set<String> simpleNameList = new HashSet<>();
        for(Action a:actions){
            Tree tree = (Tree)a.getNode();
            if(tree.getAstNode().getClass().getSimpleName().equals("SimpleName")|| tree.getAstNode().getClass().getSimpleName().endsWith("Literal")){
                simpleNameList.add(tree.getLabel());
            }
        }
        for(Action a:actions){
            Tree tree = (Tree)a.getNode();
            if(tree.getAstNode().getClass().getSimpleName().equals("SimpleName")|| tree.getAstNode().getClass().getSimpleName().endsWith("Literal")){
                ASTNode methodInvocation = findMethodInvoation(tree);
                if(methodInvocation == null){
                    continue;
                }
                List<List<String>> mList = methodInvocationList((MethodInvocation) methodInvocation);
                this.methodNames.addAll(mList.get(0));
                this.variables.addAll(mList.get(1));
            }
        }
    }

    private ASTNode findMethodInvoation(Tree tree){
        while(tree.getAstNode().getClass().getSimpleName().endsWith("Declaration")){
            tree = (Tree)tree.getParent();
            if(tree.getAstNode().getNodeType() == ASTNode.METHOD_INVOCATION){
                return tree.getAstNode();
            }
        }
        return null;
    }

    private List<List<String>> methodInvocationList(MethodInvocation methodInvocation){
        List<List<String>> result = new ArrayList<>();
        String methodName = methodInvocation.getName().toString();
        List<String> methodNameList = new ArrayList<>();
        List<String> varNameList = new ArrayList<>();
        methodNameList.add(methodName);
        List arguments = methodInvocation.arguments();
        for(int i =0;i<arguments.size();i++){
            ASTNode tmp = (ASTNode) arguments.get(i);
            switch(tmp.getNodeType()){
                case ASTNode.METHOD_INVOCATION:
                    List<List<String>> result2 = methodInvocationList((MethodInvocation)tmp);
                    methodNameList.addAll(result2.get(0));
                    varNameList.addAll(result2.get(1));
                    break;
                case ASTNode.SIMPLE_NAME:
                    SimpleName simpleName = (SimpleName)tmp;
                    varNameList.add(simpleName.toString());
                    break;
                case ASTNode.BOOLEAN_LITERAL:
                    BooleanLiteral bl = (BooleanLiteral)tmp;
                    varNameList.add(bl.toString());
                    break;
                case ASTNode.CHARACTER_LITERAL:
                    CharacterLiteral cl = (CharacterLiteral)tmp;
                    varNameList.add(cl.toString());
                    break;
                case ASTNode.NULL_LITERAL:
                    NullLiteral nl = (NullLiteral)tmp;
                    varNameList.add(nl.toString());
                    break;
                case ASTNode.NUMBER_LITERAL:
                    NumberLiteral nll = (NumberLiteral) tmp;
                    varNameList.add(nll.toString());
                    break;
                case ASTNode.STRING_LITERAL:
                    StringLiteral sl = (StringLiteral)tmp;
                    varNameList.add(sl.toString());
                    break;
                case ASTNode.TYPE_LITERAL:
                    TypeLiteral tl = (TypeLiteral) tmp;
                    varNameList.add(tl.toString());
                    break;
                default:
                    System.err.println("UNExpdasdsad");
                    break;
            }
        }
        result.add(methodNameList);
        result.add(varNameList);
        return result;
    }
}
