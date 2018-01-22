package com.navinfo.opentsp.platform.da.core.persistence.mongodb;

import java.util.List;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.bson.types.ObjectId;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public interface MongoDao {
	/**
	 * 存储
	 *
	 * @param dbObject
	 */
	abstract boolean save(DBObject dbObject);
	/**
	 * 批量存储
	 * @param dbObjects
	 */
	abstract boolean bacthSave(List<DBObject> dbObjects);

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

	abstract void uploadGridfs(String fileCode , byte[] content);
	/**
	 * 根据mongo主键删除某个对象
	 * @param id
	 * @return
	 */
	abstract PlatformResponseResult delObjectById(DBCollection collection, String id);
}
