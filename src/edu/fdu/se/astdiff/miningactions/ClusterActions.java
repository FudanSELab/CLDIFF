package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class ClusterActions {


    public static void doCluster(MiningActionData fpd){
        new ClusterInsertOrDelete(Insert.class,fpd).doCluster();
        new ClusterInsertOrDelete(Delete.class,fpd).doCluster();
        new ClusterInsertOrDelete(Move.class,fpd).doCluster();
        new ClusterInsertOrDelete(Update.class,fpd).doClusterUpdate();

    }
    //		ITree tree = a.getNode();
//		Tree parent = (Tree) tree.getParent();
//		MethodInvocation mi = (MethodInvocation) parent.getAstNode();
//		System.out.println("MethodInvocation:");
//		System.out.println(mi.getName());// method call的方法名
//		System.out.println(mi.getExpression());// variable
//		int pos = tree.getParent().getChildPosition(tree);
}
