package edu.fdu.se.astdiff.miningchangeentity.statement;

import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.StatementPlusChangeEntity;


/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class WhileChangeEntity extends StatementPlusChangeEntity {



    public WhileChangeEntity(ClusteredActionBean bean) {
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
            if(this.stageIIBean.getSubEntity()!=null) {
                sb.append(this.stageIIBean.getSubEntity());
            }
            sb.append(" by ");
        }else{
            sb.append("'s ");
            sb.append(this.stageIIBean.getSubEntity());
        }
        return sb.toString();
    }

}
