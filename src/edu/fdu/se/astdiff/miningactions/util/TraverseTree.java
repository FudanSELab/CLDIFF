package edu.fdu.se.astdiff.miningactions.util;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.javaparser.Range;
import edu.fdu.se.astdiff.generatingactions.ActionConstants;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.bean.ChangePacket;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/25.
 *
 */
public class TraverseTree {

    public static ITree[] getMappedFafatherNode(MiningActionData fp, Action a,ITree fafather){
        ITree srcFafather = null;
        ITree dstFafather = null;
        if (a instanceof Insert) {
            dstFafather = fafather;
            srcFafather = fp.getMappedSrcOfDstNode(dstFafather);
        } else {
            srcFafather = fafather;
            dstFafather = fp.getMappedDstOfSrcNode(srcFafather);
        }
        ITree [] result = {srcFafather,dstFafather};
        return result;
    }

}
