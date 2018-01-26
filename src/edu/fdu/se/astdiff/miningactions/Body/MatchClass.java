package edu.fdu.se.astdiff.miningactions.Body;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningactions.util.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class MatchClass {

    public static void matchClassSignature(MiningActionData fp, Action a, ITree fafather) {
        // 遍历 src dst 两颗树下， 非declaration 节点
        // insert/delete class signature paramter
        // insert/delete class modifier
        // update class modifier / primitive type
        ChangePacket changePacket = new ChangePacket();
        List<Action> signatureChidlren = new ArrayList<>();
        changePacket.setOperationEntity(OperationTypeConstants.ENTITY_CLASS);
        changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_SIGNATURE);
        ITree[] res = TraverseTree.getMappedFafatherNode(fp,a,fafather);
        ITree srcFafather = res[0];
        ITree dstFafather = res[1];
        Set<String> srcStatus = MyTreeUtil.traverseClassSignatureChildren(srcFafather, signatureChidlren);
        Set<String> dstStatus = MyTreeUtil.traverseClassSignatureChildren(dstFafather, signatureChidlren);
        fp.setActionTraversedMap(signatureChidlren);
        MatchUtil.setChangePackgeDownUpOperationType(changePacket,srcStatus,dstStatus);
        Range range = AstRelations.getRangeOfAstNode(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ClusteredActionBean.TRAVERSE_DOWN_UP,a,signatureChidlren,changePacket,range,(Tree)srcFafather);
        ClassOrInterfaceDeclarationChangeEntity code = new ClassOrInterfaceDeclarationChangeEntity(mBean);
        fp.addOneChangeEntity(code);
    }


}
