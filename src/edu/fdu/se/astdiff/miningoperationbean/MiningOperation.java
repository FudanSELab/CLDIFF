package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningactions.FindPatternData;

import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class MiningOperation {

    public static void printHighLevelOperationBeanList(FindPatternData fp,List<HighLevelOperationBean> mHighLevelOperationBeanList) {
        if (mHighLevelOperationBeanList.isEmpty()) {
            System.out.println("HighLevelOperationBeanList is null!");
        }else {
            for (HighLevelOperationBean operationBean : mHighLevelOperationBeanList) {
                TreeContext treeContext;
                if (operationBean.curAction instanceof Insert) {
                    treeContext = fp.getmMiningActionBean().mDstTree;
                } else {
                    treeContext = fp.getmMiningActionBean().mSrcTree;
                }
                String nextAction = ConsolePrint.getMyOneActionString(operationBean.curAction, 0, treeContext);
                System.out.print(nextAction);
                System.out.println(operationBean.toString()+"\n");
            }
        }
    }
}
