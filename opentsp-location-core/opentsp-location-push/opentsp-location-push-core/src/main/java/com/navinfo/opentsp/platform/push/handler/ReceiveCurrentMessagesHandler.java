package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.push.*;
import com.navinfo.opentsp.platform.push.api.*;
import com.navinfo.opentsp.platform.push.impl.DeliverWorker;
import com.navinfo.opentsp.platform.push.impl.DeliveryProcessor;
import com.navinfo.opentsp.platform.push.online.OnlineUser;
import com.navinfo.opentsp.platform.push.online.OnlineUsers;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import com.navinfo.opentspcore.common.security.ExtendedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handler which send messages to command sender and mark its as received. Note that this behavior potentially is
 * not save, because we can loose command response.
 */
@Component
public class ReceiveCurrentMessagesHandler extends AbstractCommandHandler<RequestMessagesForUserCommand, RequestMessagesForUserCommand.Result> {

    private final OnlineUsers onlineUsers;
    private final DeliverWorker deliverWorker;
    private final DeliveryProcessor processor;


    @Autowired
    public ReceiveCurrentMessagesHandler(DeliverWorker deliverWorker, OnlineUsers onlineUsers, DeliveryProcessor processor) {
        super(RequestMessagesForUserCommand.class, RequestMessagesForUserCommand.Result.class);
        this.deliverWorker = deliverWorker;
        this.onlineUsers = onlineUsers;
        this.processor = processor;
    }

    @Override
    public RequestMessagesForUserCommand.Result handle(final RequestMessagesForUserCommand command) {
        final ExtendedUserDetails principal = (ExtendedUserDetails) command.getAuthentication().getPrincipal();
        final CommitStrategy commitStrategy = command.getCommitStrategy();
        final List<MessageContainer> messages = new ArrayList<>();
        Consumer<MessageParcel> callback = new Consumer<MessageParcel>() {
            @Override
            public void accept(MessageParcel parcel) {
                ScheduledTask scheduledTask = parcel.getTask();
                DeliveringState delivering = parcel.getState();
                MessageContainer container = new MessageContainer();
                container.setId(DeliveringId.from(scheduledTask, delivering));
                container.setMessage(scheduledTask.getMessage());
                messages.add(container);
                // delivery commit is do by sending appropriate event
                if(commitStrategy == CommitStrategy.AUTO ||
                  (commitStrategy == CommitStrategy.AUTO_NON_PERSISTENT && !scheduledTask.isPersistent())) {
                    processor.commit(parcel);
                }
            }
        };
        try(OnlineUser h = onlineUsers.onlineTemporary(principal, callback)) {
            // we manually run work, to force sync process of all message delivers
            this.deliverWorker.doWork();
        }
        RequestMessagesForUserCommand.Result result = new RequestMessagesForUserCommand.Result();
        result.fillResult(ResultCode.OK);
        result.setMessages(messages);
        return result;
    }
}
