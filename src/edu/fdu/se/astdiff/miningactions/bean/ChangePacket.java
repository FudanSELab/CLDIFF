package edu.fdu.se.astdiff.miningactions.bean;

import edu.fdu.se.astdiff.miningoperationbean.model.ChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/25.
 */
public class ChangePacket {
    public ChangePacket(){

    }

    private int operationType;
    private int operationEntity;
    private int operationSubEntity;
    public int statementType;

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }



    public void setOperationEntity(int operationEntity) {
        this.operationEntity = operationEntity;
    }

    public int getStatementType() {
        return statementType;
    }

    public void setStatementType(int statementType) {
        this.statementType = statementType;
    }

    public void setOperationSubEntity(int operationSubEntity) {
        this.operationSubEntity = operationSubEntity;
    }

    public int getOperationType() {
        return operationType;
    }

    public int getOperationSubEntity() {
        return operationSubEntity;
    }

    public int getOperationEntity() {
        return operationEntity;
    }
}
