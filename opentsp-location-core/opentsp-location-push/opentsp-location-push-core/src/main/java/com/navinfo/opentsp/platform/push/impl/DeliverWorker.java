package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.common.utils.Uuids;
import com.navinfo.opentsp.platform.push.MessageScheduleState;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Class which do delivering: read undelivered data from storage, and pass it to specified deliverer
 */
@Component
public class DeliverWorker {
    private static final Logger LOG = LoggerFactory.getLogger(DeliverWorker.class);
    private final ScheduledTasksStorage storage;
    private final MessageDelivererRegistry messageDeliverer;
    private final Runnable worker = new Runnable() {
        @Override
        public void run() {
            doWork();
        }
    };

    @Autowired
    public DeliverWorker(ScheduledTasksStorage storage, MessageDelivererRegistry messageDeliverer) {
        this.storage = storage;
        this.messageDeliverer = messageDeliverer;
    }

    public void doWork() {
        LOG.debug("执行指令重新下发任务，start");
        Collection<String> ids =storage.getAllSchedules();
        if(!CollectionUtils.isEmpty(ids)) {
            LOG.debug("Receive follow schedules: {}.", ids);
        }
        for(String id: ids) {
            doWorkOn(id);
        }
        LOG.debug("执行指令重新下发任务，end");
    }

    public void doWorkOn(String id) {
        LOG.debug("下发任务id:{}",id);
        StoredScheduledTask schedule = storage.getSchedule(id);
        if(schedule == null) {
            return;
        }
        final MessageScheduleState state = schedule.getState();
        if(state != MessageScheduleState.SCHEDULE && state != MessageScheduleState.DELIVER) {
            storage.endSchedule(schedule);
            return;
        }
        List<DeliveringState> states = schedule.getDeliveringStates();
        for(DeliveringState deliveringState: states) {
            DeliveringStatus status = deliveringState.getStatus();
            if(status != null && status.isEnd()) {
                continue;
            }
            if(status == DeliveringStatus.SCHEDULED) {
                invokeDelivering(schedule, deliveringState);
            } else {
                startDelivering(schedule, deliveringState);
            }
        }


    }

    private void startDelivering(StoredScheduledTask schedule, DeliveringState delivering) {
        ScheduledTask task = schedule.getTask();
        DeliveringState newState = delivering.clone();
        String nextMethod = chooseNextMethod(schedule, delivering);
        newState.setLastMethod(nextMethod);
        if(nextMethod == null) {
            newState.setStatus(DeliveringStatus.FAIL);
            storage.updateDelivering(task.getId(), delivering, newState);
        } else {
            newState.setStatus(DeliveringStatus.SCHEDULED);
            newState.setCallbackId(Uuids.liteRandom().toString());
            // on fail delivering will be executed at next 'doWork' cycle
            if(storage.updateDelivering(task.getId(), delivering, newState)) {
                invokeDelivering(schedule, newState);
            }
        }
    }

    private void invokeDelivering(StoredScheduledTask schedule, DeliveringState delivering) {
        MessageParcel parcel = new MessageParcel();
        parcel.setRecipient(delivering.getRecipient());
        ScheduledTask task = schedule.getTask();
        parcel.setTask(task);
        parcel.setMethod(delivering.getLastMethod());
        parcel.setState(delivering);
        try {
            this.messageDeliverer.deliver(parcel);
        } catch (Exception e) {
            LOG.error(MessageFormat.format("Can not deliver parcel: \"{0}\"", delivering), e);
            deliveringError(task, delivering);
        }
    }

    private void deliveringError(ScheduledTask task, DeliveringState delivering) {
        DeliveringState failState = delivering.clone();
        failState.setStatus(DeliveringStatus.ERROR);
        storage.updateDelivering(task.getId(), delivering, failState);
    }

    /**
     * Return "*" or next appropriate method. It return null if all described methods was used.
     * @param schedule
     * @param delivering
     * @return
     */
    private String chooseNextMethod(StoredScheduledTask schedule, DeliveringState delivering) {
        ScheduledTask task = schedule.getTask();
        String[] methods = StringUtils.commaDelimitedListToStringArray(task.getDeliveryMethods());
        if(methods.length == 0) {
            throw new RuntimeException("No methods in task with id " + task.getId());
        }
        String lastMethod = delivering.getLastMethod();
        if(lastMethod == null) {
            return methods[0];
        }
        boolean useNext = false;
        for(String curr: methods) {
            if(useNext) {
                return curr;
            }
            if(curr.equals(lastMethod)) {
                useNext = true;
            }
        }
        if(useNext) {//we use last method, but no delivery
            return null;
        }
        throw new RuntimeException(MessageFormat.format("We cannot find \"{0}\" in \"{1}\" from task \"{2}\"", lastMethod, methods, task.getId()));
    }

    public Runnable getWorker() {
        return worker;
    }
}
