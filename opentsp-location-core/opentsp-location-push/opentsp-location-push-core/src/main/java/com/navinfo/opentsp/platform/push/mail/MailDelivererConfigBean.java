package com.navinfo.opentsp.platform.push.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration bean for mail deliverer
 */
@ConfigurationProperties("opentsp.push.maildeliverer")
public class MailDelivererConfigBean {
    private String from;
    private String template;

    /**
     * email address of sender.
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     * email address of sender.
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Template text for {@link com.navinfo.opentsp.platform.mail.dto.MailPartTemplateText}
     * @return
     */
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
