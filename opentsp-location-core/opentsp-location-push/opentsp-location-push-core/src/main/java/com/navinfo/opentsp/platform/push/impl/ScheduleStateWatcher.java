package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.platform.push.MessageScheduleEvent;
import com.navinfo.opentsp.platform.push.MessageScheduleState;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Component which get notification of changing status of message schedule delivering, and compute schedule state from its. <p/>
 * Conceptually this component part of {@link com.navinfo.opentsp.platform.push.impl.DeliveryProcessor } but we cannot
 * combine them because it  will caused cyclic dependencies.
 */
@Component
public class ScheduleStateWatcher {
    private final LocalMessageListener listener;

    @Autowired
    public ScheduleStateWatcher(LocalMessageEventHub listener) {
        this.listener = listener;
    }

    /**
     * Send event with task state.
     * @param task
     */
    public void notifyTaskState(StoredScheduledTask task) {
        LocalMessageEvent event = LocalMessageEvent.create(task);
        fire(event);
    }

    private void fire(LocalMessageEvent event) {
        this.listener.accept(event);
    }

    public void notifyTaskState(ScheduledTask task, Object result) {
        MessageScheduleEvent mse = MessageScheduleEvent.builder()
          .id(task.getId())
          .state(MessageScheduleState.DELIVER)
          .response(result)
          .build();
        fire(new LocalMessageEvent(task, mse));
    }

    /**
     *  Notify about task state change at each delivering end
     * @param taskSupplier
     * @param state
     * @return
     */
    public void updateState(Supplier<StoredScheduledTask> taskSupplier, DeliveringState state) {
        DeliveringStatus status = state.getStatus();
        if(!status.isEnd()) {
            return;
        }
        StoredScheduledTask task = taskSupplier.get();
        // we need event of any new state and delivering, therefore checking of state is redundant
        //notifyTaskState(task);
    }
}
