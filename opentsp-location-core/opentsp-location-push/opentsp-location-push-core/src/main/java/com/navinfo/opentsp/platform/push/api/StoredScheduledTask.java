package com.navinfo.opentsp.platform.push.api;

import com.navinfo.opentsp.platform.push.MessageScheduleState;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Entity which stores scheduled task
 */
public final class StoredScheduledTask implements Cloneable {
    private ScheduledTask task;
    private List<DeliveringState> deliveringStates = new ArrayList<>();
    private Date storedTime;

    public ScheduledTask getTask() {
        return task;
    }

    public void setTask(ScheduledTask task) {
        this.task = task;
    }

    /**
     *  time when this message was stored
     * @return
     */
    public Date getStoredTime() {
        return storedTime;
    }

    /**
     *  time when this message was stored
     */
    public void setStoredTime(Date storedTime) {
        Assert.notNull(storedTime, "storedTime is null");
        this.storedTime = storedTime;
    }

    public MessageScheduleState getState() {
        int ok = 0;
        //note that we must use same time on all service instances
        if(System.currentTimeMillis() - storedTime.getTime() > task.getTtl()) {
            return MessageScheduleState.EXCEED;
        }
        for(DeliveringState deliveringState: deliveringStates) {
            DeliveringStatus status = deliveringState.getStatus();
            if(status == null) {
                continue;
            }
            switch (status) {
                case OK:
                    ok++;
                    break;
                case FAIL:
                    return MessageScheduleState.ERROR;
            }
        }
        if(ok == deliveringStates.size()) {
            return MessageScheduleState.SUCCESS;
        }
        if(ok > 0) {
            return MessageScheduleState.DELIVER;
        }
        return MessageScheduleState.SCHEDULE;
    }

    public void setDeliveringStates(List<DeliveringState> deliveringStates) {
        this.deliveringStates.clear();
        if(deliveringStates != null) {
            this.deliveringStates.addAll(deliveringStates);
        }
    }

    public List<DeliveringState> getDeliveringStates() {
        return deliveringStates;
    }

    public StoredScheduledTask clone() {
        try {
            StoredScheduledTask clone = (StoredScheduledTask)super.clone();
            List<DeliveringState> states = new ArrayList<>(clone.deliveringStates);
            for(ListIterator<DeliveringState> i = states.listIterator(); i.hasNext();) {
                DeliveringState ds = i.next();
                i.set(ds.clone());
            }
            clone.setDeliveringStates(states);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoredScheduledTask)) {
            return false;
        }

        StoredScheduledTask that = (StoredScheduledTask) o;

        if (deliveringStates != null ? !deliveringStates.equals(that.deliveringStates) : that.deliveringStates != null) {
            return false;
        }
        if (task != null ? !task.equals(that.task) : that.task != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = task != null ? task.hashCode() : 0;
        result = 31 * result + (deliveringStates != null ? deliveringStates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StoredScheduledTask{" +
          "task=" + task +
          ", deliveringStates=" + deliveringStates +
          '}';
    }
}
