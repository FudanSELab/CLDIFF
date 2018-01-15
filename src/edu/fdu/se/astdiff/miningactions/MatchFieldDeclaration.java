package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.miningoperationbean.HighLevelOperationBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchFieldDeclaration {
    public static HighLevelOperationBean matchFieldDeclaration(MiningActionData fp, Action a, String nodeType) {
        String operationEntity  = "FIELDDECLARATION";

        List<Action> allActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, allActions);
        fp.setActionTraversedMap(allActions);
        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,allActions,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
    public static HighLevelOperationBean matchFieldDeclarationByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
        String operationEntity  = "FIELDDECLARATION-BODY";

        List<Action> allActions = new ArrayList<Action>();
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

        boolean src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
        boolean dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
        int status = MyTreeUtil.isSrcorDstAdded(src_status,dst_status);
        fp.setActionTraversedMap(allActions);
        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,allActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }
}
