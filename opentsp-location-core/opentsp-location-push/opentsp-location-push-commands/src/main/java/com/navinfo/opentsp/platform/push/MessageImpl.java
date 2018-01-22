package com.navinfo.opentsp.platform.push;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Simple message implementation
 */
public final class MessageImpl implements MessageWithSubject, MessageWithBody {
    private final String subject;
    private final String body;

    @JsonCreator
    public MessageImpl(@JsonProperty("subject") String subject, @JsonProperty("body") String body) {
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBody() {
        return body;
    }
}
