package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ExpressionChangeEntity extends StatementPlusChangeEntity{
    final static public String expression = "expression";
    public ExpressionChangeEntity(ClusteredActionBean bean) {
        super(bean);
        this.changeEntity = expression;
    }



}
