package edu.fdu.se.astdiff.miningchangeentity.statement;

import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.astdiff.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.astdiff.miningchangeentity.base.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class VariableChangeEntity extends StatementPlusChangeEntity {

    final static public String VARIABLEDECLARATION = "VariableDeclaration";
    public VariableChangeEntity(ClusteredActionBean bean) {
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
            sb.append("  ");

            sb.append("with ");
        }
        return sb.toString();
    }

}
