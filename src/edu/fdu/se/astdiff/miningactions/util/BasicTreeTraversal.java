package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/26.
 *
 */
public class BasicTreeTraversal {

    /**
     * 遍历子树，子树中含有任何的action都添加进resultActions，resultType记录type
     *
     * @param tree 遍历的root节点
     * @param resultActions [insert,null] 或者[delete,move,update,null]
     * @param resultTypes 同上
     */
    protected static void traverseNodeSubTree(ITree tree, List<Action> resultActions, List<String> resultTypes){
        for(ITree node:tree.preOrder()){
            Tree tmp = (Tree)node;
            if(tmp.getDoAction()==null){
                resultTypes.add(ActionConstants.NULLACTION);
            }else{
                tmp.getDoAction().forEach(a -> {
                    if(!(a instanceof Move)) {
                        resultActions.add(a);
                        resultTypes.add(a.getClass().getSimpleName());
                    }
                });
            }
        }
    }

    /**
     * 类似于traverseNode，只是考虑到遍历block节点，block节点的属性会影响类型的判断，root节点为block属性时，舍弃block信息
     * @param tree root节点
     * @param resultActions result
     * @param resultTypes resulttype
     */
    protected static void traverseNodeChildrenSubTree(ITree tree, List<Action> resultActions, List<String> resultTypes){
        boolean flag = true;
        for(ITree node:tree.preOrder()){
            if(flag){
                flag = false;
                continue;
            }

            Tree tmp = (Tree)node;
            if(tmp.getDoAction()==null){
                resultTypes.add(ActionConstants.NULLACTION);
            }else{
                tmp.getDoAction().forEach(a -> {
                    if(!(a instanceof Move)) {
                        resultActions.add(a);
                        resultTypes.add(a.getClass().getSimpleName());
                    }
                });
            }
        }
    }

    /**
     * class signature，method signature等需要将形如 XXX(A){B} A和B的变化分开，所以需要首先识别分割点，然后按照range遍历
     * @param tree root
     * @param a range a
     * @param b range b
     * @param resultActions result
     * @param resultTypes resulttype
     */
    protected static void traverseNodeSubTreeInRange(ITree tree, int a, int b, List<Action> resultActions, List<String> resultTypes){
        List<ITree> children = tree.getChildren();
        for(int i = a;i<=b;i++){
            ITree c = children.get(i);
            traverseNodeSubTree(c,resultActions,resultTypes);
        }
    }

    /**
     * 如果是形如field等类型，将其作为一个整体考虑，所以不需要range
     * @param node root
     * @param result1
     * @param changePacket
     */
    protected static void traverseOneType(ITree node,List<Action> result1,ChangePacket changePacket){
        if(changePacket.getChangeSet1()==null){
            changePacket.initChangeSet1();
        }
        traverseNodeSubTree(node,result1,changePacket.getChangeSet1());
    }


    /**
     * 找当前节点的父节点 XXXStatement XXXDelclaration JavaDoc CatchClause
     *
     * @param node 节点
     * @return 返回fafather
     */
    public static Tree findFafatherNode(ITree node) {
        int type;
        Tree curNode = (Tree)node;
        while (true) {
            type = curNode.getAstNode().getNodeType();
            boolean isEnd = false;
            switch (type) {
                case ASTNode.TYPE_DECLARATION:
                case ASTNode.METHOD_DECLARATION:
                case ASTNode.FIELD_DECLARATION:
                case ASTNode.ENUM_DECLARATION:
                case ASTNode.BLOCK:
                case ASTNode.ASSERT_STATEMENT:
                case ASTNode.THROW_STATEMENT:
                case ASTNode.RETURN_STATEMENT:
                case ASTNode.DO_STATEMENT:
                case ASTNode.IF_STATEMENT:
                case ASTNode.WHILE_STATEMENT:
                case ASTNode.ENHANCED_FOR_STATEMENT:
                case ASTNode.FOR_STATEMENT:
                case ASTNode.TRY_STATEMENT:
                case ASTNode.SWITCH_STATEMENT:
                case ASTNode.SWITCH_CASE:
                case ASTNode.CATCH_CLAUSE:
                case ASTNode.EXPRESSION_STATEMENT:
                case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                case ASTNode.SYNCHRONIZED_STATEMENT:
                    isEnd = true;
                default:break;
            }

            if(isEnd){
                break;
            }
//            try {
                curNode = (Tree) curNode.getParent();
//            }catch(Exception e){
//                System.out.println("a");
//            }
        }
        return curNode;
    }

    public static ITree[] getMappedFafatherNode(MiningActionData fp, Action a, ITree fafather){
        ITree srcFafather;
        ITree dstFafather;
        if (a instanceof Insert) {
            dstFafather = fafather;
            srcFafather = fp.getMappedSrcOfDstNode(dstFafather);
        } else {
            srcFafather = fafather;
            dstFafather = fp.getMappedDstOfSrcNode(srcFafather);
        }
        ITree [] result = {srcFafather,dstFafather};
        return result;
    }

    public static boolean traverseWhenActionIsMove(Action a,List<Action> result,ChangePacket changePacket,boolean flag){
        if(a instanceof Move){
            result.add(a);
            changePacket.initChangeSet1();
            changePacket.getChangeSet1().add(ActionConstants.MOVE);
            if(flag) {
                changePacket.initChangeSet2();
                changePacket.getChangeSet2().add(ActionConstants.NULLACTION);
            }
            return true;
        }
        return false;
    }









}
