package edu.fdu.se.astdiff.miningoperationbean.model;


import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;

/**
 * Created by huangkaifeng on 2018/1/22.
 *
 *
 */
public abstract class StatementPlusChangeEntity extends ChangeEntity{

    public StatementPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    abstract public void generateDesc();


    @Override
    public String toString(){
        if(this.clusteredActionBean.traverseType == ClusteredActionBean.TRAVERSE_UP_DOWN){
            return "BIG_BLOCK_CHANGE" + ChangeEntity.SPLITTER + this.outputDesc;
        }else {
            return "SMALL_NODE_CHANGE" +ChangeEntity.SPLITTER + this.outputDesc;
        }
    }


}
