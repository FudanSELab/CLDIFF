package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.Body.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.MemberPlusChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class ClusterActions {

    public static void doCluster(MiningActionData fpd) {
        //big
        new ClusterBig(Insert.class, fpd).doClusterBig();
        new ClusterBig(Delete.class, fpd).doClusterBig();
        new ClusterBig(Move.class, fpd).doClusterBig();
        //small
        new ClusterSmall(Insert.class, fpd).doClusterSmall();
        new ClusterSmall(Delete.class, fpd).doClusterSmall();
        new ClusterSmall(Move.class, fpd).doClusterSmall();
        new ClusterSmall(Update.class, fpd).doClusterSmall();
        iterateChangeEntityListSetChangePacket(fpd);
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
    }






}
