package com.navinfo.opentsp.platform.push.api;

import java.util.Collection;
import java.util.Set;

/**
 * Repository for different scheduled tasks storage.
 */
public interface ScheduledTasksStorage {
    /**
     * method uses for initial saving of storedScheduledTask
     * @param storedScheduledTask
     */
    void store(StoredScheduledTask storedScheduledTask);

    /**
     * Returns collection of all unfinished schedules
     * @return
     */
    Collection<String> getAllSchedules();

    /**
     * Returns StoredScheduledTask
     * @param id
     * @return
     */
    StoredScheduledTask getSchedule(String id);

    /**
     * Update old delivering with new state, it will success only if old deliveryState is equal with stored.
     * @param id
     * @param oldState
     * @param newState
     * @return true if success
     */
    boolean updateDelivering(String id, DeliveringState oldState, DeliveringState newState);

    /**
     * End schedule. It can remove schedule, or mark it as archived, and also sends appropriate event.
     * @param task
     */
    void endSchedule(StoredScheduledTask task);
}
