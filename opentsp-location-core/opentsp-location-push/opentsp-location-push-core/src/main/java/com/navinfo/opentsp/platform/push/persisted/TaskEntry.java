package com.navinfo.opentsp.platform.push.persisted;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Entry which represent {@link com.navinfo.opentsp.platform.push.api.StoredScheduledTask }
 */
@Entity
@Table(name = "task")
public class TaskEntry {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true, name = "original_id")
    private String originalId;

    @Column
    private String message;

    @Column(nullable = false)
    private String router;

    @Column(name = "router_argument")
    private String routerArgument;

    @Column
    private boolean persistent;

    @Column(nullable = false)
    private long ttl;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date storedTime;

    @Column(name = "delivery_methods")
    private String deliveryMethods;

    @Column(name = "listener_queue")
    private String listenerQueue;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<DeliveringEntry> deliverings;

    @Column(name = "ended")
    public boolean end;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getRouterArgument() {
        return routerArgument;
    }

    public void setRouterArgument(String routerArgument) {
        this.routerArgument = routerArgument;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getDeliveryMethods() {
        return deliveryMethods;
    }

    public void setDeliveryMethods(String deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
    }

    public String getListenerQueue() {
        return listenerQueue;
    }

    public void setListenerQueue(String listenerQueue) {
        this.listenerQueue = listenerQueue;
    }

    public List<DeliveringEntry> getDeliverings() {
        return deliverings;
    }

    public void setDeliverings(List<DeliveringEntry> deliverings) {
        this.deliverings = deliverings;
    }

    /**
     * Flag for ended tasks
     * @return
     */
    public boolean isEnd() {
        return end;
    }

    /**
     * Flag for ended tasks
     * @param end
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    public Date getStoredTime() {
        return storedTime;
    }

    public void setStoredTime(Date storedTime) {
        this.storedTime = storedTime;
    }
}
