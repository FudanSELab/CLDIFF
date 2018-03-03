package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Created by huangkaifeng on 2018/1/26.
 * up down
 */
public class DefaultUpDownTraversal extends BasicTreeTraversal{

    private static void traverseOneType(Action a,List<Action> result1,ChangePacket changePacket){
        traverseOneType(a.getNode(),result1,changePacket);
    }

    public static void traverseClass(Action a, List<Action> result1, ChangePacket changePacket){
        ITree node = a.getNode();
        result1.add(a);
        List<ITree> children = node.getChildren();
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstClass().getSimpleName().endsWith("Declaration")||t.getAstNode().getNodeType() == ASTNode.INITIALIZER){
                break;
            }
        }
        Set<String> type1 = new HashSet<>();
        Set<String> type2 = new HashSet<>();
        type1.add(a.getClass().getSimpleName());
        traverseNodeInRange(node,0,i-1,result1,type1);
        traverseNodeInRange(node,i,children.size()-1,result1,type2);
        changePacket.changeSet1 = type1;
        changePacket.changeSet2 = type2;
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
    }

    public static void traverseField(Action a,List<Action> result1,ChangePacket changePacket){
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        traverseOneType(a,result1,changePacket);
    }

    public static void traverseInitializer(Action a,List<Action> result1,ChangePacket changePacket){
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        traverseOneType(a,result1,changePacket);
    }
    public static void traverseMethod(Action a,List<Action> result1,ChangePacket changePacket){
        ITree node = a.getNode();
        result1.add(a);
        List<ITree> children = node.getChildren();
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstNode().getNodeType() == ASTNode.BLOCK ){
                break;
            }
        }
        Set<String> type1 = new HashSet<>();
        Set<String> type2 = new HashSet<>();
        type1.add(a.getClass().getSimpleName());
        traverseNodeInRange(node,0,i-1,result1,type1);
        traverseNodeInRange(node,i,children.size()-1,result1,type2);
        changePacket.changeSet1 = type1;
        changePacket.changeSet2 = type2;
    }
    public static void traverseConstructor(Action a,List<Action> result1,ChangePacket changePacket){
        ITree node = a.getNode();
        result1.add(a);
        List<ITree> children = node.getChildren();
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstNode().getNodeType() == ASTNode.BLOCK ){
                break;
            }
        }
        Set<String> type1 = new HashSet<>();
        Set<String> type2 = new HashSet<>();
        type1.add(a.getClass().getSimpleName());
        traverseNodeInRange(node,0,i-1,result1,type1);
        traverseNodeInRange(node,i,children.size()-1,result1,type2);
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
    }


    public static void traverseTypeIIStatements(Action a,List<Action> result1,ChangePacket changePacket){
        ITree node = a.getNode();
        result1.add(a);
        List<ITree> children = node.getChildren();
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstNode().getNodeType() == ASTNode.BLOCK  || t.getAstClass().getSimpleName().endsWith("Statement")){
                break;
            }
        }
        Set<String> type1 = new HashSet<>();
        Set<String> type2 = new HashSet<>();
        type1.add(a.getClass().getSimpleName());
        traverseNodeInRange(node,0,i-1,result1,type1);
        traverseNodeInRange(node,i,children.size()-1,result1,type2);
        changePacket.changeSet1 = type1;
        changePacket.changeSet2 = type2;

    }

    public static void traverseIf(Action a,List<Action> result1,ChangePacket changePacket){
        ITree node = a.getNode();
        result1.add(a);
        int type = 0;
        List<ITree> children = node.getChildren();
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstNode().getNodeType() == ASTNode.BLOCK ){
                type = 1;
                break;
            }else if(t.getAstClass().getSimpleName().endsWith("Statement")){
                type = 2;
                break;
            }
        }
        Set<String> type1 = new HashSet<>();
        Set<String> type2 = new HashSet<>();
        type1.add(a.getClass().getSimpleName());
        traverseNodeInRange(node,0,i-1,result1,type1);

        if(type==1){
            Tree block  = (Tree) node.getChild(i);
            if(block.getDoAction()!=null){
                for(Action aa:block.getDoAction()){
                    if(aa.getClass().equals(a.getClass())){
                        result1.add(aa);
                    }
                }
            }
            traverseNodeChildren(block,result1,type2);
            traverseNodeInRange(node,i+1,children.size()-1,result1,type2);
        }else{
            traverseNodeInRange(node,i,children.size()-1,result1,type2);
        }
        changePacket.changeSet1 = type1;
        changePacket.changeSet2 = type2;

    }
    public static void traverseTypeIStatements(Action a,List<Action> result1,ChangePacket changePacket){
        traverseOneType(a,result1,changePacket);
    }

    public static void traverseTryStatements(Action a,List<Action> result,ChangePacket changePacket){
        ITree node = a.getNode();
        result.add(a);
        List<ITree> children = node.getChildren();
        changePacket.changeSet1 = new HashSet<>();
        changePacket.changeSet1.add(a.getClass().getSimpleName());
        changePacket.changeSet2 = new HashSet<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_MEMBER);
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstNode().getNodeType() == ASTNode.BLOCK){
                traverseNodeChildren(t,result,changePacket.changeSet2);
            }else{
                traverseNode(t,result,changePacket.changeSet1);
            }
        }


    }

    public static void traverseSwitchStatements(Action a,List<Action> result1,ChangePacket changePacket){
        ITree node = a.getNode();
        result1.add(a);
        List<ITree> children = node.getChildren();
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstNode().getNodeType() == ASTNode.SWITCH_CASE){
                break;
            }
        }
        Set<String> type1 = new HashSet<>();
        Set<String> type2 = new HashSet<>();
        type1.add(a.getClass().getSimpleName());
        traverseNodeInRange(node,0,i-1,result1,type1);
        traverseNodeInRange(node,i,children.size()-1,result1,type2);
        changePacket.changeSet1 = type1;
        changePacket.changeSet2 = type2;

    }

    public static void traverseSwitchCase(Action a,List<Action> result1,ChangePacket changePacket){
        Set<String> type1 = new HashSet<>();
        Tree switchCase = (Tree) a.getNode();
        Tree switchParent = (Tree) switchCase.getParent();
        result1.add(a);
        int pos = switchParent.getChildPosition(switchCase);
        int len = switchParent.getChildren().size();
        int i=pos+1;
        for(;i<=len-1;i++){
            Tree tmp = (Tree)switchParent.getChild(i);
            if(tmp.getAstNode().getNodeType() == ASTNode.SWITCH_CASE){
                break;
            }

        }
        if(i>len){
            traverseNodeInRange(switchParent,pos,len,result1,type1);

        }else{
            traverseNodeInRange(switchParent,pos,i-1,result1,type1);
        }
        changePacket.changeSet1 = type1;

    }



}
