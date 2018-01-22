package com.terminal.service;

/**
 * Created by zhanhk on 2017/3/31.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ResultStatisticalDataLog {
    public static Logger logger = LoggerFactory.getLogger(ResultStatisticalDataLog.class);
    public static void writeLogTitle() {
        logger.info("开始时间,结束时间,发包总数,接受包总数,上行流量(k),下行流量(k),异常包总数,异常包应答数,断开连接次数,重连成功次数,正常数据丢包率(%),异常数据丢包率(%),并发量,消息平均响应时间(ms)");
    }
    public static void writeLog(String log) {
        logger.info(log);
    }
}