package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchWhileStatement {
    public static ClusteredActionBean matchWhileStatement(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "WHILESTATEMENT";
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);

        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }

    public static ClusteredActionBean matchDoStatement(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "DOSTATEMENT";
        List<Action> subActions = new ArrayList<Action>();
        int status = MyTreeUtil.traverseNodeGetAllEditActions(a, subActions);
        fp.setActionTraversedMap(subActions);

        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,subActions,status,operationEntity,null,null);
        return mHighLevelOperationBean;
    }
}
