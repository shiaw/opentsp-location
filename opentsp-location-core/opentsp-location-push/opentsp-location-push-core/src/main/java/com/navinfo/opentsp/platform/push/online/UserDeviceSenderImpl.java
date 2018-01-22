package com.navinfo.opentsp.platform.push.online;

import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCallback;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.DeviceResult;
import com.navinfo.opentsp.platform.push.DownCommandState;
import com.navinfo.opentsp.platform.push.api.DeliveringStatus;
import com.navinfo.opentsp.platform.push.api.MessageParcel;
import com.navinfo.opentsp.platform.push.event.OperationLogEvent;
import com.navinfo.opentsp.platform.push.impl.DeliveryProcessor;
import com.navinfo.opentsp.platform.push.impl.ScheduleStateWatcher;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Defaul sender implementation
 */
@Component
public class UserDeviceSenderImpl implements UserDeviceSender {

    private final MessageChannel messageChannel;
    private final ScheduleStateWatcher watcher;
    private final ObjectFactory<DeliveryProcessor> processor;
    private final RedisCommandStatusStorage commandStatusStorage;


    @Autowired
    public UserDeviceSenderImpl(MessageChannel messageChannel, ObjectFactory<DeliveryProcessor> processor, ScheduleStateWatcher watcher, RedisCommandStatusStorage commandStatusStorage) {
        this.messageChannel = messageChannel;
        this.processor = processor;
        this.watcher = watcher;
        this.commandStatusStorage = commandStatusStorage;
    }

    @Override
    public void send(UserDeviceSenderContext context) throws Exception {
        String route = context.getRoute();
        // we does not allow null route, because then command is will be sent to our, it cause cyclic storm
        Assert.notNull(route, "route is null");
        try {
            this.messageChannel.sendAndReceive(context.getCommand(), new Callback(context.getParcel()), route);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class Callback implements ResultCallback<Command.Result> {

        private final MessageParcel parcel;

        public Callback(MessageParcel parcel) {
            this.parcel = parcel;
        }

        @Override
        public void onResult(Command.Result result) {
            watcher.notifyTaskState(parcel.getTask(), result);
            processor.getObject().updateStatus(parcel, DeliveringStatus.OK);
            //        commandStatusStorage.modiflyState(parcel.getTask().getId(), DownCommandState.SEND);
        }

        @Override
        public void onError(RuntimeException result) {
            processor.getObject().updateStatus(parcel, DeliveringStatus.ERROR);
            commandStatusStorage.modiflyState(parcel.getTask().getId(), DownCommandState.W_TIMEOUT, null, null);

        }
    }
}
