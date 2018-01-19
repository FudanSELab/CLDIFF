package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchConditionalExpression {
    public static ClusteredActionBean matchConditionalExpression(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "ConditionalExpression";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        return mHighLevelOperationBean;
    }
}
