package com.navinfo.opentsp.platform.push;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Event which is raised by changing message schedule status.
 */
public final class MessageScheduleEvent {

    public static final class Builder {
        private String id;
        private MessageScheduleState state;
        private Object response;

        /**
         * Task id
         * @return
         */
        public String getId() {
            return id;
        }

        /**
         * Task id
         * @param id
         * @return
         */
        public Builder id(String id) {
            setId(id);
            return this;
        }

        /**
         * Task id
         * @param id
         */
        public void setId(String id) {
            this.id = id;
        }

        public MessageScheduleState getState() {
            return state;
        }

        public Builder state(MessageScheduleState state) {
            setState(state);
            return this;
        }

        public void setState(MessageScheduleState state) {
            this.state = state;
        }

        /**
         * Response on message from task
         * @return
         */
        public Object getResponse() {
            return response;
        }

        /**
         * Response on message from task
         * @param response
         * @return
         */
        public Builder response(Object response) {
            setResponse(response);
            return this;
        }

        /**
         * Response on message from task
         * @param response
         */
        public void setResponse(Object response) {
            this.response = response;
        }

        public MessageScheduleEvent build() {
            return new MessageScheduleEvent(this);
        }
    }

    private final String id;
    private final MessageScheduleState state;
    private final Object response;

    @JsonCreator
    public MessageScheduleEvent(Builder b) {
        this.id = b.id;
        this.state = b.state;
        this.response = b.response;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Task id
     * @return
     */
    public String getId() {
        return id;
    }

    public MessageScheduleState getState() {
        return state;
    }

    /**
     * Response on message from task
     * @return
     */
    public Object getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageScheduleEvent)) {
            return false;
        }

        MessageScheduleEvent that = (MessageScheduleEvent) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (state != that.state) {
            return false;
        }
        if (response != null ? !response.equals(that.response) : that.response != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (response != null ? response.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageScheduleEvent{" +
          "id='" + id + '\'' +
          ", state=" + state +
          ", response=" + response +
          '}';
    }
}
