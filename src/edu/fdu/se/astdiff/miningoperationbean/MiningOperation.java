package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningactions.MiningActionData;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class MiningOperation {

    public static void printHighLevelOperationBeanList(MiningActionData fp) {
        List<ClusteredActionBean> mHighLevelOperationBeanList =  fp.getmHighLevelOperationBeanList();
        if (mHighLevelOperationBeanList.isEmpty()) {
            System.out.println("HighLevelOperationBeanList is null!");
        }else {
            for (ClusteredActionBean operationBean : mHighLevelOperationBeanList) {
                TreeContext treeContext;
                if (operationBean.curAction instanceof Insert) {
                    treeContext = fp.getDstTree();
                } else {
                    treeContext = fp.getSrcTree();
                }
                String nextAction = ConsolePrint.getMyOneActionString(operationBean.curAction, 0, treeContext);
                System.out.print(nextAction);
                System.out.println(operationBean.toString()+"\n");
            }
        }
    }

}
