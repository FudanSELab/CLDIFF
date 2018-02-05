package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningactions.util.UpDownMatchUtil;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/2/2.
 */
public class ThrowChangeEntity extends StatementPlusChangeEntity{
    public ThrowChangeEntity(ClusteredActionBean mbean){
        super(mbean);
    }

    public void generateDesc(){
        UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        this.changeType = this.clusteredActionBean.changePacket.getOperationType();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) + ChangeEntity.spiltter+ this.changeEntity+ ChangeEntity.spiltter+this.lineRangeStr;
    }

    @Override
    public String toString(){
        return this.outputDesc;
    }

}
