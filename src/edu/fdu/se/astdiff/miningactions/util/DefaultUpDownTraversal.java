package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.ArrayList;
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
        changePacket.initChangeSet1();
        changePacket.initChangeSet2();
        changePacket.getChangeSet1().add(a.getClass().getSimpleName());
        traverseNodeSubTreeInRange(node,0,i-1,result1,changePacket.getChangeSet1());
        traverseNodeSubTreeInRange(node,i,children.size()-1,result1,changePacket.getChangeSet2());

    }

    public static void traverseField(Action a,List<Action> result1,ChangePacket changePacket){
        traverseOneType(a,result1,changePacket);
    }

    public static void traverseInitializer(Action a,List<Action> result1,ChangePacket changePacket){
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
        changePacket.initChangeSet1();
        changePacket.initChangeSet2();
        changePacket.getChangeSet1().add(a.getClass().getSimpleName());
        traverseNodeSubTreeInRange(node,0,i-1,result1,changePacket.getChangeSet1());
        traverseNodeSubTreeInRange(node,i,children.size()-1,result1,changePacket.getChangeSet2());
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
        changePacket.initChangeSet1();
        changePacket.initChangeSet2();
        changePacket.getChangeSet1().add(a.getClass().getSimpleName());
        traverseNodeSubTreeInRange(node,0,i-1,result1,changePacket.getChangeSet1());
        traverseNodeSubTreeInRange(node,i,children.size()-1,result1,changePacket.getChangeSet2());
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
        changePacket.initChangeSet1();
        changePacket.initChangeSet2();
        changePacket.getChangeSet1().add(a.getClass().getSimpleName());
        traverseNodeSubTreeInRange(node,0,i-1,result1,changePacket.getChangeSet1());
        traverseNodeSubTreeInRange(node,i,children.size()-1,result1,changePacket.getChangeSet2());

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
        changePacket.initChangeSet1();
        changePacket.initChangeSet2();
        changePacket.getChangeSet1().add(a.getClass().getSimpleName());
        traverseNodeSubTreeInRange(node,0,i-1,result1,changePacket.getChangeSet1());

        if(type==1){
            Tree block  = (Tree) node.getChild(i);
            if(block.getDoAction()!=null){
                for(Action aa:block.getDoAction()){
                    if(aa.getClass().equals(a.getClass())){
                        result1.add(aa);
                    }
                }
            }
            traverseNodeChildrenSubTree(block,result1,changePacket.getChangeSet2());
            traverseNodeSubTreeInRange(node,i+1,children.size()-1,result1,changePacket.getChangeSet2());
        }else{
            traverseNodeSubTreeInRange(node,i,children.size()-1,result1,changePacket.getChangeSet2());
        }

    }
    public static void traverseTypeIStatements(Action a,List<Action> result1,ChangePacket changePacket){
        traverseOneType(a,result1,changePacket);
    }

    public static void traverseTryStatements(Action a,List<Action> result,ChangePacket changePacket){
        ITree node = a.getNode();
        result.add(a);
        List<ITree> children = node.getChildren();
        changePacket.initChangeSet1();
        changePacket.initChangeSet2();
        changePacket.getChangeSet1().add(a.getClass().getSimpleName());
        int i=0;
        for(;i<children.size();i++){
            Tree t = (Tree) children.get(i);
            if(t.getAstNode().getNodeType() == ASTNode.BLOCK){
                traverseNodeChildrenSubTree(t,result,changePacket.getChangeSet2());
            }else{
                traverseNodeSubTree(t,result,changePacket.getChangeSet1());
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
        changePacket.initChangeSet1();
        changePacket.initChangeSet2();
        changePacket.getChangeSet1().add(a.getClass().getSimpleName());
        traverseNodeSubTreeInRange(node,0,i-1,result1,changePacket.getChangeSet1());
        traverseNodeSubTreeInRange(node,i,children.size()-1,result1,changePacket.getChangeSet2());

    }

    public static void traverseSwitchCase(Action a,List<Action> result1,ChangePacket changePacket){
        traverseOneType(a,result1,changePacket);
//        Set<String> type1 = new ArrayList<>();
//        Tree switchCase = (Tree) a.getNode();
//        Tree switchParent = (Tree) switchCase.getParent();
//        result1.add(a);
//        int pos = switchParent.getChildPosition(switchCase);
//        int len = switchParent.getChildren().size();
//        int i=pos+1;
//        for(;i<=len-1;i++){
//            Tree tmp = (Tree)switchParent.getChild(i);
//            if(tmp.getAstNode().getNodeType() == ASTNode.SWITCH_CASE){
//                break;
//            }
//
//        }
//        if(i>len){
//            traverseNodeSubTreeInRange(switchParent,pos,len,result1,type1);
//
//        }else{
//            traverseNodeSubTreeInRange(switchParent,pos,i-1,result1,type1);
//        }
//        changePacket.changeSet1 = type1;
    }



}
