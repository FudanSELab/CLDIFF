package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ReturnChangeEntity extends StatementPlusChangeEntity {
    final public static String returnStr = "Return";
    public ReturnChangeEntity(ClusteredActionBean bean) {
        super(bean);
        this.changeEntity = ReturnChangeEntity.returnStr;

    }



}
