package edu.fdu.se.astdiff.miningoperationbean.statement;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ForChangeEntity extends StatementPlusChangeEntity {
    final static public String FOR = "For";

    final static public String FOR_EACH = "ForEach";

    public ForChangeEntity(ClusteredActionBean bean) {
        super(bean);
    }





}
