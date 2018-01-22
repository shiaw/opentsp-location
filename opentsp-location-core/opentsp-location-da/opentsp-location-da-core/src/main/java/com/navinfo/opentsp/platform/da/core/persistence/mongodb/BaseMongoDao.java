package com.navinfo.opentsp.platform.da.core.persistence.mongodb;

import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.BaseMongo;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.BaseMongoEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.FaultCodeDaAlarmEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.StagnationTimeoutAlarmEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACommonSummaryEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.FaultCodeEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.OvertimeParkAlarm;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.StagnationTimeoutEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.StaytimeParkAlarm;
import org.bson.types.ObjectId;

import com.mongodb.DBObject;

public interface BaseMongoDao {
	/**
	 * 存储
	 *
	 * @param dbObject
	 */
	abstract void save(BaseMongo baseMongoEntity);

	/**
	 * 批量存储
	 *
	 * @param dbObjects
	 */
	abstract void bacthSave(List<BaseMongoEntity> baseMongoEntitys);

	/**
	 * 据对象ID查询
	 *
	 * @param objectId
	 * @return
	 */
	abstract DBObject findByObjectId(ObjectId objectId);

	/**
	 * 据某个字段查询
	 *
	 * @param fieldName
	 * @param value
	 * @return
	 */
	abstract List<DBObject> findByField(String fieldName, Object value);

	abstract void uploadGridfs(String fileCode, byte[] content);

	abstract void saveForBatch(BaseMongoEntity baseMongoEntity);

	abstract void updateOverTimeEntity(BaseMongoEntity baseMongoEntity, List<OvertimeParkAlarm> list);

	abstract void updateStayTimeEntity(BaseMongoEntity baseMongoEntity, List<StaytimeParkAlarm> list);

	abstract void updateFaultCodeEntity(FaultCodeDaAlarmEntity entity, List<FaultCodeEntity> alarms);

	/**
	 * 查询指定条件的报警统计信息
	 *
	 * @param collectionName
	 * @param terminalId
	 * @param st
	 * @param et
	 * @return
	 */
	abstract public DACommonSummaryEntity getSummaryByCondition(String collectionName, long terminalId, long st, long et);

	/**
	 * 查询指定条件的报警数量
	 *
	 * @param collectionName
	 * @param terminalIds
	 * @param st
	 * @param et
	 * @return
	 */
	abstract public int getCountByCondition(String collectionName, long[] terminalIds, long st, long et);

	/**
	 * 查询结果集
	 *
	 * @param collectionName
	 * @param terminal_id
	 * @param st
	 * @param et
	 * @return
	 */
	abstract List<DBObject> queryByCondition(String collectionName, long terminal_id, long st, long et);
	abstract void updateStagnationTimeoutEntity(StagnationTimeoutAlarmEntity entity,List<StagnationTimeoutEntity> StagnationTimeoutAlarmList);
	/**
	 * 停滞超时撤销和恢复
	 * @param   _id  数据记录标识
	 * @param	isCancel	true：撤销；false：恢复
	 * @param	recordDate	停滞超时报警开始时间
	 */

	abstract void updateStagnationTimeoutCancelOrNot(String _id,
													 boolean isCancel, long recordDate);

}
