package com.navinfo.opentsp.platform.da.core.persistence.mongodb;

import com.mongodb.*;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;


public class MongoManager {
    private static MongoClient mongo = null;
    private static ThreadLocal<DBCollection> collections = new ThreadLocal<DBCollection>();
    private static Logger logger = LoggerFactory.getLogger(BaseMongoDaoImpl.class);

    public static DB getDB(String dbName) throws UnknownHostException {
        return createDB(dbName);
    }

    public static DBCollection start(String dbName, String collectionName) throws UnknownHostException {
        DBCollection collection = getDB(dbName).getCollection(collectionName);
        collections.set(collection);
        return collection;
    }

    public static DBCollection get() {
        return collections.get();
    }

    public static void close() {
        DBCollection collection = collections.get();
        if (collection != null) {
            collections.remove();
        }
    }

    private static DB createDB(String dbName) throws UnknownHostException {
        // 连接数量
        int poolSize = new Integer(Configuration.getString("mongodb.master.poolSize"));
        // 等待队列长度
        int blockSize = new Integer(Configuration.getString("mongodb.master.blockSize"));
        if (mongo == null) {

            //MongoOptions opt = mongo.getMongoOptions();
            MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
                    .connectionsPerHost(poolSize)
                    .threadsAllowedToBlockForConnectionMultiplier(blockSize);

            //opt.connectionsPerHost = poolSize;
            //opt.threadsAllowedToBlockForConnectionMultiplier = blockSize;
            //mongo = new Mongo(new ServerAddress(Configuration.getString("mongodb.master.ip"),Configuration.getInt("mongodb.master.port")),opt);
            //mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
            //mongodb://kwiner:test123@127.0.0.1/test?authMechanism=MONGODB-CR&maxPoolSize=500"
            //mongo = new MongoClient(new ServerAddress(Configuration.getString("mongodb.master.ip"), Configuration.getInt("mongodb.master.port")), opt);
            mongo = new MongoClient(new MongoClientURI(Configuration.getString("mongodb.master.ip"), builder));

        }
        return mongo.getDB(dbName);
    }

    public static void main(String[] args) {
        //MongoOptions opt = mongo.getMongoOptions();
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder()
                .connectionsPerHost(1)
                .threadsAllowedToBlockForConnectionMultiplier(1);

        //opt.connectionsPerHost = poolSize;
        //opt.threadsAllowedToBlockForConnectionMultiplier = blockSize;
        //mongo = new Mongo(new ServerAddress(Configuration.getString("mongodb.master.ip"),Configuration.getInt("mongodb.master.port")),opt);
        //mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
        //mongodb://kwiner:test123@127.0.0.1/test?authMechanism=MONGODB-CR&maxPoolSize=500"
        //mongo = new MongoClient(new ServerAddress(Configuration.getString("mongodb.master.ip"), Configuration.getInt("mongodb.master.port")), opt);
        MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://192.168.85.38:27017", builder));
        System.out.println(mongo);
    }

}
