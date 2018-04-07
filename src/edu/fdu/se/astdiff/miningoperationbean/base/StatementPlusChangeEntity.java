package edu.fdu.se.astdiff.miningoperationbean.base;


import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.link.linkbean.LinkBean;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

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
//            OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.changePacket.getOperationType());
//
//        this.stageIIBean.setOpt(OperationTypeConstants.getKeyNameByValue(this.changeType));
//
//
//        if(this.changeType == OperationTypeConstants.MULTIPLE_EDIT){
//            this.stageIIOutput.add(this.clusteredActionBean.changePacket.multiEditStr);
//        }else{
//        }
//        this.stageIIBean.setChangeEntity(this.changeEntity);
//        this.stageIIBean.setSubEntity(OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.changePacket.getOperationSubEntity()));

    }




}
