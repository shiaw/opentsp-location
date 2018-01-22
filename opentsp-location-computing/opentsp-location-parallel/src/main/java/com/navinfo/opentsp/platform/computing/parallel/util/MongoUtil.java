package com.navinfo.opentsp.platform.computing.parallel.util;

import com.mongodb.*;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Neo
 * @date 2017-04-24
 * @modify
 */

public class MongoUtil implements Serializable {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(MongoUtil.class);
    /**
     * Mongodb实例
     */
    private static volatile MongoUtil instance = null;

    private MongoClient mongoClient;

    private String databaseName;

    private String collectionName;

    private MongoUtil() {
    }

    /**
     * @return MongoDBUtil实例
     */
    public static MongoUtil getInstance() {
        if (instance == null) {
            throw new RuntimeException("MongoDB Do not initialize!!!");
        }
        return instance;
    }


    /**
     * 实例化MongoDBUtil对象
     *
     * @param conf 配置参数
     */
    public static void buildInstance(Map conf) throws Exception {
        if (instance == null) {
            synchronized (MongoUtil.class) {
                if (instance == null) {
                    MongoUtil local = new MongoUtil();
                    local.init(conf);
                    instance = local;
                }
            }
        }
    }

    /**
     * 测试入口
     *
     * @param args
     */
    public static void main(String[] args) {


        String str = "{\"terminalId\":14807393149,\"date\":\"2017-05-05\",\"data\":[{\"gpstime\":1498461700,\"lat\":117.440659,\"lon\":117.440659},{\"gpstime\":1498461700,\"lat\":117.440659,\"lon\":117.440659}]}";
        HashMap<String, String> map = new HashMap<>(5);
        map.put("mongo.url", "10.30.50.147:27778,10.30.50.147:27779,10.30.50.148:27778,10.30.50.148:27779,10.30.50.149:27778,10.30.50.149:27779");
        map.put("mongo.connectionsPerHost", "10");
        map.put("mongo.database", "test");
        map.put("mongo.collection", "location");
        try {
            MongoUtil.buildInstance(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document dbObject = Document.parse(str);

        String date = dbObject.getString("date");
        if (StringUtils.isNotEmpty(date)) {
            MongoUtil instance = MongoUtil.getInstance();
            String databaseName = instance.getDatabaseName();
            String collectionName = instance.getCollectionName();
            MongoCollection<Document> collection = instance.getMongoClient().getDatabase(databaseName).getCollection(collectionName);

            ArrayList<Long> longs = new ArrayList<>();
            longs.add(14807393149L);
            longs.add(14807393147L);
            longs.add(14807393146L);
            longs.add(14807393148L);

            List<WriteModel<Document>> requests = new ArrayList<WriteModel<Document>>();
            for (Long aLong : longs) {
                //更新条件
                Bson tidfilter = Filters.eq("terminalId", aLong);
                Bson dateFilter = Filters.eq("date", date);
                Bson and = Filters.and(tidfilter, dateFilter);
                //更新内容
                Document updateDocument = new Document("$push", Document.parse("{'data': {\"gpstime\":1498461700,\"lat\":117.440659,\"lon\":117.440659}}"));
                //构造更新单个文档的操作模型
                UpdateOneModel<Document> uom = new UpdateOneModel<Document>(and, updateDocument, new UpdateOptions().upsert(true));
                //UpdateOptions代表批量更新操作未匹配到查询条件时的动作，默认false，什么都不干，true时表示将一个新的Document插入数据库，他是查询部分和更新部分的结合
                requests.add(uom);
            }

            BulkWriteResult bulkWriteResult = collection.bulkWrite(requests);
            System.out.println(bulkWriteResult.toString());
        }

    }

    public MongoClient getMongoClient() {
        List<MongoCredential> list = mongoClient.getCredentialsList();
        for (MongoCredential m :list){
            logger.info("getUserName:"+m.getUserName());
            logger.info("getPassword:"+String.valueOf(m.getPassword()));
            logger.info("getSource:"+m.getSource());
            logger.info("getAddressHost:"+mongoClient.getAddress().getHost());
            logger.info("getAddressPort:"+mongoClient.getAddress().getPort());
        }

        for (ServerAddress s : mongoClient.getAllAddress()){
            logger.info("getAllAddressHost:"+s.getHost());
            logger.info("getAllAddressPort:"+s.getPort());
        }

        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    private void init(Map conf) {
        logger.info("开始初始化MongoDB连接池");
        String host = (String) conf.get("mongo.url");
        int connectionsPerHost = Integer.parseInt((String) conf.get("mongo.connectionsPerHost"));
        databaseName = (String) conf.get("mongo.database");
        collectionName = (String) conf.get("mongo.collection");

        String username = (String) conf.get("mongo.username");
        String password = (String) conf.get("mongo.password");
        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            //创建个 credential对象
            MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());
            mongoCredentialList.add(credential);
        }

        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        options.connectionsPerHost(connectionsPerHost);
        // 连接池设置为300个连接,默认为100
        options.connectTimeout(15000);
        // 连接超时，推荐>3000毫秒
        options.maxWaitTime(5000);
        options.socketTimeout(0);
        // 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(5000);
        // 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。

        List<ServerAddress> seeds = new ArrayList<>();
        String[] split = host.split(",");
        for (int i = 0; i < split.length; i++) {
            String[] url = split[i].split(":");
            ServerAddress serverAddress = new ServerAddress(url[0], Integer.parseInt(url[1]));
            seeds.add(serverAddress);
        }

        if (mongoCredentialList.size() != 0) {
            mongoClient = new MongoClient(seeds, mongoCredentialList, options.build());
        } else {
            mongoClient = new MongoClient(seeds, options.build());
        }
        logger.info("成功初始化MongoDB连接池");
    }

    /**
     * 获取DB实例 - 指定DB
     *
     * @param dbName
     * @return
     */
    public MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }

    /**
     * 获取collection对象 - 指定Collection
     *
     * @param collName
     * @return
     */
    public MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

    /**
     * 获取collection对象
     *
     * @return
     */
    public MongoCollection<Document> getCollection() {
        MongoCollection<Document> collection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
        return collection;
    }

    /**
     * 插入json格式数据
     *
     * @param json
     */
    public void insertJson(String json) {
        Document dbObject = Document.parse(json);
        MongoCollection<Document> collection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
        collection.insertOne(dbObject);
    }

    /**
     * 查询DB下的所有表名
     */
    public List<String> getAllCollections(String dbName) {
        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
        List<String> list = new ArrayList<String>();
        for (String s : colls) {
            list.add(s);
        }
        return list;
    }

    /**
     * 获取所有数据库名称列表
     *
     * @return
     */
    public MongoIterable<String> getAllDBNames() {
        MongoIterable<String> s = mongoClient.listDatabaseNames();
        return s;
    }

    /**
     * 查找对象 - 根据主键_id
     *
     * @param
     * @param id
     * @return
     */
    public Document findById(MongoCollection<Document> coll, String id) {
        ObjectId idobj = null;
        try {
            idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Document myDoc = coll.find(Filters.eq("_id", idobj)).first();
        return myDoc;
    }

    /**
     * 统计数
     */
    public int getCount(MongoCollection<Document> coll) {
        int count = (int) coll.count();
        return count;
    }

    /**
     * 条件查询
     */
    public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    /**
     * 分页查询
     */
    public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
        Bson orderBy = new BasicDBObject("_id", 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }

    /**
     * 通过ID删除
     *
     * @param coll
     * @param id
     * @return
     */
    public int deleteById(MongoCollection<Document> coll, String id) {
        int count = 0;
        ObjectId id1 = null;
        try {
            id1 = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", id1);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    /**
     * FIXME
     *
     * @param coll
     * @param id
     * @param newdoc
     * @return
     */
    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId idobj = null;
        try {
            idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", idobj);
        // coll.replaceOne(filter, newdoc); // 完全替代
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }

    public void dropCollection(String dbName, String collName) {
        getDB(dbName).getCollection(collName).drop();
    }

    /**
     * 关闭Mongodb
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
