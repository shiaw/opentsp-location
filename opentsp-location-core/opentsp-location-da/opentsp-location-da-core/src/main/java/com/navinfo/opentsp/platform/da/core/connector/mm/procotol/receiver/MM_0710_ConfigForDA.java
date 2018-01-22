package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.receiver;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCConfigForDA.ConfigForDA;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCNodeStatusReport.NodeStatusReport.NodeStatus;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCParameterConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCParameterConfig.ParameterConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCParameterConfig.ParameterConfig.ParameterType;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

public class MM_0710_ConfigForDA extends MMCommand {

    @Override
    public int processor(Packet packet) {
        try {
            ConfigForDA configForDA = ConfigForDA
                    .parseFrom(packet.getContent());
            super.commonResponses(packet.getFrom(), packet.getSerialNumber(),
                    packet.getCommand(), PlatformResponseResult.success);
            // 加载配置信息
            this.loadConfig(configForDA);
            if (NodeHelper.nodeStatus.getNumber() == NodeStatus.working_VALUE) {
                return 1;
            }
            NodeHelper.nodeStatus = NodeStatus.working;

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void loadConfig(ConfigForDA configForDA) {
        List<LCParameterConfig.ParameterConfig> list = configForDA
                .getParasList();
        for (ParameterConfig poc : list) {
            ParameterType parameterType = poc.getTypes();
            Configuration.addConfig(parameterType.name(), poc.getValues());
        }
    }

}
