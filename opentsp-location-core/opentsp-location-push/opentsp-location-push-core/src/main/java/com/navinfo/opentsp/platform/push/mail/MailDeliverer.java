package com.navinfo.opentsp.platform.push.mail;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.mail.AbstractSendMailCommand;
import com.navinfo.opentsp.platform.mail.SendMailCommand;
import com.navinfo.opentsp.platform.mail.SendMailWithTemplateCommand;
import com.navinfo.opentsp.platform.mail.dto.*;
import com.navinfo.opentsp.platform.push.MessageWithBody;
import com.navinfo.opentsp.platform.push.MessageWithSubject;
import com.navinfo.opentsp.platform.push.PushModuleConstants;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.DeliveringId;
import com.navinfo.opentsp.platform.push.api.MessageDeliverer;
import com.navinfo.opentsp.platform.push.api.MessageParcel;
import com.navinfo.opentsp.platform.push.api.UserRecipient;
import com.navinfo.opentsp.platform.users.entity.UserAuthDetails;
import com.navinfo.opentsp.users.commands.security.GetUserDetailsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;

/**
 * Deliver message to user email
 */
@Component
public class MailDeliverer implements MessageDeliverer {

    private MessageChannel messageChannel;
    private MailDelivererConfigBean configBean;

    @Autowired
    public MailDeliverer(MessageChannel messageChannel, MailDelivererConfigBean configBean) {
        this.messageChannel = messageChannel;
        this.configBean = configBean;
    }

    @Override
    public void deliver(MessageParcel parcel) throws Exception {
        final UserAuthDetails userDetails = getUserAuthDetails(parcel);

        ScheduledTask task = parcel.getTask();
        final Object message = task.getMessage();
        Assert.notNull(message, getClass().getName() + " does not support null messages");

        final String template = configBean.getTemplate();
        boolean hasTemplate = StringUtils.hasText(template);
        AbstractSendMailCommand command = hasTemplate? new SendMailWithTemplateCommand() : new SendMailCommand();
        command.setCallbackRoutingKey(PushModuleConstants.QUEUE);
        MailHeadImpl mailHead = createMailHead(parcel, userDetails, task, message);
        if(hasTemplate) {
            SendMailWithTemplateCommand mwtc = (SendMailWithTemplateCommand) command;
            mwtc.setTemplate(MailTemplateImpl.builder()
              .headSource(mailHead)
              .bodySource(new MailPartTemplateText(MimeTypeUtils.TEXT_PLAIN, template))
              .build());
            mwtc.setSources(Collections.<MailSource>singletonList(MailSourceImpl.builder().addVariable("message", message).build()));
        } else {
            MailMessageImpl.Builder mmb = MailMessageImpl.builder();
            mmb.setHead(mailHead);
            mmb.setBody(extractBody(message));
            ((SendMailCommand)command).setMessage(mmb.build());
        }

        messageChannel.send(command);
    }

    private MailHeadImpl createMailHead(MessageParcel parcel, UserAuthDetails userDetails, ScheduledTask task, Object message) {
        return MailHeadImpl.builder()
          .customId(DeliveringId.from(task, parcel.getState()))
          .from(configBean.getFrom())
          .to(Arrays.asList(userDetails.getEmail()))
          .subject(extractSubject(message))
          .build();
    }

    private String extractSubject(Object message) {
        if(message instanceof MessageWithSubject) {
            return ((MessageWithSubject)message).getSubject();
        }
        return null;
    }

    private MailBody extractBody(Object message) {
        if(message instanceof MessageWithBody) {
            return new MailTextBody(((MessageWithBody)message).getBody());
        }
        return new MailTextBody(message.toString());
    }

    private UserAuthDetails getUserAuthDetails(MessageParcel parcel) throws Exception {
        // we support only user recipient
        UserRecipient userRecipient = (UserRecipient) parcel.getRecipient();
        GetUserDetailsCommand getUserDetailsCommand = new GetUserDetailsCommand();
        getUserDetailsCommand.setUsername(userRecipient.getUsername());
        GetUserDetailsCommand.Result result = messageChannel.sendAndReceive(getUserDetailsCommand);
        return (UserAuthDetails) result.getUserDetails();
    }
}
