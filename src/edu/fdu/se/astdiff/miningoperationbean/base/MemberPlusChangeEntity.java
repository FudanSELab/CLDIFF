package edu.fdu.se.astdiff.miningoperationbean.base;

import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningactions.util.DownUpMatchUtil;
import edu.fdu.se.astdiff.miningactions.util.UpDownMatchUtil;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;
import edu.fdu.se.astdiff.preprocessingfile.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/2/8.
 *
 */
public class MemberPlusChangeEntity extends ChangeEntity {


    public MemberPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public MemberPlusChangeEntity(String location,int changeType,MyRange myRange){
        super();
        this.entityGeneratedStage = ChangeEntity.STAGE_PREDIFF;
        this.lineRange = myRange;
        this.changeType = changeType;
        this.location = location;
    }

    public void appendListString(){
        if(this.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
            UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        }else{
            DownUpMatchUtil.setChangePacket(this.clusteredActionBean);
        }
        this.changeType = this.clusteredActionBean.changePacket.getOperationType();
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(this.changeType));
        this.stageIIOutput.add(this.changeEntity);
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.changePacket.getOperationSubEntity()));
        this.stageIIOutput.add(this.lineRangeStr);
    }


}
