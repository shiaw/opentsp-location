package com.navinfo.opentsp.platform.push.persisted;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.DeliveringState;
import com.navinfo.opentsp.platform.push.api.ScheduledTasksStorage;
import com.navinfo.opentsp.platform.push.api.StoredScheduledTask;
import com.navinfo.opentsp.platform.push.impl.ScheduleStateWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
* Storage for persisted scheduled tasks.
*/
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PersistedScheduleStorage implements ScheduledTasksStorage {
    private static final Logger LOG = LoggerFactory.getLogger(PersistedScheduleStorage.class);
    private final TasksRepository tasksRepository;
    private final DeliveringsRepository deliveringsRepository;
    private final ObjectMapper objectMapper;
    private final ScheduleStateWatcher watcher;

    @Autowired
    public PersistedScheduleStorage(TasksRepository tasksRepository,
                                    DeliveringsRepository deliveringsRepository,
                                    ObjectMapper objectMapper,
                                    ScheduleStateWatcher watcher) {
        this.tasksRepository = tasksRepository;
        this.deliveringsRepository = deliveringsRepository;
        this.objectMapper = objectMapper;
        this.watcher = watcher;
    }

    @Override
    @Transactional
    public void store(StoredScheduledTask storedScheduledTask) {
        TaskEntry entry = fromTask(storedScheduledTask);
        entry = tasksRepository.save(entry);

        List<DeliveringEntry> deliveringEntries = fromDeliverings(entry, storedScheduledTask.getDeliveringStates());
        deliveringsRepository.save(deliveringEntries);
        watcher.notifyTaskState(storedScheduledTask);
    }

    @Override
    @Transactional
    public Set<String> getAllSchedules() {
        LOG.debug("------------------------------------------(Set)tasksRepository.findAllOriginalIds()");
        return (Set)tasksRepository.findAllOriginalIds();
    }

    @Override
    @Transactional
    public StoredScheduledTask getSchedule(String id) {

        TaskEntry entry = tasksRepository.findByOriginalId(id);
        if(entry == null) {
            return null;
        }
        StoredScheduledTask sst = toSchedule(entry);
        return sst;
    }

    private StoredScheduledTask toSchedule(TaskEntry entry) {
        StoredScheduledTask sst = toTask(entry);
        sst.setDeliveringStates(toDeliverings(entry.getDeliverings()));
        return sst;
    }

    @Override
    @Transactional
    public boolean updateDelivering(String id, DeliveringState oldState, final DeliveringState newState) {
        final TaskEntry task = tasksRepository.findByOriginalId(id);
        if(task == null) {// if task is not present in repository, then we cannot throw exception, but return false
            return false;
        }
        DeliveringEntry targetEntry = null;
        int counter = -1;
        for(DeliveringEntry entry: task.getDeliverings()) {
            counter++;
            if(entry.getOriginalId() != oldState.getId()) {
                continue;
            }
            targetEntry = entry;
            break;
        }
        if(targetEntry == null) {
            return false;
        }
        //check that saved state is equal with oldState
        DeliveringState savedState = new DeliveringState();
        toDelivering(targetEntry, savedState);
        if(!savedState.equals(oldState)) {
            return false;
        }
        fromDelivering(newState, targetEntry);
        deliveringsRepository.save(targetEntry);
        final int index = counter;
        watcher.updateState(new Supplier<StoredScheduledTask>() {
            @Override
            public StoredScheduledTask get() {
                StoredScheduledTask stored = toSchedule(task);
                stored.getDeliveringStates().set(index, newState);
                return stored;
            }
        }, newState);
        return true;
    }

    @Override
    @Transactional
    public void endSchedule(StoredScheduledTask task) {
        watcher.notifyTaskState(task);
        tasksRepository.updateEnd(task.getTask().getId(), true);
    }

    private List<DeliveringEntry> fromDeliverings(TaskEntry task, List<DeliveringState> states) {
        List<DeliveringEntry> entries = new ArrayList<>(states.size());
        for(int i = 0; i < states.size(); ++i) {
            DeliveringState state = states.get(i);

            DeliveringEntry entry = new DeliveringEntry();
            fromDelivering(state, entry);
            entry.setTask(task);
            entries.add(entry);
        }
        return entries;
    }

    private List<DeliveringState> toDeliverings(Collection<DeliveringEntry> entries) {
        List<DeliveringState> states = new ArrayList<>(entries.size());
        for(DeliveringEntry entry : entries) {
            DeliveringState state = new DeliveringState();
            toDelivering(entry, state);
            states.add(state);
        }
        return states;
    }

    private void fromDelivering(DeliveringState state, DeliveringEntry entry) {
        entry.setOriginalId(state.getId());
        entry.setLastMethod(state.getLastMethod());
        entry.setCallbackId(state.getCallbackId());
        entry.setRecipient(toJson(state.getRecipient()));
        entry.setStatus(state.getStatus());
    }

    private void toDelivering(DeliveringEntry entry, DeliveringState state) {
        state.setId(entry.getOriginalId());
        state.setLastMethod(entry.getLastMethod());
        state.setCallbackId(entry.getCallbackId());
        state.setRecipient(fromJson(entry.getRecipient()));
        state.setStatus(entry.getStatus());
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(new JsonHolder(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Object fromJson(String str) {
        try {
            JsonHolder holder = objectMapper.readValue(str, JsonHolder.class);
            return holder.getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TaskEntry fromTask(StoredScheduledTask stored) {
        ScheduledTask task = stored.getTask();
        TaskEntry entry = new TaskEntry();
        entry.setOriginalId(task.getId());
        entry.setDeliveryMethods(task.getDeliveryMethods());
        entry.setListenerQueue(task.getListenerQueue());
        entry.setMessage(toJson(task.getMessage()));
        entry.setPersistent(task.isPersistent());
        entry.setRouter(task.getRouter());
        entry.setRouterArgument(toJson(task.getRouterArgument()));
        entry.setTtl(task.getTtl());
        entry.setStoredTime(stored.getStoredTime());
        return entry;
    }

    private StoredScheduledTask toTask(TaskEntry entry) {
        StoredScheduledTask sst = new StoredScheduledTask();
        sst.setStoredTime(entry.getStoredTime());
        ScheduledTask.Builder stb = ScheduledTask.builder();
        stb.setId(entry.getOriginalId());
        stb.setTtl(entry.getTtl());
        stb.setRouter(entry.getRouter());
        stb.setRouterArgument(fromJson(entry.getRouterArgument()));
        stb.setPersistent(entry.isPersistent());
        stb.setListenerQueue(entry.getListenerQueue());
        stb.setDeliveryMethods(entry.getDeliveryMethods());
        stb.setMessage(fromJson(entry.getMessage()));
        sst.setTask(stb.build());
        return sst;
    }
}
