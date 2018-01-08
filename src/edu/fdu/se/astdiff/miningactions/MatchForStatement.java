package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchForStatement {
    public static HighLevelOperationBean matchForStatement(FindPattern fp, Action a, String nodeType){
        String operationEntity = "FORSTATEMENT";
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);

        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,subActions,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }

    public static HighLevelOperationBean matchEnhancedForStatement(FindPattern fp, Action a, String nodeType){
        String operationEntity = "ENHANCEDFORSTATEMENT";
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);

        HighLevelOperationBean mHighLevelOperationBean = new HighLevelOperationBean(
                a,nodeType,subActions,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
}
