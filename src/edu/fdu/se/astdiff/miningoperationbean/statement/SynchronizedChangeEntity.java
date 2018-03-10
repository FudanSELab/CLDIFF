package edu.fdu.se.astdiff.miningoperationbean.statement;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 */
public class SynchronizedChangeEntity extends StatementPlusChangeEntity {
    final static public String synchronizedStr = "synchronized";
    public SynchronizedChangeEntity(ClusteredActionBean bean) {
        super(bean);
        this.changeEntity = synchronizedStr;
    }

}
