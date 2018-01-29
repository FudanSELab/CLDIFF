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

    /**
     * traverseNode
     * @param tree
     * @param resultActions
     * @param resultTypes
     */
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
        MatchUtil.setChangePacket(changePacket,type1);
    }


    /**
     * 找当前节点的父节点 XXXStatement XXXDelclaration JavaDoc CatchClause
     *
     * @param node
     * @return 返回fafafather
     */
    public static Tree findFafatherNode(ITree node) {
        int type = 0;
        String simpleName =null;
        Tree curNode = (Tree)node;
        while (!curNode.isRoot()) {
            type = curNode.getAstNode().getNodeType();
            simpleName = curNode.getAstClass().getSimpleName();
            if (simpleName.endsWith("Statement") ||simpleName.endsWith("Declaration")) {
                break;
            } else{
                boolean isEnd = false;
                switch(type) {
                    //declaration
                    case ASTNode.BLOCK:
                    case ASTNode.INITIALIZER:
                    case ASTNode.SWITCH_CASE:
                    case ASTNode.CONSTRUCTOR_INVOCATION:
                    case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
                        isEnd = true;
                        break;
                    default:
                        curNode = (Tree)curNode.getParent();
                        break;
                }
                if(isEnd) {
                    break;
                }
            }
        }
        if (curNode.isRoot())
            return null;
        else
            return curNode;
    }

    public static ITree[] getMappedFafatherNode(MiningActionData fp, Action a, ITree fafather){
        ITree srcFafather = null;
        ITree dstFafather = null;
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




}
