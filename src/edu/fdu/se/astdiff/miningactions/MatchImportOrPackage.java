package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchImportOrPackage {
    public static ClusteredActionBean matchImportDeclaration(MiningActionData fp, Action a, String nodeType) {
        String operationEntity  = "IMPORTDECLARATION";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        return mHighLevelOperationBean;

    }
    public static ClusteredActionBean matchImportDeclarationByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
        String operationEntity  = "FATHER-IMPORTDECLARATION";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }

    public static ClusteredActionBean matchPackageDeclaration(MiningActionData fp, Action a, String nodeType) {
        String operationEntity  = "PACKAGEDECLARATION";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByNode(fp,a,nodeType,operationEntity);
        return mHighLevelOperationBean;
    }
    public static ClusteredActionBean matchPackageDeclarationByFather(MiningActionData fp, Action a, String nodeType, ITree fafafatherNode, String ffFatherNodeType) {
        String operationEntity  = "FATHER-PACKAGEDECLARATION";
        ClusteredActionBean mHighLevelOperationBean = AstRelations.matchByFatherNode(fp,a,nodeType,operationEntity,fafafatherNode,ffFatherNodeType);
        return mHighLevelOperationBean;
    }

}
