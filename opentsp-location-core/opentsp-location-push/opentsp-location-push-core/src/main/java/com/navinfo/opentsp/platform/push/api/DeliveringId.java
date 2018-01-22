package com.navinfo.opentsp.platform.push.api;

import com.navinfo.opentsp.common.utils.StringUtils;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import org.springframework.util.Assert;

/**
 * Structure with ID-s of delivering and task.
 */
public final class DeliveringId {
    private String task;
    private int delivering;


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getDelivering() {
        return delivering;
    }

    public void setDelivering(int delivering) {
        this.delivering = delivering;
    }

    /**
     * Create string representation of {@link DeliveringId }
     * @param task
     * @param delivering
     * @return
     */
    public static String from(ScheduledTask task, DeliveringState delivering) {
        int deliveringId = delivering.getId();
        String taskId = task.getId();
        Assert.hasText(taskId, "task.id is empty or null");
        Assert.isTrue(deliveringId >= 0, "delivering.id is less than zero");
        return taskId + " " + deliveringId;
    }

    /**
     * Pasre string into {@link DeliveringId }
     * @param str
     * @return
     */
    public static DeliveringId parse(String str) {
        String[] strings = StringUtils.splitLast(str, " ");
        DeliveringId ids = new DeliveringId();
        String taskId = strings[0];
        Assert.hasText(taskId, "task.id is empty or null");
        ids.setTask(taskId);
        int deliveringId = Integer.parseInt(strings[1]);
        Assert.isTrue(deliveringId >= 0, "delivering.id is less than zero");
        ids.setDelivering(deliveringId);
        return ids;
    }
}
