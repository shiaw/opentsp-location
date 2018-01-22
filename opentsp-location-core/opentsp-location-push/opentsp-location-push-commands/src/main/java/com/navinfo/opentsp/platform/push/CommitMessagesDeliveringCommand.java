package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractSecuredCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.springframework.security.access.annotation.Secured;

import javax.validation.constraints.NotNull;
import java.util.Collection;


/**
 * Commit messages delivering
 */
@Secured("ROLE_USER")
@MessageGroup(PushModuleConstants.QUEUE)
public class CommitMessagesDeliveringCommand extends AbstractSecuredCommand<CommandResult> {

    @NotNull
    private Collection<String> messagesIds;

    /**
     * Collection of delivered messages ids.
     * @return
     */
    public Collection<String> getMessagesIds() {
        return messagesIds;
    }

    /**
     * Collection of delivered messages ids.
     * @param messagesIds
     */
    public void setMessagesIds(Collection<String> messagesIds) {
        this.messagesIds = messagesIds;
    }

    @Override
    public Class<? extends CommandResult> getResultType() {
        return CommandResult.class;
    }
}
