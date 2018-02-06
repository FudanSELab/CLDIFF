package edu.fdu.se.astdiff.miningoperationbean.model;


import edu.fdu.se.astdiff.miningactions.util.UpDownMatchUtil;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 *
 */
public abstract class StatementPlusChangeEntity extends ChangeEntity{

    public StatementPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
    }


    public void generateDesc(){
        UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        this.changeType = this.clusteredActionBean.changePacket.getOperationType();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) + ChangeEntity.SPLITTER + this.changeEntity+ChangeEntity.SPLITTER +this.lineRangeStr;

    }

    @Override
    public String toString(){
        if(this.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
            return "BIG_BLOCK_CHANGE" + ChangeEntity.SPLITTER + this.outputDesc;
        }else {
            return "SMALL_NODE_CHANGE" +ChangeEntity.SPLITTER + this.outputDesc;
        }
    }


}
