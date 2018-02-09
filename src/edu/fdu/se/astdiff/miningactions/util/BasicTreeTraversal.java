package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/26.
 *
 */
public class BasicTreeTraversal {

    public static void traverseNode(ITree tree, List<Action> resultActions, Set<String> resultTypes){
        for(ITree node:tree.preOrder()){
            Tree tmp = (Tree)node;
            if(tmp.getDoAction()==null){
                resultTypes.add(ActionConstants.NULLACTION);
            }else{
                tmp.getDoAction().forEach(a -> {
                    resultActions.add(a);
                    resultTypes.add(a.getClass().getSimpleName());
                });
            }
        }
    }

    public static void traverseNodeChildren(ITree tree, List<Action> resultActions, Set<String> resultTypes){
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
                    resultActions.add(a);
                    resultTypes.add(a.getClass().getSimpleName());
                });
            }
        }
    }

    public static void traverseNodeInRange(ITree tree,int a,int b,List<Action> resultActions,Set<String> resultTypes){
        List<ITree> children = tree.getChildren();
        for(int i = a;i<=b;i++){
            ITree c = children.get(i);
            traverseNode(c,resultActions,resultTypes);
        }
    }

    public static void traverseOneType(Action a,List<Action> result1,ChangePacket changePacket){
        Set<String> type1 = new HashSet<>();
        traverseNode(a.getNode(),result1,type1);
        changePacket.changeSet1 = type1;
    }


    /**
     * 找当前节点的父节点 XXXStatement XXXDelclaration JavaDoc CatchClause
     *
     * @param node
     * @return 返回fafafather
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
            curNode = (Tree) curNode.getParent();
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

    public static Tree[] getQueryFafatherNode(MiningActionData fp,Action a,ITree fafather){
        ITree[] fatherPair = BasicTreeTraversal.getMappedFafatherNode(fp, a, fafather);
        Tree srcFafather = (Tree) fatherPair[0];
        Tree dstFafather = (Tree) fatherPair[1];
        Tree queryFather = null;
        if (srcFafather == null && dstFafather != null) {
            queryFather = dstFafather;
        } else if (srcFafather != null && dstFafather == null) {
            queryFather = srcFafather;
        } else if (srcFafather != null && dstFafather != null) {
            queryFather = srcFafather;
        }
        return queryFather;

    }




}
