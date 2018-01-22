package com.navinfo.opentsp.platform.push.api;

import com.navinfo.opentsp.platform.push.ScheduledTask;

/**
 * Contains message, recipient and some other data which will need for {@link com.navinfo.opentsp.platform.push.api.MessageDeliverer}.
 */
public class MessageParcel {

    private ScheduledTask task;
    private DeliveringState state;
    private Object recipient;
    private String method;

    public DeliveringState getState() {
        return state;
    }

    public void setState(DeliveringState state) {
        this.state = state;
    }

    public ScheduledTask getTask() {
        return task;
    }

    public void setTask(ScheduledTask task) {
        this.task = task;
    }

    public Object getRecipient() {
        return recipient;
    }

    public void setRecipient(Object recipient) {
        this.recipient = recipient;
    }

    /**
     * delivering method
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * delivering method
     * @return
     */
    public String getMethod() {
        return method;
    }
}
