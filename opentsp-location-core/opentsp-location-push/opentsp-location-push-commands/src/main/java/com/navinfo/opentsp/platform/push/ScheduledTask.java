package com.navinfo.opentsp.platform.push;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Task for message scheduling.
 */
public final class ScheduledTask {

    public static final class Builder {
        private String id;
        private Object message;
        private String router;
        private Object routerArgument;
        private boolean persistent;
        private long ttl;
        private String deliveryMethods;
        private String listenerQueue;

        public String getId() {
            return id;
        }

        /**
         * Task id is used for resolve duplicates
         *
         * @param id
         * @return
         */
        public Builder id(String id) {
            setId(id);
            return this;
        }

        public void setId(String id) {
            this.id = id;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
        public Object getMessage() {
            return message;
        }

        /**
         * Payload of original command. ScheduledTask wraps original command
         *
         * @param message
         * @return
         */
        public Builder message(Object message) {
            setMessage(message);
            return this;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public String getRouter() {
            return router;
        }

        /**
         * Name of router like com.navinfo.opentsp.platform.push.impl.UserDeviceRouter#NAME
         *
         * @param router
         * @return
         */
        public Builder router(String router) {
            setRouter(router);
            return this;
        }

        public void setRouter(String router) {
            this.router = router;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
        public Object getRouterArgument() {
            return routerArgument;
        }

        /**
         * Arguments for searching recipients
         * Can be different for different router
         * example deviceIdv for UserDeviceRouter
         *
         * @param routerArgument
         */
        public Builder routerArgument(Object routerArgument) {
            setRouterArgument(routerArgument);
            return this;
        }

        public void setRouterArgument(Object routerArgument) {
            this.routerArgument = routerArgument;
        }

        public boolean isPersistent() {
            return persistent;
        }

        /**
         * Enable persisting task in RDB
         * by default task will be stored only in redis
         *
         * @param persistent
         * @return
         */
        public Builder persistent(boolean persistent) {
            setPersistent(persistent);
            return this;
        }

        public void setPersistent(boolean persistent) {
            this.persistent = persistent;
        }

        public long getTtl() {
            return ttl;
        }

        /**
         * Task tt;
         *
         * @param ttl
         * @return
         */
        public Builder ttl(long ttl) {
            setTtl(ttl);
            return this;
        }

        public void setTtl(long ttl) {
            this.ttl = ttl;
        }

        /**
         * Comma separated list of delivery method identifiers.
         */
        public Builder deliveryMethods(String ... deliveryMethods) {
            setDeliveryMethods(StringUtils.arrayToCommaDelimitedString(deliveryMethods));
            return this;
        }

        /**
         * Comma separated list of delivery method identifiers.
         * @param deliveryMethods
         * @return
         */
        public Builder deliveryMethods(String deliveryMethods) {
            setDeliveryMethods(deliveryMethods);
            return this;
        }

        /**
         * Comma separated list of delivery method identifiers.
         *
         * @param deliveryMethods
         */
        public void setDeliveryMethods(String deliveryMethods) {
            this.deliveryMethods = deliveryMethods;
        }

        public String getListenerQueue() {
            return listenerQueue;
        }

        /**
         * Name of queue for delivering result
         *
         * @param listenerQueue
         * @return
         */
        public Builder listenerQueue(String listenerQueue) {
            setListenerQueue(listenerQueue);
            return this;
        }

        public void setListenerQueue(String listenerQueue) {
            this.listenerQueue = listenerQueue;
        }

        public ScheduledTask build() {
            return new ScheduledTask(this);
        }
    }

    private final String id;
    private final Object message;
    private final String router;
    private final Object routerArgument;
    private final boolean persistent;
    private final long ttl;
    private final String deliveryMethods;
    private final String listenerQueue;

    @JsonCreator
    public ScheduledTask(Builder builder) {
        this.id = builder.id;
        this.message = builder.message;
        this.router = builder.router;
        this.routerArgument = builder.routerArgument;
        this.persistent = builder.persistent;
        this.ttl = builder.ttl;
        this.deliveryMethods = builder.deliveryMethods;
        this.listenerQueue = builder.listenerQueue;
        Assert.hasText(this.id, "id is null or empty");
        Assert.hasText(this.router, "router is null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Task id is used for resolve duplicates
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * payload of original command. ScheduledTask wraps original command
     *
     * @return  message
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public Object getMessage() {
        return message;
    }

    /**
     * Name of router like com.navinfo.opentsp.platform.push.impl.UserDeviceRouter#NAME
     *
     * @return router
     */
    public String getRouter() {
        return router;
    }

    /**
     * Arguments for searching recipients
     * Can be different for different router
     * example deviceIdv for UserDeviceRouter
     *
     * @return  routerArgument
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public Object getRouterArgument() {
        return routerArgument;
    }

    /**
     * Enable persisting task in RDB
     * by default task will be stored only in redis
     *
     * @return persistent
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     * Task tt;
     *
     * @return ttl
     */
    public long getTtl() {
        return ttl;
    }

    /**
     * Comma separated list of delivery method identifiers.
     * example: email, device (com.navinfo.opentsp.platform.push.online.OnlineDeliverer#ID), ...
     *
     * @return
     */
    public String getDeliveryMethods() {
        return deliveryMethods;
    }

    /**
     * Name of queue for delivering result
     *
     * @return listenerQueue
     */
    public String getListenerQueue() {
        return listenerQueue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduledTask)) {
            return false;
        }

        ScheduledTask that = (ScheduledTask) o;

        if (persistent != that.persistent) {
            return false;
        }
        if (ttl != that.ttl) {
            return false;
        }
        if (deliveryMethods != null ? !deliveryMethods.equals(that.deliveryMethods) : that.deliveryMethods != null) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (listenerQueue != null ? !listenerQueue.equals(that.listenerQueue) : that.listenerQueue != null) {
            return false;
        }
        if (message != null ? !message.equals(that.message) : that.message != null) {
            return false;
        }
        if (router != null ? !router.equals(that.router) : that.router != null) {
            return false;
        }
        if (routerArgument != null ? !routerArgument.equals(that.routerArgument) : that.routerArgument != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (router != null ? router.hashCode() : 0);
        result = 31 * result + (routerArgument != null ? routerArgument.hashCode() : 0);
        result = 31 * result + (persistent ? 1 : 0);
        result = 31 * result + (int) (ttl ^ (ttl >>> 32));
        result = 31 * result + (deliveryMethods != null ? deliveryMethods.hashCode() : 0);
        result = 31 * result + (listenerQueue != null ? listenerQueue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ScheduledTask{" +
                "id='" + id + '\'' +
                ", message=" + message +
                ", router='" + router + '\'' +
                ", routerArgument=" + routerArgument +
                ", persistent=" + persistent +
                ", ttl=" + ttl +
                ", deliveryMethods='" + deliveryMethods + '\'' +
                ", listenerQueue='" + listenerQueue + '\'' +
                '}';
    }
}
