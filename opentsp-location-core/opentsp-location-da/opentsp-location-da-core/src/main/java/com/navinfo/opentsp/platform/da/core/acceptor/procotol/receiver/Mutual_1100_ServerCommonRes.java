package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes.ServerCommonRes;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMMutualCommandFacotry;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;
@DaRmiNo(id = "1100")
public class Mutual_1100_ServerCommonRes extends Dacommand {

    @Override
    public Packet processor(Packet packet) {
        try {
            ServerCommonRes commonRes = ServerCommonRes.parseFrom(packet
                    .getContent());
            logger.info("收到LcNode通用应答：CommandId["
                    + Convert.decimalToHexadecimal(commonRes.getResponseId(), 4)
                    + "],ResponseSerialNumber["
                    + Convert.decimalToHexadecimal(commonRes.getSerialNumber(),
                    4) + "],Result[" + commonRes.getResults().name()
                    + "]");

            if (commonRes.getResponseId() == AllCommands.NodeCluster.ReportServerIdentify_VALUE &&
                    commonRes.getResults().getNumber() == PlatformResponseResult.success_VALUE) {

                Packet heartBeatToMM = new Packet(true);
                heartBeatToMM.setCommand(AllCommands.NodeCluster.HeartBeatToMM_VALUE);
                heartBeatToMM.setUniqueMark(NodeHelper.getNodeUniqueMark());
                heartBeatToMM.setProtocol(LCMessageType.PLATFORM);
                MMMutualCommandFacotry.processor(heartBeatToMM);

            }


            //心跳回复应答
            if (packet.getCommand() == AllCommands.Platform.Heartbeat_VALUE) {
                //暂时无逻辑实现
                logger.info("收到心跳应答回复.");
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

}
