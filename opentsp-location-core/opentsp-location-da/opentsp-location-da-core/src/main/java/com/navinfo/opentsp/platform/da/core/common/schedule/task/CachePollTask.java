package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDataQueryKeyOverdue.DataQueryKeyOverdue;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.QueryKeyEntity;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.MutualCommandFacotry;
import com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DASQueryKeyServiceImp;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 定时检查redis中统计数据查询缓存的key是否过期，如有过期的key通知mm广播给DA和DSA进行删除
 *
 * @author jin_s
 */
public class CachePollTask extends TimerTask implements ITask {
    DASQueryKeyServiceImp service = new DASQueryKeyServiceImp();

    @Override
    public void setExecuteCycle(long cycle) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getLastExecuteTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TaskStatus getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void run() {

        long currentTime = System.currentTimeMillis() / 1000;
        Map<String, Object> queryKeysMap = service.findQueryKeys();
        if (null != queryKeysMap && !queryKeysMap.isEmpty()) {
            DataQueryKeyOverdue.Builder builder = DataQueryKeyOverdue.newBuilder();
            Collection<Object> values = queryKeysMap.values();
            Iterator<Object> iterator = values.iterator();
            while (iterator.hasNext()) {
                QueryKeyEntity bean = (QueryKeyEntity) iterator.next();
                if (currentTime - bean.lastUpdateTime > 300) {
                    builder.addKey(bean.getQueryKey());
                }
            }
            List<Long> nodeCodes = MutualSessionManage.getInstance().getNodeListByNodeType(NodeType.mm);
            for (Long nodeCode : nodeCodes) {
                Packet pk = new Packet(true);
                pk.setProtocol(LCMessageType.PLATFORM);
                pk.setCommand(AllCommands.DataAccess.DataQueryKeyOverdue_VALUE);
                pk.setContent(builder.build().toByteArray());
                pk.setUniqueMark(NodeHelper.getNodeUniqueMark());
                pk.setTo(nodeCode);
                MutualCommandFacotry.processor(pk);
            }

        }
    }

}
