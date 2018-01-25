package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.statementplus.ForChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchForStatement {
    public static void matchForStatement(MiningActionData fp, Action a, String nodeType){
        String operationEntity = ForChangeEntity.FOR;
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        ForChangeEntity forChangeEntity = new ForChangeEntity(mHighLevelOperationBean);
        fp.addOneChangeEntity(forChangeEntity);
    }

    public static void matchEnhancedForStatement(MiningActionData fp, Action a, String nodeType){
        String operationEntity = ForChangeEntity.FOR_EACH;
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        ForChangeEntity forChangeEntity = new ForChangeEntity(mHighLevelOperationBean);
        fp.addOneChangeEntity(forChangeEntity);
    }

    public static void matchForPredicate(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
        String operationEntity  = ForChangeEntity.FOR;
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
        Set<String> srcT = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a,srcfafafather, allActions);
        Set<String> dstT = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a,dstfafafather, allActions);
        int status = MatchTry.MyTreeUtil.isSrcOrDstAdded(srcT,dstT);
        fp.setActionTraversedMap(allActions);
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
        ForChangeEntity forChangeEntity = new ForChangeEntity(mHighLevelOperationBean);
        fp.addOneChangeEntity(forChangeEntity);
    }

    public static void matchEnhancedForPredicate(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
        String operationEntity  = ForChangeEntity.FOR_EACH;
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
        Set<String> srcT = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a,srcfafafather, allActions);
        Set<String> dstT = MatchTry.MyTreeUtil.traverseMethodSignatureChildrenWithoutBlock(a,dstfafafather, allActions);
        int status = MatchTry.MyTreeUtil.isSrcOrDstAdded(srcT,dstT);
        fp.setActionTraversedMap(allActions);
        Range nodeLinePosition = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,allActions,nodeLinePosition,status,operationEntity,fafafatherNode,ffFatherNodeType);
        ForChangeEntity forChangeEntity = new ForChangeEntity(mHighLevelOperationBean);
        fp.addOneChangeEntity(forChangeEntity);
    }
}
