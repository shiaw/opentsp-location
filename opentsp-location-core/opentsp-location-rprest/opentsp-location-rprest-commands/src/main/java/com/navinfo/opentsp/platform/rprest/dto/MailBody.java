package com.navinfo.opentsp.platform.rprest.dto;

import org.springframework.util.MimeType;

import java.io.Reader;

/**
 * A common iface for any types of mail body. Expected only two types: text and text with attachments.
 */
public interface MailBody {
    MimeType getMimeType();

    /**
     * Stream with encoded data of this body.
     * @return
     */
    Reader  getReader();
}
