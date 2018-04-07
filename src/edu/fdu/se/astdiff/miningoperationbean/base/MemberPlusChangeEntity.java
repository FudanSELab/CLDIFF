package edu.fdu.se.astdiff.miningoperationbean.base;

import edu.fdu.se.astdiff.link.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.preprocessingfile.data.BodyDeclarationPair;

/**
 * Created by huangkaifeng on 2018/2/8.
 *
 */
public class MemberPlusChangeEntity extends ChangeEntity {

    public BodyDeclarationPair bodyDeclarationPair;

    public MemberPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public MemberPlusChangeEntity(String location,int changeType,MyRange myRange){
        super(location,changeType,myRange);

    }

    public void refreshEntityValue(){
        if(this.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
//            UpDownMatchUtil.setChangePacket(this.clusteredActionBean);
        }else{
//            DownUpMatchUtil.setChangePacket(this.clusteredActionBean);
        }
//        this.stageIIBean.setOpt(OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.changePacket.getOperationType()));
//        this.stageIIBean.setSubEntity(OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.changePacket.getOperationSubEntity()));
    }


}
