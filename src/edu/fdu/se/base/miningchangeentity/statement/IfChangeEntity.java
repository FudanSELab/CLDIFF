package edu.fdu.se.base.miningchangeentity.statement;

import edu.fdu.se.base.miningchangeentity.ClusteredActionBean;
import edu.fdu.se.base.miningchangeentity.base.ChangeEntityDesc;
import edu.fdu.se.base.miningchangeentity.base.StatementPlusChangeEntity;

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
            sb.append(" with/by...");
        }else{
            sb.append(" ");
            sb.append(this.stageIIBean.getSubEntity());
        }
        return sb.toString();
    }




}
