package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchJavaDoc {
    public static ClusteredActionBean matchJavaDoc(MiningActionData fp, Action a, String nodeType){
        String operationEntity = "JAVADOC";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        return mHighLevelOperationBean;
    }
    public static ClusteredActionBean matchJavaDocByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType){
        String operationEntity = "FATHER-JAVADOC";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFafafatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }
}
