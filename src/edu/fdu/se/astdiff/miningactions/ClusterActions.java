package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class ClusterActions {

    public static void doCluster(MiningActionData fpd) {
        new ClusterUpDown(Move.class, fpd).doClusterUpDown();
        new ClusterDownUp(Move.class, fpd).doClusterDownUp();
        new ClusterUpDown(Insert.class, fpd).doClusterUpDown();
        new ClusterUpDown(Delete.class, fpd).doClusterUpDown();
        new ClusterDownUp(Insert.class, fpd).doClusterDownUp();
        new ClusterDownUp(Delete.class, fpd).doClusterDownUp();
        new ClusterDownUp(Update.class, fpd).doClusterDownUp();
        iterateChangeEntityListSetChangePacket(fpd);
    }



    public static void iterateChangeEntityListSetChangePacket(MiningActionData mad){
        List<ChangeEntity> mList = mad.getChangeEntityList();
        for(ChangeEntity c : mList){
            if(c instanceof StatementPlusChangeEntity){
                StatementPlusChangeEntity s = (StatementPlusChangeEntity) c;
                s.refreshEntityValue();
            }else {
                MemberPlusChangeEntity m = (MemberPlusChangeEntity) c;
                m.refreshEntityValue();
            }
        }
    }










}
