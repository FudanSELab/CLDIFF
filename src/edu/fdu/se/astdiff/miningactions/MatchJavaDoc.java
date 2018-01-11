package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchJavaDoc {
    public static HighLevelOperationBean matchJavaDoc(FindPattern fp, Action a, String nodeType){
        String operationEntity = "JAVADOC";
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);

        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,subActions,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
    public static HighLevelOperationBean matchJavaDoc(FindPattern fp, Action a, String nodeType, ITree fafafatherNode,String ffFatherNodeType){
        String operationEntity = "FATHERNODE-JAVADOC";
        ITree srcParent = null;
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

        boolean dst_status = MyTreeUtil.traverseNodeGetAllEditActions(dstfafafather, allActions);
        boolean src_status = MyTreeUtil.traverseNodeGetAllEditActions(srcfafafather, allActions);
        int status = MyTreeUtil.isSrcorDstAdded(src_status,dst_status);

        //int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(allActions);

        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,allActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }
}
