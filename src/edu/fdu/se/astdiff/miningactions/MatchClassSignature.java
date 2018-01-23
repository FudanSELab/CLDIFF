package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.gumtree.MyTreeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchClassSignature {

    public static ClusteredActionBean matchClassSignature(MiningActionData fp, Action a, String nodeType, ITree fafafather, String fafafatherType) {
        String operationEntity = "CLASSSIGNATURE";
        TreeContext con = null;

        ITree srcfafafather = null;
        ITree dstfafafather = null;
        if (a instanceof Insert) {
            con = fp.getDstTree();
            dstfafafather = fafafather;
            srcfafafather = fp.getMappedSrcOfDstNode(dstfafafather);
            if (srcfafafather == null) {
                System.err.println("err null mapping");
            }
        } else {
            con = fp.getSrcTree();
            srcfafafather = fafafather;
            dstfafafather = fp.getMappedDstOfSrcNode(srcfafafather);
            if (dstfafafather == null) {
                System.err.println("err null mapping");
            }
        }
        List<Action> signatureChidlren = new ArrayList<Action>();
        Set<String> src_status = MyTreeUtil.traverseClassSignatureChildren(a, srcfafafather,con, signatureChidlren);
        Set<String> dst_status = MyTreeUtil.traverseClassSignatureChildren(a, dstfafafather,con, signatureChidlren);

        int status = MyTreeUtil.isSrcOrDstAdded(src_status,dst_status);

        fp.setActionTraversedMap(signatureChidlren);
        Range nodeLinePosition = AstRelations.getnodeLinePosition(a,con);
        ClusteredActionBean mHighLevelOperationBean = new ClusteredActionBean(
                a,nodeType,signatureChidlren,nodeLinePosition,status,operationEntity,fafafather,fafafatherType);
        return mHighLevelOperationBean;
    }
}
