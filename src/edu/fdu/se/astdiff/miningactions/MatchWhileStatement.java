package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchWhileStatement {
    public static ClusteredActionBean matchWhileStatement(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "WHILESTATEMENT";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        return mHighLevelOperationBean;
    }

    public static ClusteredActionBean matchDoStatement(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "DOSTATEMENT";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        return mHighLevelOperationBean;
    }
}
