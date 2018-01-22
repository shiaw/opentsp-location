package com.navinfo.opentsp.platform.push.persisted;

import javax.persistence.*;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/10
 * @modify
 * @copyright opentsp
 */
@Entity
@Table(name = "lc_terminal_operation_log")
public class TerminalOperationLogEntry {
    @Id
    @GeneratedValue
    private long tolId;

    @Column(nullable = false, name = "terminal_id")
    private String terminalId;
    @Column
    private int operationType;

    @Column
    private long operationTime;

    @Column
    private String operationContent;


    @Column
    private String  operatorResult;

    @Column
    private String operationId;

    public long getTolId() {
        return tolId;
    }

    public void setTolId(long tolId) {
        this.tolId = tolId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public long getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(long operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationContent() {
        return operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    public String getOperatorResult() {
        return operatorResult;
    }

    public void setOperatorResult(String operatorResult) {
        this.operatorResult = operatorResult;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
