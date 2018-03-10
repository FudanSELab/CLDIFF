package edu.fdu.se.astdiff.miningoperationbean.statement;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.base.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *  if && else && else if 控制流
 */
public class IfChangeEntity extends StatementPlusChangeEntity{

    final public static String IF = "if";
    final public static String ELSE = "else";
    final public static String ELSE_IF = "else if";

    public IfChangeEntity(ClusteredActionBean bean) {
        super(bean);

    }




}
