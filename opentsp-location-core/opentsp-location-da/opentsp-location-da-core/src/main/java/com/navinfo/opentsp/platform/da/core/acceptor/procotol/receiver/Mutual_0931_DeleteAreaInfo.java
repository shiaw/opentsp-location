package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.navinfo.opentsp.platform.da.core.persistence.RegularDataAreaAndDataManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.RegularDataAreaAndDataManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDeleteAreaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@DaRmiNo(id = "0931")
public class Mutual_0931_DeleteAreaInfo extends Dacommand {
    final static RegularDataAreaAndDataManage manager = new RegularDataAreaAndDataManageImpl();
    private static final Logger logger = LoggerFactory.getLogger(Mutual_0931_DeleteAreaInfo.class);

    @Override
    public Packet processor(Packet packet) {
        logger.error("******Mutual_0931_DeleteAreaInfo**********************");
        try {
            LCDeleteAreaInfo.DeleteAreaInfo da = LCDeleteAreaInfo.DeleteAreaInfo.parseFrom(packet.getContent());
            long terminalId = da.getTerminalId();
            List<Long> areaIds = da.getAreaIdentifyList();
            int[] originalAreaId = new int[areaIds.size()];
            for (int i = 0; i < areaIds.size(); i++) {
                originalAreaId[i] = new Long(areaIds.get(i)).intValue();
            }
            manager.deleteAreaInfo(terminalId, originalAreaId);
            return commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.failure);
        }
    }

}
