package edu.fdu.se.base.miningchangeentity.statement;

import edu.fdu.se.base.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.base.StatementPlusChangeEntity;

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



    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
        sb.append(this.stageIIBean.getOpt());
        sb.append(" ");
        sb.append(this.stageIIBean.getChangeEntity());
        if(this.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
            sb.append(" ");
            sb.append(this.stageIIBean.getSubEntity());
            sb.append(" with insert/delete");
        }else if(this.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT)||this.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE)){
            sb.append(" ");
            sb.append(this.stageIIBean.getSubEntity());
        }
        return sb.toString();
    }


}
