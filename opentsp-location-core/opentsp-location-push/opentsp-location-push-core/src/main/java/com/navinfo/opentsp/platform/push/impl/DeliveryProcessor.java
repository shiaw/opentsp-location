package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.platform.push.MessageScheduleState;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * Main entry which manage scheduled messages and deliver it.
 */
@Component
public class DeliveryProcessor {

    private final ScheduledTasksStorage storage;
    private final MessageRouterRegistry router;
    private final DeliverWorker deliverWorker;

    @Autowired
    public DeliveryProcessor(ScheduledTasksStorage storage, MessageRouterRegistry router, DeliverWorker deliverWorker) {
        this.storage = storage;
        this.router = router;
        this.deliverWorker = deliverWorker;
    }

    /**
     * Put task into storage and schedule its delivering.
     * @param task
     */
    public void schedule(ScheduledTask task) {
        //first we must store message
        StoredScheduledTask storedSchedule = createStoredTask(task);
        MessageScheduleState state = storedSchedule.getState();
        if(state != MessageScheduleState.SCHEDULE) {
            throw new RuntimeException("Illegal state of schedule: " + state + ", new schedule can have only " + MessageScheduleState.SCHEDULE + " state.");
        }
        storage.store(storedSchedule);

        // then immediate invoke deliver process, for delivering message now if it possible
        this.deliverWorker.doWorkOn(task.getId());
    }

    /**
     * Create stored task by usual task.
     * @param task
     * @return
     */
    public StoredScheduledTask createStoredTask(ScheduledTask task) {
        StoredScheduledTask storedSchedule = new StoredScheduledTask();
        storedSchedule.setStoredTime(new Date());
        storedSchedule.setTask(task);
        Collection<?> recipients = this.router.resolveRoute(task);
        storedSchedule.setDeliveringStates(PushUtils.createInitialDeliverings(recipients));
        return storedSchedule;
    }

    public void commit(MessageParcel parcel) {
        updateStatus(parcel, DeliveringStatus.OK);
    }

    /**
     * Set status of concrete delivering
     * @param parcel
     * @param status status of delivering
     * @return
     */
    public boolean updateStatus(MessageParcel parcel, DeliveringStatus status) {
        DeliveringState oldState = parcel.getState();
        DeliveringState newState = oldState.clone();
        newState.setStatus(DeliveringStatus.OK);
        return storage.updateDelivering(parcel.getTask().getId(), oldState, newState);
    }

}
