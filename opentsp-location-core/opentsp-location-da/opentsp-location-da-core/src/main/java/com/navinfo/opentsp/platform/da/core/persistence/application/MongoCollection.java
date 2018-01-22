package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.BaseMongoDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;

import java.util.List;

/**
 * @author Lenovo
 * @date 2016-12-13
 * @modify
 * @copyright
 */
public class MongoCollection {
    public static void main(String[] args) {
        String date = "20160407";
        String mongth=date.substring(2,6);
        BaseMongoDaoImpl dao=new BaseMongoDaoImpl();
        for(String collectionName: Constant.COLLECTION_NAME)	{
            dao.exsitNotOrCreate(collectionName+"_"+mongth);
        }
        exsitNotOrCreateGpsData(LCMongo.DB.LC_GPS_LOCATION, LCMongo.Collection.LC_GPS_DATA_ENTITY,mongth, "tId","day");
    }

    public static void exsitNotOrCreateGpsData(String DBName,String collectionName,String mongth,String ...index){
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
                DBCollection collection = db.getCollection(collectionName+mongth);
                List<DBObject> obj = collection.getIndexInfo();
                boolean status = false;
                if(obj.size()==1){
                    status = true;
                }
                for(DBObject dbo : obj){
                    String name = (String)dbo.get("name");
                    if(name.indexOf("tId_1__id_1") != -1){
                        status = true;
                        collection.dropIndexes();
                    }
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
            e.getMessage();
        }
    }
}
