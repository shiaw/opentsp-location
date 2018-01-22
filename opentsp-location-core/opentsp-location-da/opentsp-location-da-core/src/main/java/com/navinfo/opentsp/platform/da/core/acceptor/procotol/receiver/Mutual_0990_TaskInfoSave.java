package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskInfo.TaskInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoSave;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.application.TaskInfoManagerImpl;

/**
 * 任务存储
 *
 * @author jin_s
 */
@DaRmiNo(id = "0990")
public class Mutual_0990_TaskInfoSave extends Dacommand {
    /**
     * 存储MM转存任务、统计任务的信息到DA的集中式Redis中
     */
    @Override
    public Packet processor(Packet packet) {
        TaskInfoManagerImpl service = new TaskInfoManagerImpl();
        try {
            LCTaskInfoSave.TaskInfoSave taskInfoBuf = LCTaskInfoSave.TaskInfoSave.parseFrom(packet.getContent());
            logger.info("存储MM转存任务、统计任务的信息到DA的集中式Redis中");
            TaskType type = taskInfoBuf.getTypes();
            List<TaskInfo> infosList = taskInfoBuf.getInfosList();
            for (TaskInfo taskInfo : infosList) {
                //节点编号
                long nodeCode = taskInfo.getNodeCode();
                //终端列表
                List<Long> terminalIdList = taskInfo.getTerminalIdList();
                long[] terminalIds = new long[terminalIdList.size()];
                for (int i = 0; i < terminalIdList.size(); i++) {
                    terminalIds[i] = terminalIdList.get(i);
                }
                service.saveTaskInfo(type.getNumber(), (int) nodeCode, terminalIds);
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
