package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCGetTerminalInfos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class TerminalInfoCacheInit {

    public static Logger logger = LoggerFactory.getLogger(TerminalInfoCacheInit.class);

    @Autowired
    private LvsConfiguration lvsConfiguration;

    @Autowired
    private RPRmiService rpRmiService;// rmi调用通用类

    @Autowired
    private DA_1900_GetTerminalInfosRes da_1900_GetTerminalInfosRes;// 终端数据

    /**
     * 初始化缓存
     */
    @PostConstruct
    public void initTerminalInfoCache() {
        try {
            // 设置入参
            Packet packetIn = setPacketIn();
            Packet packet = rpRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
                    RmiConstant.RMI_INTERFACE_NAME, RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
                    packetIn);

            if (packet != null) {
                // 初始化缓存
                da_1900_GetTerminalInfosRes.processor(packet);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 设置入参
     *
     * @return
     */
    public Packet setPacketIn() {
        Packet packetIn = new Packet();
        packetIn.setCommand(LCAllCommands.AllCommands.DataAccess.GetTerminalInfos_VALUE);
        packetIn.setProtocol(LCConstant.LCMessageType.PLATFORM);
        //packetIn.setFrom(NodeHelper.getNodeCode());
        //packetIn.setUniqueMark(NodeHelper.getNodeUniqueMark());
        LCGetTerminalInfos.GetTerminalInfos.Builder builder = LCGetTerminalInfos.GetTerminalInfos.newBuilder();
        builder.setDistrictCode(LCDistrictCode.DistrictCode.valueOf(LCDistrictCode.DistrictCode.beijing_VALUE));
        builder.setNodeCode(0L);
        packetIn.setContent(builder.build().toByteArray());
        return packetIn;
    }
}
