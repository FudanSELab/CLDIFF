package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningactions.util.UpDownMatchUtil;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;


/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class WhileChangeEntity extends StatementPlusChangeEntity {


    final static public String WHILE = "while";
    final static public String DO_WHILE = "do while";

    public WhileChangeEntity(ClusteredActionBean bean) {
        super(bean);
    }


    public void generateDesc(){
        UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        this.changeType = this.clusteredActionBean.changePacket.getOperationType();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) + " "+ this.changeEntity+" "+this.lineRangeStr;
    }

    @Override
    public String toString(){
        return this.outputDesc;
    }
}
