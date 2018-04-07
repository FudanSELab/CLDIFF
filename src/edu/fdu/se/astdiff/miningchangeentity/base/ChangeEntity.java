package edu.fdu.se.astdiff.miningchangeentity.base;

import edu.fdu.se.astdiff.Global.Global;
import edu.fdu.se.astdiff.associating.linkbean.LinkBean;
import edu.fdu.se.astdiff.associating.MyRange;
import edu.fdu.se.astdiff.miningactions.util.AstRelations;
import edu.fdu.se.astdiff.miningchangeentity.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/16.
 * 父类 其他的Entity都继承于此Entity
 */
public class ChangeEntity {
    protected int changeEntityId;

    public ClusteredActionBean clusteredActionBean;
    public StageIIIBean stageIIIBean;
    public StageIIBean stageIIBean;
    public LinkBean linkBean;
    public MyRange lineRange;

    private  void init(){
        changeEntityId = Global.changeEntityId;
        Global.changeEntityId++;
        stageIIIBean = new StageIIIBean();
        stageIIBean = new StageIIBean();

    }

    /**
     * 预处理
     * @param location
     * @param changeType
     * @param myRange
     */
    public ChangeEntity(String location,String changeType,MyRange myRange){
        init();
        this.lineRange = myRange;
        this.stageIIBean.setLineRange("("+this.lineRange.startLineNo +","+ this.lineRange.endLineNo+")");
        this.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF);
        this.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER);
        this.stageIIBean.setOpt(ChangeEntityDesc.getKeyNameByValue(changeType));
        this.stageIIBean.setLocation(location);
    }


    public ChangeEntity(ClusteredActionBean bean){
        init();
        this.clusteredActionBean = bean;
        this.lineRange = AstRelations.getMyRange(bean.fafather,bean.nodeType);
    }

    public ChangeEntity(){
        init();
    }

    @Override
    public String toString(){
        return changeEntityId+". "+this.stageIIBean.toString();
    }


    public String toString2(){
        return changeEntityId+". "+ this.stageIIBean.toString2();
    }




}
