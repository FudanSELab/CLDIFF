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





}
