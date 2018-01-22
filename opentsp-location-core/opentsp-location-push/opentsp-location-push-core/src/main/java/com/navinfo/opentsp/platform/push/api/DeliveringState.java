package com.navinfo.opentsp.platform.push.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Structure which holds states of delivering message to each recipient
 */
public final class DeliveringState implements Cloneable {
    private int id;
    private Object recipient;
    private DeliveringStatus status;
    private String lastMethod;
    private String callbackId;

    /**
     * Local Id of delivering in current task
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Local Id of delivering in current task
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public Object getRecipient() {
        return recipient;
    }

    public void setRecipient(Object recipient) {
        this.recipient = recipient;
    }

    public DeliveringStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveringStatus status) {
        this.status = status;
    }

    /**
     * Name of last delivering method
     * @return
     */
    public String getLastMethod() {
        return lastMethod;
    }

    /**
     * Name of last delivering method
     */
    public void setLastMethod(String lastMethod) {
        this.lastMethod = lastMethod;
    }

    /**
     * This id used for concrete delivering method, saved in command to delivery service (for example mail service),
     * and also contains in events from  delivery service.
     * @return
     */
    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveringState)) {
            return false;
        }

        DeliveringState that = (DeliveringState) o;

        if (id != that.id) {
            return false;
        }
        if (callbackId != null ? !callbackId.equals(that.callbackId) : that.callbackId != null) {
            return false;
        }
        if (lastMethod != null ? !lastMethod.equals(that.lastMethod) : that.lastMethod != null) {
            return false;
        }
        if (recipient != null ? !recipient.equals(that.recipient) : that.recipient != null) {
            return false;
        }
        if (status != that.status) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (lastMethod != null ? lastMethod.hashCode() : 0);
        result = 31 * result + (callbackId != null ? callbackId.hashCode() : 0);
        return result;
    }

    public DeliveringState clone() {
        try {
            DeliveringState clone = (DeliveringState) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "DeliveringState{" +
          "id=" + id +
          ", recipient=" + recipient +
          ", status=" + status +
          ", lastMethod='" + lastMethod + '\'' +
          ", callbackId='" + callbackId + '\'' +
          '}';
    }

    public void copyFrom(DeliveringState newState) {
        setStatus(newState.getStatus());
        setLastMethod(newState.getLastMethod());
        setCallbackId(newState.getCallbackId());
        setRecipient(newState.getRecipient());
        setId(newState.getId());
    }
}
