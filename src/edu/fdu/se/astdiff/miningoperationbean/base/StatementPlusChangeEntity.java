package edu.fdu.se.astdiff.miningoperationbean.base;


import com.github.gumtreediff.actions.model.Move;
import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.miningactions.util.DownUpMatchUtil;
import edu.fdu.se.astdiff.miningactions.util.UpDownMatchUtil;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 *
 */
public abstract class StatementPlusChangeEntity extends ChangeEntity {

    public StatementPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
        if(bean.curAction instanceof Move){
            this.linkBean = new LinkBean(bean.curAction);
        }else {
            this.linkBean = new LinkBean(bean.actions);
        }
    }

    public void appendListString(){
        if(this.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
            UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        }else{
            DownUpMatchUtil.setChangePacket(this.clusteredActionBean);
        }
        this.changeType = this.clusteredActionBean.changePacket.getOperationType();
        if(this.changeType == OperationTypeConstants.MULTIPLE_EDIT){
            this.stageIIOutput.add(this.clusteredActionBean.changePacket.multiEditStr);
        }else{
            this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(this.changeType));
        }
        this.stageIIOutput.add(this.changeEntity);
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.changePacket.getOperationSubEntity()));
        this.stageIIOutput.add(this.lineRangeStr);

    }




}
