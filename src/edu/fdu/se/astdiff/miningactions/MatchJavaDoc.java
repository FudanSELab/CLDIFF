package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
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
}
