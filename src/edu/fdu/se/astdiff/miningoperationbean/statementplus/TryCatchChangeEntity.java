package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningactions.util.UpDownMatchUtil;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class TryCatchChangeEntity extends StatementPlusChangeEntity {
    final public static String tryCatch = "try ... catch";
    final public static String tryWithResources = "try with resources";
    final public static String catchClause = "catch";
    final public static String finallyClause = "finally";
    final public static String throwStatement = "throw";
    public TryCatchChangeEntity(ClusteredActionBean bean) {
        super(bean);
    }



}
