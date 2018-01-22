package com.navinfo.opentsp.platform.computing.parallel.application;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.navinfo.opentsp.platform.computing.parallel.util.MongoUtil;
import com.navinfo.opentsp.platform.computing.parallel.util.PropertiesUtil;
import com.navinfo.opentsp.platform.computing.parallel.util.RedisConstans;
import com.navinfo.opentsp.platform.computing.parallel.util.RedisUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.sql.*;
import java.util.*;

/**
 * Created by neo on 2017/11/1.
 *
 * @author neo
 */
public class SaveGpsDataApplicaton {

    private static Logger logger = LoggerFactory.getLogger(SaveGpsDataApplicaton.class);

    public static void main(String[] args) {
        String day = args[0];
//        String nums = args[1];
//        String remove = "false";
//
        Map<String,String> config = PropertiesUtil.getProperties();
        String driver = config.get("mysql.driverClassName");
        // URL指向要访问的数据库名game
        String url = config.get("mysql.url");
        // MySQL配置时的用户名
        String user = config.get("mysql.username");
        // MySQL配置时的密码
        String password = config.get("mysql.password");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("DriverException", e);
        }
        List<String> list = new ArrayList<>();
        try {
            try (
                    Connection conn = DriverManager.getConnection(url, user, password);
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT C.TERMINAL_ID FROM LC_TERMINAL_INFO C WHERE C.DATA_STATUS=1");
            ) {

                while (rs.next()) {
                    Long terminalId = rs.getLong("TERMINAL_ID");
                    list.add(String.valueOf(terminalId) + "_" + day);
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException", e);
        }
        System.out.println("amount size:"+list.size());
        logger.info("amount size:" + list.size());
        SparkConf sc = new SparkConf()
//                .setMaster("local") //spark master 地址 spark://10.30.50.148:7077
                .setAppName("SaveGpsDataApplicaton")
                //应用名称
//                .set("spark.mongodb.input.uri",
//                        PropertiesUtil.getProperties("spark.mongodb.input.uri"))
//                //mongodb input 连接
//                .set("spark.mongodb.output.uri",
//                        PropertiesUtil.getProperties("spark.mongodb.output.uri"))
                //mongodb output 连接
                .set("spark.mongodb.input.partitioner", "MongoPaginateBySizePartitioner")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        JavaSparkContext jsc = new JavaSparkContext(sc);
        Accumulator<Integer> accumulator = jsc.accumulator(0, "AccumulatorCounter");
        Accumulator<Integer> accumulatorF = jsc.accumulator(0, "AccumulatorFailure");

        Broadcast<Map<String,String>> configMap = jsc.broadcast(config);

        int fetchSize = Integer.parseInt(config.get("fetch.size"));
        int size = list.size() / fetchSize;
        if (list.size() % fetchSize != 0) {
            size = size + 1;
        }
        int from = 0;
        int to = 0;
        for (int i = 0; i < size; i++) {
            from = i * fetchSize;
            if (i == (size - 1)) {
                to = list.size();
            } else {
                to = (i + 1) * fetchSize;
            }
            List<String> subList = list.subList(from, to);

            JavaRDD<String> rdd = jsc.parallelize(subList);
            rdd.foreachPartition(new VoidFunction<Iterator<String>>() {
                @Override
                public void call(Iterator<String> iterator) throws Exception {
                    List<String> keyList = new ArrayList<>();
                    while (iterator.hasNext()) {
                        keyList.add(iterator.next());
                    }
                    List<Tuple2<String, Set<byte[]>>> tuple2List = new ArrayList<>();
                    try {
                        RedisUtil.buildInstance(configMap.getValue());
                        MongoUtil.buildInstance(configMap.getValue());

                        tuple2List = RedisUtil.getInstance().getPipeline(keyList);
                    } catch (Exception e) {
                        logger.error("Executor init failure", e);
                    }

                    List<Document> dtList = new ArrayList<>();
                    List<String> delList = new ArrayList<>();

                    Iterator<Tuple2<String, Set<byte[]>>> tuple2Iterator = tuple2List.iterator();
                    logger.info("executor IN:" + keyList.size() + "start");
                    while (tuple2Iterator.hasNext()) {

                        Tuple2<String, Set<byte[]>> tuple2 = tuple2Iterator.next();
                        String key = tuple2._1();
                        Set<byte[]> set = tuple2._2();
                        List<byte[]> result = new ArrayList<>();
                        result.addAll(set);
                        try {
                            if (null != result && result.size() > 0) {
                                //位置数据根据gpsDate从小到大排序
                                Collections.sort(result, new Comparator<byte[]>() {
                                    @Override
                                    public int compare(byte[] o1, byte[] o2) {
                                        //return o1.getGpsDate() > o2.getGpsDate()?1:-1;
                                        String s1 = "";
                                        String s2 = "";
                                        try {
                                            s1 = String.valueOf(LocationData.parseFrom(o1).getGpsDate());
                                            s2 = String.valueOf(LocationData.parseFrom(o2).getGpsDate());

                                        } catch (InvalidProtocolBufferException e) {
                                            logger.error("InvalidProtocolBuffer Exception", e);
                                        }
                                        return s1.compareTo(s2);
                                    }
                                });
                                ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();

                                for (byte[] locationData : result) {
                                    HashMap<String, Object> map = new HashMap<>(2);
                                    map.put("data", locationData);
                                    map.put("gpsTime", LocationData.parseFrom(locationData).getGpsDate());
                                    dataList.add(map);
                                }

                                Document document = new Document().
                                        append("tId", Long.valueOf(Long.valueOf(key.split("_")[0]))).
                                        append("day", day).
                                        append("dataList", dataList);
                                dtList.add(document);
                                delList.add(key);
                                accumulator.add(1);
                            }
                        } catch (Exception ex) {
                            accumulatorF.add(1);
                            logger.error("Location data execute failure:" + key, ex);
                        }
                    }
                    try {
                        if(dtList!=null&&dtList.size()>0){
                            MongoUtil instanceM = MongoUtil.getInstance();
                            MongoClient client = instanceM.getMongoClient();

                            String databaseName = instanceM.getDatabaseName();
                            String collectionName = "GpsDataEntity";
                            String yyMM = day.substring(2, 6);
                            MongoCollection<Document> collection = client.getDatabase(databaseName).getCollection(collectionName + "_" + yyMM);
                            collection.insertMany(dtList);
                        }

                        if(delList!=null&&delList.size()>0){
                            String[] strings = new String[delList.size()];
                            RedisUtil.getInstance().delKey(delList.toArray(strings));
                        }
                        logger.info("executor IN: " + keyList.size() + "end");
                        logger.info("mongodbList: " + dtList.size());
                        logger.info("delkeys: " + delList.size());
                    } catch (Exception e) {
                        accumulatorF.add(keyList.size());
                        logger.error("GPSData Tansfer from Redis to MongoDB Exception:", e);
                    }
                }
            });
        }
        logger.info("Accumulator Count:" + accumulator.value());
        logger.info("Accumulator Failure:" + accumulatorF.value());
        try {
            RedisUtil.buildInstance(config);
            RedisUtil.getInstance().delKey(RedisConstans.RedisKey.STATISTIC_STATUS_OVERTIMEPARKINAREA.name() + "_" + day);
        } catch (Exception e) {
            logger.error("RedisException STATISTIC_STATUS_OVERTIMEPARKINAREA", e);
        }
//        JavaRDD<Document> saveR = rdd.mapPartitions(new FlatMapFunctio;n<Iterator<String>, Document>() {
//            @Override
//            public Iterable<Document> call(Iterator<String> iterator) throws Exception {
//
//                List<String> keyList = new ArrayList<>();
//                while (iterator.hasNext()) {
//                    keyList.add(iterator.next());
//                }
//                try {
//                    RedisUtil.buildInstance(PropertiesUtil.getProperties());
//                } catch (Exception e) {
//                    logger.error("RedisException", e);
//                }
//                List<Tuple2<String, Set<byte[]>>> tuple2List = RedisUtil.getInstance().getPipeline(keyList);
//                List<Document> dtList = new ArrayList<>();
//
//                Iterator<Tuple2<String, Set<byte[]>>> tuple2Iterator = tuple2List.iterator();
//                logger.info("executor:" + keyList.size() + "start");
//                while (tuple2Iterator.hasNext()) {
//                    Tuple2<String, Set<byte[]>> tuple2 = tuple2Iterator.next();
//                    String key = tuple2._1();
//                    Set<byte[]> set = tuple2._2();
//                    List<byte[]> result = new ArrayList<>();
//                    result.addAll(set);
//                    try {
//
//                        if (null != result && result.size() > 0) {
//                            //位置数据根据gpsDate从小到大排序
//                            Collections.sort(result, new Comparator<byte[]>() {
//                                @Override
//                                public int compare(byte[] o1, byte[] o2) {
//                                    //return o1.getGpsDate() > o2.getGpsDate()?1:-1;
//                                    String s1 = "";
//                                    String s2 = "";
//                                    try {
//                                        s1 = String.valueOf(LocationData.parseFrom(o1).getGpsDate());
//                                        s2 = String.valueOf(LocationData.parseFrom(o2).getGpsDate());
//
//                                    } catch (InvalidProtocolBufferException e) {
//                                        logger.error("InvalidProtocolBuffer Exception", e);
//                                    }
//                                    return s1.compareTo(s2);
//                                }
//                            });
//                            ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
//
//                            for (byte[] locationData : result) {
//                                HashMap<String, Object> map = new HashMap<>(2);
//                                map.put("data", locationData);
//                                map.put("gpsTime", LocationData.parseFrom(locationData).getGpsDate());
//                                dataList.add(map);
//                            }
//
//                            Document document = new Document().
//                                    append("tId", Long.valueOf(Long.valueOf(key.split("_")[0]))).
//                                    append("day", day).
//                                    append("dataList", dataList);
//                            dtList.add(document);
//                            accumulator.add(1);
//                        }
//                    } catch (Exception ex) {
//                        accumulatorF.add(1);
//                        logger.error("Location data execute failure:" + key, ex);
//                    }
//                }
//                logger.info("executor:" + keyList.size() + "end");
//                return dtList;
//            }
//        });
//
//        Map<String, String> writeOverrides = new HashMap<String, String>(1);
//        writeOverrides.put("collection", "GpsDataEntity_"+day.substring(2, 6));
//
//        WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);
//        MongoSpark.save(saveR, writeConfig);
//        rdd.foreachPartition(new VoidFunction<Iterator<String>>() {
//            @Override
//            public void call(Iterator<String> iterator) throws Exception {
//                try {
//                    RedisUtil.buildInstance(PropertiesUtil.getProperties());
//                } catch (Exception e) {
//                    logger.error("RedisException", e);
//                }
//                List<String> keyList = new ArrayList<>();
//                while (iterator.hasNext()) {
//                    keyList.add(iterator.next());
//                }
//                String[] keys = new String[keyList.size()];
//                logger.info("delkeys:"+keys.length);
//                RedisUtil.getInstance().delKey(keyList.toArray(keys));
//            }
//        });

    }
}
