package com.navinfo.opentsp.platform.push.persisted;

import com.navinfo.opentsp.platform.push.api.DeliveringStatus;

import javax.persistence.*;

/**
 */
@Entity
@Table(name = "delivering")
public class DeliveringEntry {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "original_id", nullable = false)
    private int originalId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private TaskEntry task;

    @Column(nullable = false)
    private String recipient;

    @Column
    private DeliveringStatus status;

    @Column(name = "last_method")
    private String lastMethod;

    @Column(name = "callback_id")
    private String callbackId;

    public TaskEntry getTask() {
        return task;
    }

    public void setTask(TaskEntry task) {
        this.task = task;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOriginalId() {
        return originalId;
    }

    public void setOriginalId(int originalId) {
        this.originalId = originalId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public DeliveringStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveringStatus status) {
        this.status = status;
    }

    public String getLastMethod() {
        return lastMethod;
    }

    public void setLastMethod(String lastMethod) {
        this.lastMethod = lastMethod;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }
}
