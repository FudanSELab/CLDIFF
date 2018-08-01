package edu.fdu.se.base.miningactions;

import com.github.gumtreediff.actions.model.*;
import edu.fdu.se.base.miningactions.bean.MiningActionData;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class ActionAggregationGenerator {

    public void doCluster(MiningActionData fpd) {
        new ClusterUpDown(Move.class, fpd).passGumtreePalsePositiveMoves();
        new ClusterUpDown(Move.class, fpd).doClusterUpDown();
        new ClusterDownUp(Move.class, fpd).doClusterDownUp();
        new ClusterUpDown(Insert.class, fpd).doClusterUpDown();
        new ClusterUpDown(Delete.class, fpd).doClusterUpDown();
        new ClusterDownUp(Insert.class, fpd).doClusterDownUp();
        new ClusterDownUp(Delete.class, fpd).doClusterDownUp();
        new ClusterDownUp(Update.class, fpd).doClusterDownUp();
    }















}
