package edu.fdu.se.astdiff.miningoperationbean.model;

import com.github.javaparser.Range;
import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/16.
 * 父类 其他的Entity都继承于此Entity
 */
public abstract class ChangeEntity {

    final public static String SPLITTER = "\t";
    public ClusteredActionBean clusteredActionBean;

    public Range lineRange;
    public String lineRangeStr;

    public int changeType;
    public String changeEntity;

    public String outputDesc;
    /**
     * 因为存在复杂的内部类
     * String为A.B.c的形式
     *
     */
    public String location;

    public ChangeEntity(){

    }

    public ChangeEntity(ClusteredActionBean bean){
        this.clusteredActionBean = bean;
        Range range = bean.nodeLinePosition;
        this.lineRangeStr ="("+range.begin.line+","+range.end.line+")";
        this.changeType = bean.changePacket.getOperationType();
    }






}
