package edu.fdu.se.astdiff.miningactions;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.Tree;
import edu.fdu.se.astdiff.generatingactions.SimpleActionPrinter;
import edu.fdu.se.astdiff.miningactions.Body.*;
import edu.fdu.se.astdiff.miningactions.bean.MiningActionData;
import edu.fdu.se.astdiff.miningactions.statement.*;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 *
 */
public class ClusterActions {
    //		ITree tree = a.getNode();
//		Tree parent = (Tree) tree.getParent();
//		MethodInvocation mi = (MethodInvocation) parent.getAstNode();
//		System.out.println("MethodInvocation:");
//		System.out.println(mi.getName());// method call的方法名
//		System.out.println(mi.getExpression());// variable
//		int pos = tree.getParent().getChildPosition(tree);

    public static void doCluster(MiningActionData fpd) {

        //big
        new ClusterBig(Insert.class, fpd).doClusterBig();
        new ClusterBig(Delete.class, fpd).doClusterBig();
        new ClusterBig(Move.class, fpd).doClusterBig();
        //small
//        new ClusterSmall(Insert.class, fpd).doClusterSmall();
//        new ClusterSmall(Delete.class, fpd).doClusterSmall();
//        new ClusterSmall(Move.class, fpd).doClusterSmall();
//        new ClusterUpdate(Update.class, fpd).doClusterUpdate();

    }






}
