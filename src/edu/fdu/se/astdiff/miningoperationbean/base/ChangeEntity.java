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

    final public static String SPLITTER = "\t";
    public ClusteredActionBean clusteredActionBean;
    public LinkBean linkBean;

    public MyRange lineRange;
    public String lineRangeStr;

    public int changeType;
    public String changeEntity;

    public int entityGeneratedStage;

    public static final int STAGE_PREDIFF = 0;
    public static final int STAGE_GUMTREE_UD = 1;
    public static final int STAGE_GUMTREE_DUD = 2;

    public String outputDesc;
    public List<String> stageIIOutput = new ArrayList<>();
    /**
     * 因为存在复杂的内部类
     * String为A.B.c的形式
     */
    public String location;

    public ChangeEntity(){

    }

    public ChangeEntity(ClusteredActionBean bean){
        this.clusteredActionBean = bean;
        this.lineRange = bean.range;
        this.lineRangeStr ="("+this.lineRange.startLineNo +","+ this.lineRange.endLineNo+")";
        this.changeType = bean.changePacket.getOperationType();
        this.stageIIOutput.add(OperationTypeConstants.getKeyNameByValue(this.clusteredActionBean.changePacket.getOperationEntity()));
        if(this.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
            this.stageIIOutput.add("UP-DOWN-PHASE");
        }else{
            this.stageIIOutput.add("DOWN-UP-PHASE");
        }
    }

    public String tabbedToString(){
        StringBuffer sb = new StringBuffer();
        for(String tmp:this.stageIIOutput){
            sb.append(tmp);
            sb.append(ChangeEntity.SPLITTER);
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        outputDesc = tabbedToString() + linkBean.toString();
        return outputDesc;
    }




}
