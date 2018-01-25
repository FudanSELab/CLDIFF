package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/25.
 */
public class TraverseTree {


    public static ClusteredActionBean traverseNodeUpDown(MiningActionData fp, Action a,ChangePacket changePacket){
        List<Action> subActions = new ArrayList<>();
        int status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_UP_DOWN,a,subActions,range,status,operationEntity,operationSubEntity);
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
        Set<String> src_status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
        Set<String> dst_status = MatchTry.MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
        int status = MatchTry.MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
        fp.setActionTraversedMap(allActions);
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
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
        Set<String> src_status = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(srcfafafather, allActions);
        Set<String> dst_status = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(dstfafafather, allActions);
        int status = MatchTry.MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);
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



}
