package edu.fdu.se.base.miningactions;

import com.github.gumtreediff.actions.model.Action;
import edu.fdu.se.base.miningactions.Body.MatchNonStatement;
import edu.fdu.se.base.miningactions.bean.MiningActionData;

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
            MatchNonStatement.matchNonStatement(fp, a);
        }
    }
}
