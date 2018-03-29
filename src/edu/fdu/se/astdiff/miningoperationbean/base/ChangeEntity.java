package edu.fdu.se.astdiff.miningoperationbean.base;

import edu.fdu.se.astdiff.linkpool.LinkBean;
import edu.fdu.se.astdiff.linkpool.MyRange;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.OperationTypeConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/1/16.
 * 父类 其他的Entity都继承于此Entity
 */
public abstract class ChangeEntity {

    public ClusteredActionBean clusteredActionBean;

    public StageIIIBean stageIIIBean;
    public StageIIBean stageIIBean;
    public LinkBean linkBean;
    public MyRange lineRange;

    private  void init(){
        stageIIIBean = new StageIIIBean();
        stageIIBean = new StageIIBean();
        linkBean = new LinkBean();
    }

    /**
     * 预处理
     * @param location
     * @param changeType
     * @param myRange
     */
    public ChangeEntity(String location,int changeType,MyRange myRange){
        init();
        this.lineRange = myRange;
        this.stageIIBean.setLineRange("("+this.lineRange.startLineNo +","+ this.lineRange.endLineNo+")");
        this.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF);
        this.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER);
        this.stageIIBean.setOpt(OperationTypeConstants.getKeyNameByValue(changeType));
        this.stageIIBean.setLocation(location);
    }


    public ChangeEntity(ClusteredActionBean bean){
        init();
        this.clusteredActionBean = bean;
        this.lineRange = bean.range;
    }

    @Override
    public String toString(){
        return this.stageIIBean.toString();
    }


    public void voidS(){
//        this.stageIIBean.setEnityCreationStage();
//               this.stageIIBean.setGranularity();
//        this.stageIIBean.setOpt();
//        this.stageIIBean.setChangeEntity();
//        this.stageIIBean.setOpt2();
//        this.stageIIBean.setSubEntity();
//        this.stageIIBean.setLineRange();1
//        this.stageIIBean.setLocation();

//        this.stageIIBean.setThumbnail();

    }

}
