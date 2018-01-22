package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMMutualCommandFacotry;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCDataTransfer;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCDataTransfer.DataTransfer;

import java.util.List;

public class MM_0734_DataTransfer extends MMCommand {

    @Override
    public int processor(Packet packet) {
        try {
            LCDataTransfer.DataTransfer data = DataTransfer.parseFrom(packet.getContent());
            logger.info("收到转存任务");
            //是否新任务
            final boolean isNewTask = data.getIsNewTask();
            final long beginDate = data.getBeginDate();
            final long endDate = data.getEndDate();
            final long from = packet.getFrom();
            //终端列表
            final List<Long> terminalIdList = data.getTerminalIdList();

            //数据转存消息回复平台通用应答（0x1100）
            super.commonResponses(packet.getFrom(), packet.getSerialNumber(),
                    packet.getCommand(), PlatformResponseResult.success);
            new Thread() {
                @Override
                public void run() {
//                    SaveGpsDataTask task = new SaveGpsDataTask(terminalIdList);
//                    try {
//                        task.run(isNewTask, beginDate, endDate);
//                    } catch (UnknownHostException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }

                    Packet _out_packet = new Packet(true);
                    _out_packet.setCommand(AllCommands.NodeCluster.DataTransferCompletionNotice_VALUE);
                    _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
                    _out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
                    _out_packet.setTo(from);
                    _out_packet.setFrom(NodeHelper.getNodeCode());
                    MMMutualCommandFacotry.processor(_out_packet);
                }
            }.start();
//			return super.write(buildPacket(packet));
            return 1;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 构建packet
     *
     * @param list
     * @return
     */
    private Packet buildPacket(Packet packet) {

        Packet _out_packet = new Packet(true);
        _out_packet.setCommand(AllCommands.NodeCluster.DataTransferCompletionNotice_VALUE);
        _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
        _out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
        _out_packet.setTo(packet.getFrom());
        _out_packet.setFrom(NodeHelper.getNodeCode());

        return _out_packet;
    }
}
