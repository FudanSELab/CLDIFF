package edu.fdu.se.astdiff.miningoperationbean.statement;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/2/8.
 */
public class BreakContinueEntity extends StatementPlusChangeEntity{
    final static public String breakStatement = "break";
    final static public String continueStatement = "continue";
    public BreakContinueEntity(ClusteredActionBean bean) {
        super(bean);
    }
}
