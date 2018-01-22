package com.navinfo.tasktracker.nilocation.util;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
/**
 * @author
 */
@Service
public class MongoManager {
    private static MongoClient mongo = null;
    private static ThreadLocal<DBCollection> collections = new ThreadLocal<DBCollection>();
    @Value("${mongodb.master.ip}")
    private String mongodbIp;

    @Value("${mongodb.master.poolSize}")
    private String mongodbPoolSize;

    @Value("${mongodb.master.port}")
    private  String mongodbPort;

    @Value("${mongodb.master.blockSize}")
    private  String mongodbBlockSize;

    public  DB getDB(String dbName) throws UnknownHostException {
        return createDB(dbName);
    }

    public  DBCollection start(String dbName, String collectionName) throws UnknownHostException {
        DBCollection collection = getDB(dbName).getCollection(collectionName);
        collections.set(collection);
        return collection;
    }

    public  DBCollection get() {
        return collections.get();
    }

    public static void close() {
        DBCollection collection = collections.get();
        if (collection != null) {
            collections.remove();
        }
    }

    private  DB createDB(String dbName) throws UnknownHostException {
        // 连接数量
        int poolSize = new Integer(mongodbPoolSize);
        // 等待队列长度
        int blockSize = new Integer(mongodbBlockSize);
        if (mongo == null) {

            MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
                    .connectionsPerHost(poolSize)
                    .threadsAllowedToBlockForConnectionMultiplier(blockSize);

            mongo = new MongoClient(new MongoClientURI(mongodbIp, builder));

        }
        return mongo.getDB(dbName);
    }

}
