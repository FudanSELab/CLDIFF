package edu.fdu.se.base.miningactions;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.base.miningactions.bean.MiningActionData;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/2.
 */
public class AbstractCluster {
    public List<Action> actionList;
    public MiningActionData fp;

    public AbstractCluster(Class mClazz, MiningActionData mminingActionData) {
        this.fp = mminingActionData;
        Class clazz = mClazz;
        if (Insert.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getInsertActions();
        } else if (Delete.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getDeleteActions();
        } else if (Move.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getMoveActions();
        } else {
            this.actionList = mminingActionData.mGeneratingActionsData.getUpdateActions();
        }
    }
}
