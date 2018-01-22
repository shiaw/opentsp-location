package com.navinfo.opentsp.platform.da.core.rmi;

import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by wzw on 2016/9/20.
 */
public abstract class Dacommand extends Command {
    public static Logger logger = LoggerFactory.getLogger(Dacommand.class);
    /**
     * 包指令处理
     * @param packet
     * @return
     */
    public abstract Packet processor(Packet packet);

    /**
     * 发送通用应答
     *
     * @param to
     * @param responsesSerialNumber
     * @param responsesId
     * @param result
     * @return
     */
    public Packet commonResponsesForPlatform(long to, int responsesSerialNumber,
                                          int responsesId, LCPlatformResponseResult.PlatformResponseResult result) {
        Packet packet = new Packet(true);
        LCServerCommonRes.ServerCommonRes.Builder builder = LCServerCommonRes.ServerCommonRes.newBuilder();
        builder.setSerialNumber(responsesSerialNumber);
        builder.setResponseId(responsesId);
        builder.setResults(result);

        packet.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
        packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
        packet.setContent(builder.build().toByteArray());
        // 将唯一标识设置为当前节点标识
        packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
        packet.setFrom(NodeHelper.getNodeCode());
        packet.setTo(to);
        return packet;
    }


}
