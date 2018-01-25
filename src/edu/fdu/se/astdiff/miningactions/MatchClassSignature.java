package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ClassOrInterfaceDeclarationChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchClassSignature {
    /**
     * 类的signature的变化
     * @param fp
     * @param a
     * @param fafather
     * @return
     */
    public static void matchClassSignature(MiningActionData fp, Action a, ITree fafather) {
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
