package edu.fdu.se.astdiff.miningoperationbean;

import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.tree.TreeContext;
import edu.fdu.se.astdiff.generatingactions.ConsolePrint;
import edu.fdu.se.astdiff.miningactions.MiningActionData;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.preprocessingfile.PreprocessingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/13.
 */
public class MiningOperation {

    private PreprocessingData preprocessingData;

    private List<ChangeEntity> mChangeEntityAll;

    public MiningOperation(PreprocessingData pd){
        this.preprocessingData = pd;
        this.mChangeEntityAll = new ArrayList<>();
        initChangeEntityList();
    }
    public MiningOperation(){

    }

    public void initChangeEntityList(){
//        this.preprocessingData
    }


    public void printHighLevelOperationBeanList(MiningActionData fp) {
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
