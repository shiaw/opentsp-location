package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TerminalRuleData;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCAlarmCancelOrNot;

public class Mutual_0995_AlarmCancelOrNot extends DalCommand {

    @Override
    public int processor(Packet packet) {
        try {
            LCAlarmCancelOrNot.AlarmCancelOrNot alarmCancelOrNot = LCAlarmCancelOrNot.AlarmCancelOrNot.parseFrom(packet.getContent());
            TerminalRuleData.SaveOrDelAlarmRule(packet.getTo(), 524288L, alarmCancelOrNot.getIsCancel());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
