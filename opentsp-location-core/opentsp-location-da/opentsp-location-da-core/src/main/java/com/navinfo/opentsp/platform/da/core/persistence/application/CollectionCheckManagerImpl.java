package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import com.navinfo.opentsp.platform.da.core.persistence.CollectionCheckManager;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.BaseMongoDaoImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 检查Collection是否存在，如果不存在就创建并建立索引
 * @author jin_s
 *
 */
public class CollectionCheckManagerImpl implements CollectionCheckManager {
	private Logger logger = LoggerFactory.getLogger(CollectionCheckManagerImpl.class);

	@Override
	public LCPlatformResponseResult.PlatformResponseResult checkCollection(String date) {
		logger.info("检查Collections是否存在，日期为："+date);
		if(date==null||date.length()==0){
			logger.error("检查Collections日期错误，返回失败。");
			return LCPlatformResponseResult.PlatformResponseResult.failure;
		}
		String mongth=date.substring(2,6);
		BaseMongoDaoImpl dao=new BaseMongoDaoImpl();
		for(String collectionName: Constant.COLLECTION_NAME)	{
			dao.exsitNotOrCreate(collectionName+"_"+mongth);
		}

		this.exsitNotOrCreateGpsData(LCMongo.DB.LC_GPS_LOCATION, LCMongo.Collection.LC_GPS_DATA_ENTITY,mongth, "tId","day");
//		this.exsitNotOrCreateGpsData(LCMongo.DB.LC_GPS_DRIVING_RECORDER, LCMongo.Collection.LC_RECORDER_ENTITY, mongth,"tId","saveDate");
//		this.exsitNotOrCreateGpsData(LCMongo.DB.LC_GPS_TRANSFER, LCMongo.Collection.LC_TRANSFER_ENTITY, mongth,"tId","_id");
//		this.exsitNotOrCreateGpsData(LCMongo.DB.LC_GPS_MULTIMEDIA, LCMongo.Collection.LC_MULTIMEDIA_ENTITY, "","fId");
//		this.exsitNotOrCreateGpsData(LCMongo.DB.LC_GPS_CANDATA, LCMongo.Collection.LC_CANDATA, "","tId","time");
//		this.exsitNotOrCreateGpsData(LCMongo.DB.LC_GPS_CANDATA, LCMongo.Collection.LC_CANDATA_REPORT, mongth,"tId","day");
		return LCPlatformResponseResult.PlatformResponseResult.success;
	}
	/**
	 * Collection检测
	 * @param collectionName
	 */
	public void exsitNotOrCreateGpsData(String DBName,String collectionName,String mongth,String ...index){
	    try {
			//获取数据库
			DB db= MongoManager.getDB(DBName);
			//判断是否存在集合collectionName
			boolean isExist=db.collectionExists(collectionName+mongth);
			if(!isExist){
				DBCollection collection=db.getCollection(collectionName+mongth);
				DBObject keys = new BasicDBObject();
				for(String indexName:index){
					// 索引列表
			    	keys.put(indexName, 1);
				}
			   //创建索引
			   collection.createIndex(keys);
			}else{
				DBCollection collection = db.getCollection(collectionName);
				List<DBObject> obj = collection.getIndexInfo();
				boolean status = false;
				if(obj.size()==1){
					status = true;
				}
				for(DBObject dbo : obj){
					
					String name = (String) dbo.get("name");
					if(name.indexOf("_id_") != -1){
						continue;
					}
					if(name.indexOf("tId_1_day_1") == -1){
						status = true;
						collection.dropIndexes();
					}else{continue;}
				}
				if(status){
					DBObject keys = new BasicDBObject();
					keys.put("tId", 1);
					keys.put("day", 1);
					collection.createIndex(keys, new BasicDBObject("background",
							true));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
}
