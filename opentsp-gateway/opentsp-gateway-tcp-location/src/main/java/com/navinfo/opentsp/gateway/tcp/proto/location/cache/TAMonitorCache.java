package com.navinfo.opentsp.gateway.tcp.proto.location.cache;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: zhanhk
 * Date: 16/11/21
 * Time: 上午11:27
 */
public class TAMonitorCache {

    private static AtomicInteger intoTACount = new AtomicInteger(0);

    private static AtomicInteger goKAFKACount = new AtomicInteger(0);

    private static AtomicInteger taBackCount = new AtomicInteger(0);

    private static AtomicLong goKAFKATime = new AtomicLong(0);

    public static void addTACount() {
        if(intoTACount == null) {
            intoTACount = new AtomicInteger(0);
        }
        intoTACount.incrementAndGet();
    }

    public static int getTABackCount() {
        return taBackCount.get();
    }


    public static void addTABackCount() {
        if(taBackCount == null) {
            taBackCount = new AtomicInteger(0);
        }
        taBackCount.incrementAndGet();
    }

    public static int getTACount() {
        return intoTACount.get();
    }

    public static void addKafkaCount() {
        if(goKAFKACount == null) {
            goKAFKACount = new AtomicInteger(0);
        }
        goKAFKACount.incrementAndGet();
    }

    public static int getKAFKACount() {
        return goKAFKACount.get();
    }

    public static void addKafkaTime(long time) {
//        if(goKAFKATime == null) {
//            goKAFKATime = new AtomicLong(0);
//        }

        goKAFKATime.addAndGet(time);
    }

    public static long getKAFKATime() {
        return goKAFKACount.get();
    }
}
