package edu.fdu.se.astdiff.miningactions.bean;


import java.util.Set;

/**
 * Created by huangkaifeng on 2018/1/25.
 *
 */
public class ChangePacket {
    public ChangePacket(){

    }

    private int operationType;
    public String multiEditStr;
    private int operationEntity;
    private int operationSubEntity;
    public int statementType;

    public Set<String> changeSet1;
    public Set<String> changeSet2;

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
