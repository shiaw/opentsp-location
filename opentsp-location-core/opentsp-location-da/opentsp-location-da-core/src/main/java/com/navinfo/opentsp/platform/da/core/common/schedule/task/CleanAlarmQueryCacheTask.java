package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;

import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import  com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDataQueryKeyOverdue;
import  com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;

import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.QueryKeyEntity;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.MutualCommandFacotry;
import  com.navinfo.opentsp.platform.da.core.acceptor.session.MutualSessionManage;
import  com.navinfo.opentsp.platform.da.core.cache.alarm.AlarmQueryManager;
import  com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import  com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import  com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DASQueryKeyServiceImp;


/******************
 * 清理动态请求缓存
 *
 * @author claus
 *
 */
public class CleanAlarmQueryCacheTask extends TimerTask implements ITask {

	private Logger log = LoggerFactory.getLogger(CleanAlarmQueryCacheTask.class);

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
	/**
	 * 缓存有效存储时间 十分钟
	 */
	public static int DURATION_TIME=600;

	DASQueryKeyServiceImp service=new DASQueryKeyServiceImp();
	/**
	 *缓存过期列表
	 */
    private List <String> queryKeys=new ArrayList <String>();
	private DASQueryKeyServiceImp dsaDasQueryKeyServiceImp = new DASQueryKeyServiceImp();
    /**
     * mm节点编号列表
     */
    List<Long> nodeCodes=null;
	@Override
	public void run() {
		try {
			log.info("执行定时清理分页缓存任务。");
			//redis中的统计查询key
			Map<String, Object> findQueryKeys = service.findQueryKeys();
			if(findQueryKeys!=null&&!findQueryKeys.isEmpty()){

				Set<Entry<String, Object>> entrySet = findQueryKeys.entrySet();
				for(Entry entry:entrySet){
					QueryKeyEntity value = (QueryKeyEntity) entry.getValue();


						//超过了系统允许缓存时间,清楚缓存中的数据记录，只保留缓存类对象.
						if(DURATION_TIME < (System.currentTimeMillis() /1000- value.getLastUpdateTime())){
							queryKeys.add((String) entry.getKey());
						}

				}
				if(queryKeys.size()>0){
					sendDataQueryKeyOverdue(queryKeys);
					for(String key: queryKeys){
						AlarmQueryManager.getInstance().remove(key);
						dsaDasQueryKeyServiceImp.removeQueryKey(key);
					}

					queryKeys.clear();
				}
			}


		} catch (Exception e) {
			log.error("清理报表查询缓存错误.", e.getMessage());
			e.printStackTrace();
		}
	}
/**
 * 向MM数据检索Key过期通知
 * @param queryKeysLocal
 */
	private void sendDataQueryKeyOverdue(List queryKeysLocal) {
		if(null==nodeCodes){
	       this.nodeCodes = MutualSessionManage.getInstance().getNodeListByNodeType(NodeType.mm);
		}
		log.info("给MM发送消息播报，执行清理分页缓存");
		if(nodeCodes!=null){
		LCDataQueryKeyOverdue.DataQueryKeyOverdue.Builder builer=LCDataQueryKeyOverdue.DataQueryKeyOverdue.newBuilder();
		builer.addAllKey(queryKeysLocal);
		for (Long nodeCode : nodeCodes) {
			Packet pk = new Packet(true);
			pk.setProtocol(LCConstant.LCMessageType.PLATFORM);
			pk.setCommand(AllCommands.DataAccess.DataQueryKeyOverdue_VALUE);
 		    pk.setContent(builer.build().toByteArray());
			pk.setUniqueMark(NodeHelper.getNodeUniqueMark());
			pk.setTo(nodeCode);
			MutualCommandFacotry.processor(pk);
		}
		}
	}

}
