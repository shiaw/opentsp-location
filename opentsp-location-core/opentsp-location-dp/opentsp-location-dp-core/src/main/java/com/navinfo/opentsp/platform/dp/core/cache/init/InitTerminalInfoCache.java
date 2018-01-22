package com.navinfo.opentsp.platform.dp.core.cache.init;

import com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver.DA_0900_GetTerminalInfosRes;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCGetTerminalInfos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 去掉DP维护终端cache逻辑,统一使用push维护redis结果
 * Created by yinsihua on 2016/9/22.
 */
//@Component
@Deprecated
public class InitTerminalInfoCache
{
    @Autowired
    private DaRmiService daRmiService;// rmi调用通用类

    @Autowired
    private DA_0900_GetTerminalInfosRes da_1900_GetTerminalInfosRes;// 终端数据

    @Value("${district.code}")
    public int districtCode;

    public static Logger logger = LoggerFactory.getLogger(InitTerminalInfoCache.class);
    /**
     * 初始化缓存
     */
    public void initCache()
    {
        try
        {
            // 设置入参
            Packet packetIn = setPacketIn();

            Packet packet =
                    daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
                            RmiConstant.RMI_INTERFACE_NAME,
                            RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
                            packetIn);

            if (packet != null)
            {
                // 初始化缓存
                da_1900_GetTerminalInfosRes.processor(packet);

            } else {
                logger.info("获取RMI对象失败！");
            }
        }
        catch (Exception e)
        {
            logger.error("初始化加载终端信息缓存失败" + e.getMessage());
        }

    }

    /**
     * 设置入参
     *
     * @return
     */
    public Packet setPacketIn()
    {
        Packet packetIn = new Packet();
        packetIn.setCommand(LCAllCommands.AllCommands.DataAccess.GetTerminalInfos_VALUE);
        packetIn.setProtocol(LCConstant.LCMessageType.PLATFORM);
//        packetIn.setFrom(NodeHelper.getNodeCode());
//        packetIn.setUniqueMark(NodeHelper.getNodeUniqueMark());

        LCGetTerminalInfos.GetTerminalInfos.Builder builder = LCGetTerminalInfos.GetTerminalInfos.newBuilder();
        builder.setDistrictCode(LCDistrictCode.DistrictCode.valueOf(districtCode));
        builder.setNodeCode(0L);

        packetIn.setContent(builder.build().toByteArray());

        return packetIn;
    }
}
