package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Lenovo
 * @date 2016-11-30
 * @modify
 * @copyright
 */
public class StatisticsUtils {

    //收到的包总数
    public static AtomicLong sendTotalPacketNumInner = new AtomicLong(0);
    //发kafka成功的包总数
    public static AtomicLong sendTotalPacketNumInner_kafka = new AtomicLong(0);
    //发kafka失败的包总数
    public static AtomicLong sendTotalPacketNumInner_kafka_fail = new AtomicLong(0);
    public static AtomicLong sendTotalPacketNum808 = new AtomicLong(0);
    public static AtomicLong sendTotalPacketNum808_kafka = new AtomicLong(0);
    public static AtomicLong sendTotalPacketNum808_kafka_fail = new AtomicLong(0);
//    public static AtomicLong reveiveTotalPacketNumInner = new AtomicLong(0);
//    public static AtomicLong reveiveTotalPacketNum808 = new AtomicLong(0);
}
