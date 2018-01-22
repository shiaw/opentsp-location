package com.navinfo.opentsp.platform.push;

/**
 * Primitive message wrapper, which contains raw message and its id. We use it in communications with client
 * instead of {@link com.navinfo.opentsp.platform.push.ScheduledTask }
 */
public class MessageContainer {

    private String id;
    private Object message;

    /**
     * It use comlpex id of task and concrete delivering record.
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * It use comlpex id of task and concrete delivering record.
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageContainer)) {
            return false;
        }

        MessageContainer container = (MessageContainer) o;

        if (id != null ? !id.equals(container.id) : container.id != null) {
            return false;
        }
        if (message != null ? !message.equals(container.message) : container.message != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageContainer{" +
          "id='" + id + '\'' +
          ", message=" + message +
          '}';
    }
}
