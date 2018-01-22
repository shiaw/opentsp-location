package com.terminal.service;

/**
 * Created by zhanhk on 2017/3/31.
 */
import com.terminal.cache.StatisticalData;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class LogManager {
    private static StringBuffer sb = new StringBuffer();
    public void start() {
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long interval = StatisticalData.statisticalInterval;
                        long current = new Date().getTime();
                        Thread.sleep(interval * 1000);
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(current);
                        String startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(StatisticalData.startDate * 1000);
                        String stageStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(StatisticalData.stageStartDate * 1000);
                        double normalPacketMiss = 0;
                        if (StatisticalData.sendPacketNumber - StatisticalData.errorPacketNumber != 0) {
                            normalPacketMiss = (StatisticalData.sendPacketNumber - StatisticalData.errorPacketNumber - StatisticalData.receivePacketNumber + StatisticalData.errorPacketResNumber) * 100 / (double) (StatisticalData.sendPacketNumber - StatisticalData.errorPacketNumber);
                        }
                        double errorpacketMiss = 0;
                        if (StatisticalData.errorPacketNumber > 0) {
                            errorpacketMiss = (StatisticalData.errorPacketNumber - StatisticalData.errorPacketResNumber) * 100 / (double) StatisticalData.errorPacketNumber;
                        }
                        double answerTimes = 0;
                        if (StatisticalData.messageNumber != 0) {
                            answerTimes = StatisticalData.messageAnswerTimes / (double) StatisticalData.messageNumber;
                        }
                        sb.append(stageStartDate).append(",").append(currentDate).append(",").append(StatisticalData.sendPacketNumber).append(",");
                        sb.append(StatisticalData.receivePacketNumber).append(",").append(StatisticalData.upFlow).append(",");
                        sb.append(StatisticalData.downFlow).append(",").append(StatisticalData.errorPacketNumber).append(",");
                        sb.append(StatisticalData.errorPacketResNumber).append(",").append(StatisticalData.disconnectTimes).append(",");
                        sb.append(StatisticalData.connectTimes).append(",");
                        sb.append(normalPacketMiss).append(",").append(errorpacketMiss).append(",");
                        sb.append(answerTimes);
                        ResultStatisticalDataLog.writeLog(sb.toString());
                        sb.delete(0, sb.length());
                        StatisticalData.sendPacketNumber = 0;
                        StatisticalData.receivePacketNumber = 0;
                        StatisticalData.upFlow = 0;
                        StatisticalData.downFlow = 0;
                        StatisticalData.errorPacketNumber = 0;
                        StatisticalData.errorPacketResNumber = 0;
                        StatisticalData.disconnectTimes = 0;
                        StatisticalData.connectTimes = 0;
                        StatisticalData.stageStartDate = current / 1000;
                        StatisticalData.messageNumber = 0;
                        StatisticalData.messageAnswerTimes = 0;
                        double normalPacketMissTotal = 0;
                        if (StatisticalData.sendTotalPacketNumber - StatisticalData.errorTotalPacketNumber != 0) {
                            normalPacketMissTotal = (StatisticalData.sendTotalPacketNumber - StatisticalData.errorTotalPacketNumber - StatisticalData.receiveTotalPacketNumber + StatisticalData.errorTotalPacketResNumber) * 100 / (double) (StatisticalData.sendTotalPacketNumber - StatisticalData.errorTotalPacketNumber);
                        }
                        double errorpacketMissTotal = 0;
                        if (StatisticalData.errorTotalPacketNumber > 0) {
                            errorpacketMissTotal = (StatisticalData.errorTotalPacketNumber - StatisticalData.errorTotalPacketResNumber) * 100 / (double) StatisticalData.errorTotalPacketNumber;
                        }
                        double answerTotalTimes = 0;
                        if (StatisticalData.messageTotalNumber != 0) {
                            answerTotalTimes = StatisticalData.messageAnswerTotalTimes / (double) StatisticalData.messageTotalNumber;
                        }
                        sb.append(startDate).append(",").append(currentDate).append(",").append(StatisticalData.sendTotalPacketNumber).append(",");
                        sb.append(StatisticalData.receiveTotalPacketNumber).append(",").append(StatisticalData.upTotalFlow).append(",");
                        sb.append(StatisticalData.downTotalFlow).append(",").append(StatisticalData.errorTotalPacketNumber).append(",");
                        sb.append(StatisticalData.errorTotalPacketResNumber).append(",").append(StatisticalData.disconnectTotalTimes).append(",");
                        sb.append(StatisticalData.connectTotalTimes).append(",").append(normalPacketMissTotal).append(",").append(errorpacketMissTotal).append(",");
                        sb.append(StatisticalData.receiveTotalPacketNumber / (double) (current / 1000 - StatisticalData.startDate)).append(",");
                        sb.append(answerTotalTimes);
                        ResultStatisticalDataLog.writeLog(sb.toString());
                        sb.delete(0, sb.length());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "log_thread");
        t2.start();
    }
}