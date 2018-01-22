package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.send;

import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCReportServerIdentify.ReportServerIdentify;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;

public class MM_0602_ReportServerIdentify extends MMCommand {
    private final static NodeType NODE_TYPE = NodeType.da;

    @Override
    public int processor(Packet packet) {
        ReportServerIdentify.Builder builder = ReportServerIdentify.newBuilder();
        builder.setType(NODE_TYPE);
        builder.setNodeCode(NodeHelper.getNodeCode());

        packet.setCommand(AllCommands.NodeCluster.ReportServerIdentify_VALUE);
        packet.setProtocol(LCMessageType.PLATFORM);
        packet.setContent(builder.build().toByteArray());
        packet.setFrom(NodeHelper.getNodeCode());
        packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
        return super.broadcast(packet);
    }

}
