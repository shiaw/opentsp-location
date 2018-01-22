package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.platform.push.api.DeliveringState;
import com.navinfo.opentsp.platform.push.api.ScheduledTasksStorage;
import com.navinfo.opentsp.platform.push.api.StoredScheduledTask;

import java.util.HashSet;
import java.util.Set;

/**
 * Storage for message schedules.
 */
public class ScheduledTasksStorageImpl implements ScheduledTasksStorage {

    /**
     * It store messages in memory storage (redis).
     */
    private ScheduledTasksStorage fastMessages;

    public ScheduledTasksStorageImpl(ScheduledTasksStorage fastMessages) {
        this.fastMessages = fastMessages;
    }

    @Override
    public void store(StoredScheduledTask sms) {
        final boolean persistent = sms.getTask().isPersistent();
        final ScheduledTasksStorage storage =  fastMessages;
        storage.store(sms);
    }

    @Override
    public Set getAllSchedules() {
        Set<String> ids = new HashSet<>(this.fastMessages.getAllSchedules());
      //  ids.addAll(this.persistedMessages.getAllSchedules());
        return ids;
    }

    @Override
    public StoredScheduledTask getSchedule(String id) {
        StoredScheduledTask schedule = this.fastMessages.getSchedule(id);
//        if(schedule == null) {
//            schedule = this.persistedMessages.getSchedule(id);
//        }
        return schedule;
    }

    @Override
    public boolean updateDelivering(String id, DeliveringState oldState, DeliveringState newState) {
       return this.fastMessages.updateDelivering(id, oldState, newState);

        // note, that ok == false, does not meaning that fastMessages not contains id, it only meant that it cannot update state
        // therefore we can rewrite this method for stricter behaviour
    //    return this.persistedMessages.updateDelivering(id, oldState, newState);
    }

    @Override
    public void endSchedule(StoredScheduledTask task) {
            fastMessages.endSchedule(task);
    }
}
