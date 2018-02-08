package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class AssertChangeEntity extends StatementPlusChangeEntity{

    final static String  assertStr = "assert";
    public AssertChangeEntity(ClusteredActionBean bean) {
        super(bean);
        this.changeEntity = assertStr;
    }





}
