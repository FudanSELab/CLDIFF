package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/25.
 *
 */
public class TraverseTree {

    public static ClusteredActionBean traverseMainEntrace(MiningActionData fp, Action a, ChangePacket changePacket){

    }

    public static ClusteredActionBean traverseNodeUpDown(MiningActionData fp, Action a, ChangePacket changePacket){
        List<Action> subActions = new ArrayList<>();
        Set<String> changeTypes = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        if(changeTypes.size()==1 && changeTypes.contains(ActionConstants.INSERT)){
            changePacket.setOperationType(OperationTypeConstants.INSERT);
        }else if(changeTypes.size()==1 && changeTypes.contains(ActionConstants.DELETE)){
            changePacket.setOperationType(OperationTypeConstants.DELETE);
        }else if(changeTypes.size()==1 && changeTypes.contains(ActionConstants.MOVE)){
            changePacket.setOperationType(OperationTypeConstants.MOVE);
        }
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,range);
        return mBean;
    }

    private static ClusteredActionBean traverseNodeUpDownDstTree(MiningActionData fp,Action a,ChangePacket changePacket){
        List<Action> subActions = new ArrayList<>();
        Set<String> changeTypes = MyTreeUtil.traverseDstNodeGetAllEditActions(a, subActions);
        changePacket.setOperationType(OperationTypeConstants.INSERT);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,range);
        return mBean;
    }
    private static ClusteredActionBean traverNodeUpDownSrcTree(MiningActionData fp,Action a,ChangePacket changePacket){
        List<Action> subActions = new ArrayList<>();
        Set<String> actionTypes = MyTreeUtil.traverseSrcNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,range);
        return mBean;
    }




    public static ClusteredActionBean traverseNodeUpdownNodePair(MiningActionData fp, Action a, ITree fafafatherNode, String ffFatherNodeType){
        List<Action> allActions = new ArrayList<>();
        ITree srcfafafather = null;
        ITree dstfafafather = null;
        if (a instanceof Insert) {
            dstfafafather = fafafatherNode;
            srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
            if (srcfafafather == null) {
                System.err.println("err null mapping");
            }
        } else {
            srcfafafather = fafafatherNode;
            dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
            if (dstfafafather == null) {
                System.err.println("err null mapping");
            }
        }
        Set<String> src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
        Set<String> dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
        int status = MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
        fp.setActionTraversedMap(allActions);
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,allActions,nodeLinePosition,status,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }

    public static ClusteredActionBean traverseNodeUpdownNodePairWithoutBlock(MiningActionData fp, Action a, String nodeType, String operationEntity, ITree fafafatherNode, String ffFatherNodeType){
        List<Action> allActions = new ArrayList<>();
        ITree srcfafafather = null;
        ITree dstfafafather = null;
        if (a instanceof Insert) {
            dstfafafather = fafafatherNode;
            srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
            if (srcfafafather == null) {
                System.err.println("err null mapping");
            }
        } else {
            srcfafafather = fafafatherNode;
            dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
            if (dstfafafather == null) {
                System.err.println("err null mapping");
            }
        }
        Set<String> src_status = MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(srcfafafather, allActions);
        Set<String> dst_status = MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(dstfafafather, allActions);
        int status = MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
        fp.setActionTraversedMap(allActions);
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }






    /**
     * 找当前节点的父节点 XXXStatement XXXDelclaration JavaDoc CatchClause
     *
     * @param node
     * @return 返回fafafather
     */
    public static Tree findFafafatherNode(ITree node) {
        String type = null;
        Tree curNode = (Tree)node;
        while (!curNode.isRoot()) {
            type = curNode.getAstClass().getSimpleName();
            if (type.endsWith("Statement")) {
                break;
            } else{
                boolean isEnd = false;
                switch(type) {
                    //declaration
                    case StatementConstants.METHODDECLARATION:
                    case StatementConstants.FIELDDECLARATION:
                    case StatementConstants.TYPEDECLARATION:
                    case StatementConstants.BLOCK:
                    case StatementConstants.JAVADOC:
                    case StatementConstants.INITIALIZER:
                    case StatementConstants.SWITCHCASE:
                    case StatementConstants.CONSTRUCTORINVOCATION:
                    case StatementConstants.SUPERCONSTRUCTORINVOCATION:
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

    public static ITree[] getMappedFafatherNode(MiningActionData fp, Action a,ITree fafather){
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
