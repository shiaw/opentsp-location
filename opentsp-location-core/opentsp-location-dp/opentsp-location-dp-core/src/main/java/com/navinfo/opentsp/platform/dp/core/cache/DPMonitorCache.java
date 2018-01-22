package com.navinfo.opentsp.platform.dp.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控线程缓存
 * User: zhanhk
 * Date: 16/11/21
 * Time: 上午11:27
 */
public class DPMonitorCache {

    private String name;

    private int intoDPCount ;

    private int goRPCount;

    private int goDACount;

    private long tid;

    /**
     * 过滤算法处理时间
     */
    private long processFilterTime;

    /**
     * 规则处理时间
     */
    private long ruleProcessTime;

    /**
     * 最长处理时长
     */
    private static long maxTime;

    private long ruleSignTime;

    private long encryptTime;

    private long forwardTime;

    public long getRuleSignTime() {
        return ruleSignTime;
    }

    public void setRuleSignTime(long ruleSignTime) {
        this.ruleSignTime = ruleSignTime;
    }

    public long getEncryptTime() {
        return encryptTime;
    }

    public void setEncryptTime(long encryptTime) {
        this.encryptTime = encryptTime;
    }

    public long getForwardTime() {
        return forwardTime;
    }

    public void setForwardTime(long forwardTime) {
        this.forwardTime = forwardTime;
    }

    /**
     * 每个规则链处理详细时间,<规则ID,处理时间ms>
     */
    private Map<String,Long> detailMap = new ConcurrentHashMap<>();

    public void addDPCount() {
        intoDPCount++;
    }

    public void decDPCount() {
        intoDPCount--;
    }

    /**
     * 缓存最长处理时间，处理详细map
     * @param time
     * @param map
     */
    public void setMaxTime(long time, Map<String,Long> map,long filterTime ,long ruleTime,long ruleSignTime,long encryptTime,long forwardTime) {
        if(time > maxTime) {
            maxTime = time;
            this.setProcessFilterTime(filterTime);
            this.setRuleProcessTime(ruleTime);
            this.setRuleSignTime(ruleSignTime);
            this.setEncryptTime(encryptTime);
            this.setForwardTime(forwardTime);
            detailMap = map;
        }
    }

    public int getDPCount() {
        return intoDPCount;
    }

    public void addRPCount() {
        goRPCount++;
    }

    public int getRPCount() {
        return goRPCount;
    }

    public void addDACount() {
        goDACount++;
    }

    public int getDACount() {
        return goDACount;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public Map<String, Long> getDetailMap() {
        return detailMap;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProcessFilterTime() {
        return processFilterTime;
    }

    public void setProcessFilterTime(long processFilterTime) {
        this.processFilterTime = processFilterTime;
    }

    public long getRuleProcessTime() {
        return ruleProcessTime;
    }

    public void setRuleProcessTime(long ruleProcessTime) {
        this.ruleProcessTime = ruleProcessTime;
    }
}
