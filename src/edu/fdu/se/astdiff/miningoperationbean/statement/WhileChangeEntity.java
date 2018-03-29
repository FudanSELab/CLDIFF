package edu.fdu.se.astdiff.miningoperationbean.statement;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;


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



}
