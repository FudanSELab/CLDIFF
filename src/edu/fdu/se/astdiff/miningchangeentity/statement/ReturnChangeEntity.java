package edu.fdu.se.astdiff.miningchangeentity.statement;

import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class ReturnChangeEntity extends StatementPlusChangeEntity {
    final public static String returnStr = "Return";
    public ReturnChangeEntity(ClusteredActionBean bean) {
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
            sb.append("'s expression ");
//            sb.append();
            sb.append("with/by...");
        }
        return sb.toString();
    }

}
