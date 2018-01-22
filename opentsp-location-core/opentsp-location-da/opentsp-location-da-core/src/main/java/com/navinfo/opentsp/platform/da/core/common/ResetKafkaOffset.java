package com.navinfo.opentsp.platform.da.core.common;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author wangyd
 * @date 2017-07-14
 */
public class ResetKafkaOffset {


    public static void main(String[] args) {
        resetKafkaOffset1();
    }

    public static void resetKafkaOffset1() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "sy-lp1:9092,sy-lp2:9092,sy-lp3:9092");
//        props.put("bootstrap.servers", "10.30.10.11:19092,10.30.10.12:19092,10.30.10.13:19092");
        // kafka server;10.30.10.11:19092,10.30.10.12:19092,10.30.10.13:19092;10.30.10.11:9092,10.30.10.12:9092 ,10.30.10.13:9092
//        props.put("group.id", "opentsp-location-dpcjc");     // 组名称ni-tripintegration-ws-0720#dayun_transfer_02
//        props.put("group.id", "ni-tripintegration-ws-0804-1");     // 青汽ni-tripintegration-ws-0804-1
//        props.put("group.id", "ni-loaclevent-yl-0918");     // vi数据groupid
//        props.put("group.id", "ni-localmessage-core-20170925");     // vi数据groupid
//        props.put("group.id", " opentsp-location-dpcjc");     // 位置云DP opentsp-location-dpcjc
//          props.put("group.id", "groupda0"); //位置云DA
//          props.put("group.id", "ni_rule"); //红岩DP
//          props.put("group.id", "qingqi_data_test"); //红岩DP
        props.put("group.id", "testda"); //红岩jstorm
//        props.put("group.id", "local_test_0101"); //红岩jstorm
//        props.put("group.id","qingqi_data_test");

        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "90000");
        props.put("request.timeout.ms", "100000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "com.navinfo.opentspcore.common.kafka.serializers.KafkaJsonDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

//        String topic = "posraw";    // topic名称NiV1LocationData##dayun_transfer###jfz_ZHposcan_pkt
//        String topic = "jfz_ZHposcan_pkt"; //青汽
//        String topic = "concentratedreport";//位置云
//          String topic = "topic_batchtest";//红岩DP
          String topic = "daposdonenew";//红岩jstorm
//        String topic = "jfz_ZHposcan_pkt";
//        String topic = "jfz_ZHposcan_pkt";//红岩jstorm
//        String topic = "jfz_ZHposcan_pkt";//红岩DP
//        String topic = "NiV1LocationData1"; //v1数据
//        String topic = "localevent-status-stresstest0925";
        List<TopicPartition> list = new ArrayList<>();
        for (int i = 0; i < 18; i++) {   // 分区总数
            TopicPartition partition = new TopicPartition(topic, i);
            list.add(partition);
            System.out.println(list.get(i));
        }
        consumer.assign(list);

        for (TopicPartition partition: list){
            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(0)));     //偏移量,批量修改offset
        }

//ni项目偏移量修改 topic：NiV1LocationData    groupid：ni-tripintegration-ws-0720
//      //大运TA：  topic：dayun_transfer    groupid：dayun_transfer_02
//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(178134)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(86615)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(1289877)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(20943)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(103336)));
//        TopicPartition partition5=new TopicPartition(topic,5);
//        consumer.commitSync(Collections.singletonMap(partition5, new OffsetAndMetadata(41128)));
//        TopicPartition partition6=new TopicPartition(topic,6);
//        consumer.commitSync(Collections.singletonMap(partition6, new OffsetAndMetadata(4991226)));
//        TopicPartition partition7=new TopicPartition(topic,7);
//        consumer.commitSync(Collections.singletonMap(partition7, new OffsetAndMetadata(1873280)));
//        TopicPartition partition8=new TopicPartition(topic,8);
//        consumer.commitSync(Collections.singletonMap(partition8, new OffsetAndMetadata(103820)));
//        TopicPartition partition9=new TopicPartition(topic,9);
//        consumer.commitSync(Collections.singletonMap(partition9, new OffsetAndMetadata(163476)));
//        TopicPartition partition10=new TopicPartition(topic,10);
//        consumer.commitSync(Collections.singletonMap(partition10, new OffsetAndMetadata(151775)));
//        TopicPartition partition11=new TopicPartition(topic,11);
//        consumer.commitSync(Collections.singletonMap(partition11, new OffsetAndMetadata(55841)));
//        TopicPartition partition12=new TopicPartition(topic,12);
//        consumer.commitSync(Collections.singletonMap(partition12, new OffsetAndMetadata(7684654)));
//        TopicPartition partition13=new TopicPartition(topic,13);
//        consumer.commitSync(Collections.singletonMap(partition13, new OffsetAndMetadata(125160)));
//        TopicPartition partition14=new TopicPartition(topic,14);
//        consumer.commitSync(Collections.singletonMap(partition14, new OffsetAndMetadata(427468)));
//        TopicPartition partition15=new TopicPartition(topic,15);
//        consumer.commitSync(Collections.singletonMap(partition15, new OffsetAndMetadata(217177)));
//        TopicPartition partition16=new TopicPartition(topic,16);
//        consumer.commitSync(Collections.singletonMap(partition16, new OffsetAndMetadata(178013)));
//        TopicPartition partition17=new TopicPartition(topic,17);
//        consumer.commitSync(Collections.singletonMap(partition17, new OffsetAndMetadata(33755)));

//        //v1数据线上数据
//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(5898986)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(6767472)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(6284849)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(4994310)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(5586089)));
//        TopicPartition partition5=new TopicPartition(topic,5);
//        consumer.commitSync(Collections.singletonMap(partition5, new OffsetAndMetadata(5768406)));
//        TopicPartition partition6=new TopicPartition(topic,6);
//        consumer.commitSync(Collections.singletonMap(partition6, new OffsetAndMetadata(6615670)));
//        TopicPartition partition7=new TopicPartition(topic,7);
//        consumer.commitSync(Collections.singletonMap(partition7, new OffsetAndMetadata(9382463)));
//        TopicPartition partition8=new TopicPartition(topic,8);
//        consumer.commitSync(Collections.singletonMap(partition8, new OffsetAndMetadata(5445860)));
//        TopicPartition partition9=new TopicPartition(topic,9);
//        consumer.commitSync(Collections.singletonMap(partition9, new OffsetAndMetadata(6556853)));
//        TopicPartition partition10=new TopicPartition(topic,10);
//        consumer.commitSync(Collections.singletonMap(partition10, new OffsetAndMetadata(4988058)));
//        TopicPartition partition11=new TopicPartition(topic,11);
//        consumer.commitSync(Collections.singletonMap(partition11, new OffsetAndMetadata(5023009)));
//        TopicPartition partition12=new TopicPartition(topic,12);
//        consumer.commitSync(Collections.singletonMap(partition12, new OffsetAndMetadata(6288660)));
//        TopicPartition partition13=new TopicPartition(topic,13);
//        consumer.commitSync(Collections.singletonMap(partition13, new OffsetAndMetadata(4837342)));
//        TopicPartition partition14=new TopicPartition(topic,14);
//        consumer.commitSync(Collections.singletonMap(partition14, new OffsetAndMetadata(5809806)));
//        TopicPartition partition15=new TopicPartition(topic,15);
//        consumer.commitSync(Collections.singletonMap(partition15, new OffsetAndMetadata(5692517)));
//        TopicPartition partition16=new TopicPartition(topic,16);
//        consumer.commitSync(Collections.singletonMap(partition16, new OffsetAndMetadata(5967143)));
//        TopicPartition partition17=new TopicPartition(topic,17);
//        consumer.commitSync(Collections.singletonMap(partition17, new OffsetAndMetadata(4875247)));


        //目偏移量修改 topic：jfz_ZHposcan_pkt    groupid： yaliceshi_test

//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(1116085)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(1103840)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(1153867)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(1151183)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(1521696)));
//        TopicPartition partition5=new TopicPartition(topic,5);
//        consumer.commitSync(Collections.singletonMap(partition5, new OffsetAndMetadata(1370786)));
//        TopicPartition partition6=new TopicPartition(topic,6);
//        consumer.commitSync(Collections.singletonMap(partition6, new OffsetAndMetadata(1478233)));
//        TopicPartition partition7=new TopicPartition(topic,7);
//        consumer.commitSync(Collections.singletonMap(partition7, new OffsetAndMetadata(1398224)));
//        TopicPartition partition8=new TopicPartition(topic,8);
//        consumer.commitSync(Collections.singletonMap(partition8, new OffsetAndMetadata(1353032)));
//        TopicPartition partition9=new TopicPartition(topic,9);
//        consumer.commitSync(Collections.singletonMap(partition9, new OffsetAndMetadata(1090568)));
//        TopicPartition partition10=new TopicPartition(topic,10);
//        consumer.commitSync(Collections.singletonMap(partition10, new OffsetAndMetadata(1091193)));
//        TopicPartition partition11=new TopicPartition(topic,11);
//        consumer.commitSync(Collections.singletonMap(partition11, new OffsetAndMetadata(1183269)));
//        TopicPartition partition12=new TopicPartition(topic,12);
//        consumer.commitSync(Collections.singletonMap(partition12, new OffsetAndMetadata(1168241)));
//        TopicPartition partition13=new TopicPartition(topic,13);
//        consumer.commitSync(Collections.singletonMap(partition13, new OffsetAndMetadata(1372552)));
//        TopicPartition partition14=new TopicPartition(topic,14);
//        consumer.commitSync(Collections.singletonMap(partition14, new OffsetAndMetadata(1364986)));
//        TopicPartition partition15=new TopicPartition(topic,15);
//        consumer.commitSync(Collections.singletonMap(partition15, new OffsetAndMetadata(1489689)));
//        TopicPartition partition16=new TopicPartition(topic,16);
//        consumer.commitSync(Collections.singletonMap(partition16, new OffsetAndMetadata(1480725)));
//        TopicPartition partition17=new TopicPartition(topic,17);
//        consumer.commitSync(Collections.singletonMap(partition17, new OffsetAndMetadata(1382783)));




        //目偏移量修改 青汽数据 topic：jfz_ZHposcan_pkt    groupid： ni-tripintegration-ws-0804-1

//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(1522845)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(1514528)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(1560747)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(1535507)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(1914641)));
//        TopicPartition partition5=new TopicPartition(topic,5);
//        consumer.commitSync(Collections.singletonMap(partition5, new OffsetAndMetadata(1769264)));
//        TopicPartition partition6=new TopicPartition(topic,6);
//        consumer.commitSync(Collections.singletonMap(partition6, new OffsetAndMetadata(1881655)));
//        TopicPartition partition7=new TopicPartition(topic,7);
//        consumer.commitSync(Collections.singletonMap(partition7, new OffsetAndMetadata(1786248)));
//        TopicPartition partition8=new TopicPartition(topic,8);
//        consumer.commitSync(Collections.singletonMap(partition8, new OffsetAndMetadata(1756890)));
//        TopicPartition partition9=new TopicPartition(topic,9);
//        consumer.commitSync(Collections.singletonMap(partition9, new OffsetAndMetadata(1489482)));
//        TopicPartition partition10=new TopicPartition(topic,10);
//        consumer.commitSync(Collections.singletonMap(partition10, new OffsetAndMetadata(1492248)));
//        TopicPartition partition11=new TopicPartition(topic,11);
//        consumer.commitSync(Collections.singletonMap(partition11, new OffsetAndMetadata(1597820)));
//        TopicPartition partition12=new TopicPartition(topic,12);
//        consumer.commitSync(Collections.singletonMap(partition12, new OffsetAndMetadata(1566409)));
//        TopicPartition partition13=new TopicPartition(topic,13);
//        consumer.commitSync(Collections.singletonMap(partition13, new OffsetAndMetadata(1740278)));
//        TopicPartition partition14=new TopicPartition(topic,14);
//        consumer.commitSync(Collections.singletonMap(partition14, new OffsetAndMetadata(1766029)));
//        TopicPartition partition15=new TopicPartition(topic,15);
//        consumer.commitSync(Collections.singletonMap(partition15, new OffsetAndMetadata(1882647)));
//        TopicPartition partition16=new TopicPartition(topic,16);
//        consumer.commitSync(Collections.singletonMap(partition16, new OffsetAndMetadata(1909588)));
//        TopicPartition partition17=new TopicPartition(topic,17);
//        consumer.commitSync(Collections.singletonMap(partition17,new OffsetAndMetadata(1770066)));



//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(1214312)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(1210677)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(1230199)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(1210949)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(1229173)));

//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(46923020)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(46727490)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(47401172)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(46619198)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(46789030)));


//DP测试--------------------------------------------------------------------------------------------------------------
//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(7353778)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(7437101)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(8092697)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(8153848)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(8385574)));
//        TopicPartition partition5=new TopicPartition(topic,5);
//        consumer.commitSync(Collections.singletonMap(partition5, new OffsetAndMetadata(7311743)));
//        TopicPartition partition6=new TopicPartition(topic,6);
//        consumer.commitSync(Collections.singletonMap(partition6, new OffsetAndMetadata(8476243)));
//        TopicPartition partition7=new TopicPartition(topic,7);
//        consumer.commitSync(Collections.singletonMap(partition7, new OffsetAndMetadata(8059458)));
//        TopicPartition partition8=new TopicPartition(topic,8);
//        consumer.commitSync(Collections.singletonMap(partition8, new OffsetAndMetadata(7907704)));
//        TopicPartition partition9=new TopicPartition(topic,9);
//        consumer.commitSync(Collections.singletonMap(partition9, new OffsetAndMetadata(8357668)));
//        TopicPartition partition10=new TopicPartition(topic,10);
//        consumer.commitSync(Collections.singletonMap(partition10, new OffsetAndMetadata(7599593)));
//        TopicPartition partition11=new TopicPartition(topic,11);
//        consumer.commitSync(Collections.singletonMap(partition11, new OffsetAndMetadata(8214133)));
//        TopicPartition partition12=new TopicPartition(topic,12);
//        consumer.commitSync(Collections.singletonMap(partition12, new OffsetAndMetadata(8334927)));
//        TopicPartition partition13=new TopicPartition(topic,13);
//        consumer.commitSync(Collections.singletonMap(partition13, new OffsetAndMetadata(7317962)));
//        TopicPartition partition14=new TopicPartition(topic,14);
//        consumer.commitSync(Collections.singletonMap(partition14, new OffsetAndMetadata(7257029)));
//        TopicPartition partition15=new TopicPartition(topic,15);
//        consumer.commitSync(Collections.singletonMap(partition15, new OffsetAndMetadata(7770082)));
//        TopicPartition partition16=new TopicPartition(topic,16);
//        consumer.commitSync(Collections.singletonMap(partition16, new OffsetAndMetadata(7753957)));
//        TopicPartition partition17=new TopicPartition(topic,17);
//        consumer.commitSync(Collections.singletonMap(partition17,new OffsetAndMetadata(6771063)));

        //--------------DP-kafka丢数据测试-----------------------
//        TopicPartition partition0=new TopicPartition(topic,0);
//        consumer.commitSync(Collections.singletonMap(partition0, new OffsetAndMetadata(7168732)));
//        TopicPartition partition1=new TopicPartition(topic,1);
//        consumer.commitSync(Collections.singletonMap(partition1, new OffsetAndMetadata(7269811)));
//        TopicPartition partition2=new TopicPartition(topic,2);
//        consumer.commitSync(Collections.singletonMap(partition2, new OffsetAndMetadata(7755111)));
//        TopicPartition partition3=new TopicPartition(topic,3);
//        consumer.commitSync(Collections.singletonMap(partition3, new OffsetAndMetadata(7813353)));
//        TopicPartition partition4=new TopicPartition(topic,4);
//        consumer.commitSync(Collections.singletonMap(partition4, new OffsetAndMetadata(8248278)));
//        TopicPartition partition5=new TopicPartition(topic,5);
//        consumer.commitSync(Collections.singletonMap(partition5, new OffsetAndMetadata(7159957)));
//        TopicPartition partition6=new TopicPartition(topic,6);
//        consumer.commitSync(Collections.singletonMap(partition6, new OffsetAndMetadata(8216528)));
//        TopicPartition partition7=new TopicPartition(topic,7);
//        consumer.commitSync(Collections.singletonMap(partition7, new OffsetAndMetadata(7772097)));
//        TopicPartition partition8=new TopicPartition(topic,8);
//        consumer.commitSync(Collections.singletonMap(partition8, new OffsetAndMetadata(7672227)));
//        TopicPartition partition9=new TopicPartition(topic,9);
//        consumer.commitSync(Collections.singletonMap(partition9, new OffsetAndMetadata(8122799)));
//        TopicPartition partition10=new TopicPartition(topic,10);
//        consumer.commitSync(Collections.singletonMap(partition10, new OffsetAndMetadata(7441063)));
//        TopicPartition partition11=new TopicPartition(topic,11);
//        consumer.commitSync(Collections.singletonMap(partition11, new OffsetAndMetadata(8044058)));
//        TopicPartition partition12=new TopicPartition(topic,12);
//        consumer.commitSync(Collections.singletonMap(partition12, new OffsetAndMetadata(8110799)));
//        TopicPartition partition13=new TopicPartition(topic,13);
//        consumer.commitSync(Collections.singletonMap(partition13, new OffsetAndMetadata(7113542)));
//        TopicPartition partition14=new TopicPartition(topic,14);
//        consumer.commitSync(Collections.singletonMap(partition14, new OffsetAndMetadata(6920562)));
//        TopicPartition partition15=new TopicPartition(topic,15);
//        consumer.commitSync(Collections.singletonMap(partition15, new OffsetAndMetadata(7424919)));
//        TopicPartition partition16=new TopicPartition(topic,16);
//        consumer.commitSync(Collections.singletonMap(partition16, new OffsetAndMetadata(7508608)));
//        TopicPartition partition17=new TopicPartition(topic,17);
//        consumer.commitSync(Collections.singletonMap(partition17,new OffsetAndMetadata(6539076)));


        System.out.println("reset_kafka_offset_success.");
    }
}
