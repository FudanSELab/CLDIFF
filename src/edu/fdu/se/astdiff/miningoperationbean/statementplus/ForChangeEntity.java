package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 */
public class ForChangeEntity extends StatementPlusChangeEntity {
    final static public String FOR = "For";

    final static public String FOR_EACH = "ForEach";

    public ForChangeEntity(ClusteredActionBean bean) {
        super(bean);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.clusteredActionBean.getOperationEntity());
        sb.append(" ");
        String change = OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.getOperationType());
        sb.append(change);
        sb.append("  ");
        sb.append(this.clusteredActionBean.getNodePositionAsString());
        return sb.toString();
    }
}
