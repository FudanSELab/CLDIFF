package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningactions.util.UpDownMatchUtil;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 */
public class ReturnChangeEntity extends StatementPlusChangeEntity {
    final public static String returnStr = "Return";
    public ReturnChangeEntity(ClusteredActionBean bean) {
        super(bean);
        this.changeEntity = ReturnChangeEntity.returnStr;

    }

    public void generateDesc() {
        UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        this.changeType = this.clusteredActionBean.changePacket.getOperationType();
        this.outputDesc = OperationTypeConstants.getKeyNameByValue(changeType) + ChangeEntity.SPLITTER + this.changeEntity + ChangeEntity.SPLITTER + this.lineRangeStr;

    }

}
