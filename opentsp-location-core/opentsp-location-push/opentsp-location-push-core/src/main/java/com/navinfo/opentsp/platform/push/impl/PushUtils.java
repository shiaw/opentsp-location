package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Some utilities fro push-module.
 */
public class PushUtils {

    /**
     * Update single delivering status to {@link com.navinfo.opentsp.platform.push.api.DeliveringStatus#OK} in storage for specified parcel.
     * @param storage
     * @param task
     * @param oldState
     * @return
     */
    public static boolean commitDelivering(ScheduledTasksStorage storage, ScheduledTask task, DeliveringState oldState) {
        DeliveringState newState = oldState.clone();
        newState.setStatus(DeliveringStatus.OK);
        return storage.updateDelivering(task.getId(), oldState, newState);
    }

    public static List<DeliveringState> createInitialDeliverings(Collection<?> recipients) {
        Assert.notEmpty(recipients, "list of recipients is empty");
        List<DeliveringState> list = new ArrayList<>(recipients.size());
        for(Object recipient: recipients) {
            DeliveringState status = new DeliveringState();
            status.setRecipient(recipient);
            list.add(status);
        }
        return list;
    }
}
