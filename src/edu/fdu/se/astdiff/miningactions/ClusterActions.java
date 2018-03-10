package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.*;
import edu.fdu.se.astdiff.linkpool.LinkBean;
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
        mergeWrapperAndMoveEntity();
        generateLinkInfo(fpd);
    }



    public static void iterateChangeEntityListSetChangePacket(MiningActionData mad){
        List<ChangeEntity> mList = mad.getChangeEntityList();
        for(ChangeEntity c : mList){
            if(c instanceof StatementPlusChangeEntity){
                StatementPlusChangeEntity s = (StatementPlusChangeEntity) c;
                s.appendListString();
            }else {
                MemberPlusChangeEntity m = (MemberPlusChangeEntity) c;
                m.appendListString();
            }
        }
        System.out.print("");
    }

    public static void mergeWrapperAndMoveEntity(){
        //todo 优化之一
    }

    public static void generateLinkInfo(MiningActionData mad){
        List<ChangeEntity> mList = mad.getChangeEntityList();
        for(ChangeEntity c :mList){
            if(c.linkBean ==null){
                c.linkBean = new LinkBean();
                //todo

            }
        }

    }







}
