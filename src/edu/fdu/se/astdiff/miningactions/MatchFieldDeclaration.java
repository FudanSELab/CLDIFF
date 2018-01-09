package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchFieldDeclaration {
    public static HighLevelOperationBean matchFieldDeclaration(FindPattern fp,Action a, String nodeType) {
        String operationEntity  = "FIELDDECLARATION";

        List<Action> allActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, allActions);
        fp.setActionTraversedMap(allActions);
        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,allActions,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
    public static HighLevelOperationBean matchFieldDeclarationByFather(FindPattern fp,Action a, String nodeType,ITree fafafatherNode, String ffFatherNodeType) {
        String operationEntity  = "FIELDDECLARATION BODY";

        List<Action> allActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(fafafatherNode, allActions);
        fp.setActionTraversedMap(allActions);
        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,allActions,status,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }
}
