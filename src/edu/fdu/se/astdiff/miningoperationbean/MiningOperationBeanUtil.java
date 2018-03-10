package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import edu.fdu.se.astdiff.linkpool.LinkBean;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/3/10.
 */
public class MiningOperationBeanUtil {

    public static Set<String> getNames(NodeList nodeList){
        Set<String> result = new HashSet<>();
        for(int i =0;i<nodeList.size();i++){
            Node n = nodeList.get(i);
            if(n instanceof MethodDeclaration){
                MethodDeclaration md = (MethodDeclaration) n;
                result.add(md.getNameAsString());
            }
            if(n instanceof FieldDeclaration){
                FieldDeclaration fd = (FieldDeclaration) n;
                NodeList<VariableDeclarator> list2 = fd.getVariables();
                for(VariableDeclarator vd:list2){
                    result.add(vd.getNameAsString());
                }
            }
            if(n instanceof ClassOrInterfaceDeclaration){
                ClassOrInterfaceDeclaration cod = (ClassOrInterfaceDeclaration) n;
                Set<String> result2 = getNames(cod.getMembers());
                result.addAll(result2);
            }
            if(n instanceof ConstructorDeclaration){
                ConstructorDeclaration cd = (ConstructorDeclaration)n;
                result.add(cd.getNameAsString());
            }
        }
        return result;
    }

    public void parseActionsGetNames(List<Action> actionList, LinkBean linkBean){
        for(Action a:actionList){
            Tree tree = (Tree) a.getNode();
            int type = tree.getAstNode().getNodeType();
            switch(type){
                case ASTNode.METHOD_INVOCATION:

            }
        }

    }
}
