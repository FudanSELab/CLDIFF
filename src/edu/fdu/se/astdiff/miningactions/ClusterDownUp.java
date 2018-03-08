package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.Body.MatchSimpleNameOrLiteral;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/2.
 *
 */
public class ClusterDownUp extends AbstractCluster{


    public ClusterDownUp(Class mClazz, MiningActionData mminingActionData) {
        super(mClazz, mminingActionData);
    }

    public void doClusterDownUp() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            MatchSimpleNameOrLiteral.matchSimpleNameOrLiteral(fp, a);
        }
    }
}
