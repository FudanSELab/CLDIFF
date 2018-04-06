package edu.fdu.se.astdiff.humanreadableoutput;

import edu.fdu.se.astdiff.miningoperationbean.MiningOperationData;

/**
 * Created by huangkaifeng on 2018/3/27.
 *
 *
 */
public class ChangeEntityData {

    public MiningOperationData miningOperationData;

    public ChangeEntityData(MiningOperationData mad){
        this.miningOperationData = mad;
    }





    public void printStage2ChangeEntity(){
        miningOperationData.printContainerEntityDataAfter();
    }





}
