package edu.fdu.se.astdiff.miningoperationbean.statementplus;

import edu.fdu.se.astdiff.miningoperationbean.ClusteredActionBean;
import edu.fdu.se.astdiff.miningoperationbean.model.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 */
public class ReturnChangeEntity extends StatementPlusChangeEntity {
    public ReturnChangeEntity(ClusteredActionBean bean) {
        super(bean);
    }

    public void generateDesc(){

    }

    @Override
    public String toString(){
        return this.outputDesc;
    }
}
