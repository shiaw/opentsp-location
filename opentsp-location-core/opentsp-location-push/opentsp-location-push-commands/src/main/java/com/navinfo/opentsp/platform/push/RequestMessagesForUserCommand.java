package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractSecuredCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * Get list of current user message. Note that this command is modify state of system: mark that retrieved messages is arrived to recipient.
 */
@Secured("ROLE_USER")
@MessageGroup(PushModuleConstants.QUEUE)
public class RequestMessagesForUserCommand extends AbstractSecuredCommand<RequestMessagesForUserCommand.Result> {

    private CommitStrategy commitStrategy = CommitStrategy.NONE;

    public CommitStrategy getCommitStrategy() {
        return commitStrategy;
    }

    public void setCommitStrategy(CommitStrategy commitStrategy) {
        this.commitStrategy = commitStrategy;
    }

    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }

    public static class Result extends CommandResult {
        private List<MessageContainer> messages;

        /**
         * List of messages
         * @return
         */
        public List<MessageContainer> getMessages() {
            return messages;
        }

        /**
         * List of messages
         * @param messages
         */
        public void setMessages(List<MessageContainer> messages) {
            this.messages = messages;
        }
    }
}
