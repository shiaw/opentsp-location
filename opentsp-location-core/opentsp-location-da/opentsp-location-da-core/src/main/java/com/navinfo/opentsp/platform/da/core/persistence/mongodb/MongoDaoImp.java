package com.navinfo.opentsp.platform.da.core.persistence.mongodb;

import java.util.List;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoDaoImp implements MongoDao {
	private Logger logger = LoggerFactory.getLogger(MongoDaoImp.class);

	@Override
	public boolean save(DBObject dbObject) {
		DBCollection collection = MongoManager.get();
		try {
			collection.save(dbObject);
			return true;
		} catch (Exception e) {
			logger.error("存储出错");
			return false;
		}
	}

	@Override
	public boolean bacthSave(List<DBObject> dbObjects) {
		DBCollection collection = MongoManager.get();
		try {
			collection.insert(dbObjects);
			return true;
		} catch (Exception e) {
			logger.error("存储出错："+dbObjects.toString());
			return false;
		}
	}

	@Override
	public DBObject findByObjectId(ObjectId objectId) {
		DBObject dbObject = new BasicDBObject("_id", objectId);
		DBCollection collection = MongoManager.get();
		return this.wrap(collection.find(dbObject));
	}

	@Override
	public List<DBObject> findByField(String fieldName, Object value) {
		DBObject dbObject = new BasicDBObject(fieldName, value);
		DBCollection collection = MongoManager.get();
		return this.wraps(collection.find(dbObject));
	}

	protected DBObject wrap(DBCursor cursor) {
		if (cursor.hasNext())
			return (DBObject) cursor.next();
		return null;
	}

	protected List<DBObject> wraps(DBCursor cursor) {
		if (cursor.hasNext()) {
			return cursor.toArray();
		}
		return null;
	}

	@Override
	public void uploadGridfs(String fileCode, byte[] content) {
		GridFS myFS = new GridFS(null, "");
		GridFSInputFile gridFSInputFile = myFS.createFile(content);
		gridFSInputFile.setFilename(fileCode);
		gridFSInputFile.save();
	}



	@Override
	public PlatformResponseResult delObjectById(DBCollection collection, String id) {
		DBObject dbObject = new BasicDBObject("_id", new ObjectId(id));
		WriteResult result = collection.remove(dbObject,new WriteConcern(1));
		int n = result.getN();
		if(1 == n){
//			logger.error("删除MongoDB对象成功："+collection.getFullName()+"--->id:"+id);
			return PlatformResponseResult.success;
		}else {
			logger.error("删除MongoDB对象时出错："+collection.getFullName()+"--->id:"+id);
			return PlatformResponseResult.failure;
		}
	}
}
