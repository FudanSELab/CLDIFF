package edu.fdu.se.astdiff.miningactions;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class ClusterActions {


    public static void doCluster(MiningActionData fpd){
        ClusterInsert.findInsert(fpd);
        ClusterDelete.findDelete(fpd);
        ClusterUpdate.findUpdate(fpd);
        ClusterMove.findMove(fpd);
    }
    //		ITree tree = a.getNode();
//		Tree parent = (Tree) tree.getParent();
//		MethodInvocation mi = (MethodInvocation) parent.getAstNode();
//		System.out.println("MethodInvocation:");
//		System.out.println(mi.getName());// method call的方法名
//		System.out.println(mi.getExpression());// variable
//		int pos = tree.getParent().getChildPosition(tree);
}
