package com.navinfo.opentsp.platform.push.api;

import com.navinfo.opentsp.platform.push.MessageScheduleEvent;
import com.navinfo.opentsp.platform.push.ScheduledTask;

/**
 * Data structure for holding core events, it also holds original task and some other data which can be used by listeners.
 */
public final class LocalMessageEvent {
    private final ScheduledTask task;
    private final MessageScheduleEvent event;

    public LocalMessageEvent(ScheduledTask task, MessageScheduleEvent event) {
        this.task = task;
        this.event = event;
    }

    public ScheduledTask getTask() {
        return task;
    }

    public MessageScheduleEvent getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "LocalMessageEvent{" +
          "task=" + task +
          ", event=" + event +
          '}';
    }

    public static LocalMessageEvent create(StoredScheduledTask stored) {
        ScheduledTask scheduledTask = stored.getTask();
        return new LocalMessageEvent(scheduledTask, MessageScheduleEvent.builder()
          .id(scheduledTask.getId())
          .state(stored.getState()).build());
    }
}
