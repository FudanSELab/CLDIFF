package edu.fdu.se.astdiff.miningchangeentity.base;


import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 *
 */
public class StatementPlusChangeEntity extends ChangeEntity {

    public StatementPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public void refreshEntityValue(){
        if(this.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
//            UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        }else{
//            DownUpMatchUtil.setChangePacket(this.clusteredActionBean);
        }

    }




}
