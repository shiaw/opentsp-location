package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.TerminalManage;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.TerminalEntity;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.DACommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCGetTerminalInfosRes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询终端信息应答<br>
 * <p>
 * RP通过RMI调用DA获得应答
 */
//@Component
public class DA_1900_GetTerminalInfosRes extends DACommand {

    @Override
    public Packet processor(Packet packet) {
        try {
            LCGetTerminalInfosRes.GetTerminalInfosRes getTerminalInfos = LCGetTerminalInfosRes.GetTerminalInfosRes.parseFrom(packet.getContent());
            List<LCTerminalInfo.TerminalInfo> terminalInfos = getTerminalInfos.getTerminalsList();
            //logger.info("当前节点[ " + NodeHelper.getNodeCode() + " ]共加载[ " + terminalInfos.size() + " ]终端.");
            if (terminalInfos != null && terminalInfos.size() > 0) {
                // 加载终端信息到缓存
                for (LCTerminalInfo.TerminalInfo terminalInfo : terminalInfos) {
                    TerminalEntity entity = new TerminalEntity();
                    entity.setTerminalId(terminalInfo.getTerminalId());
                    TerminalManage.getInstance().addTerminal(entity);
                }
            }
            //DA_1100_ServerCommonRes.isLoadTerminal = true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}
