package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class ClusterMove {
    public static void findMove(MiningActionData fp) {
        int moveActionCount = fp.mGeneratingActionsData.getMoveActions().size();
        int index = 0;
        while (true) {
            if (index == moveActionCount) {
                break;
            }
            Action a = fp.mGeneratingActionsData.getMoveActions().get(index);
            index++;
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                // 标记过的 update action
                continue;
            }
            Move up = (Move) a;
            ITree moveNode = a.getNode();
            String type = fp.mSrcTree.getTypeLabel(moveNode);
            String nextAction = ConsolePrint.getMyOneActionString(a, 0, fp.mSrcTree);
            ITree fafafather = AstRelations.findFafafatherNode(moveNode, fp.mSrcTree);
            String fatherType = fp.mSrcTree.getTypeLabel(fafafather);
            System.out.print(nextAction+"\n");
            ITree dstNode = up.getParent();

        }

    }
}
