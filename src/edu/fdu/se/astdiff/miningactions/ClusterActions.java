package edu.fdu.se.astdiff.miningactions;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class ClusterActions {


    public static void doCluster(FindPatternData fpd){
        ClusterInsert.findInsert(fpd);
        ClusterDelete.findDelete(fpd);
        ClusterUpdate.findUpdate(fpd);
        ClusterMove.findMove(fpd);
    }
}
