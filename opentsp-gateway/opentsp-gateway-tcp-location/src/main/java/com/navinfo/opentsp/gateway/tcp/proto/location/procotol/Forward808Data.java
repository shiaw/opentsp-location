package com.navinfo.opentsp.gateway.tcp.proto.location.procotol;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.HexUtil;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Created by wanliang on 2017/3/24.
 */
@Component
public class Forward808Data {

    private static final Logger log = LoggerFactory.getLogger(Forward808Data.class);


    @Autowired
    private TcpClient tcpClient;

//    @Autowired
//    private TcpClient pktTcpClient;
    /**
     * 消费执行线程池
     */
    static ExecutorService executorServicePool = Executors.newFixedThreadPool(5);


    public void forward(byte[] data) {
        try {
            if (tcpClient != null&&tcpClient.getChannel()!=null) {
                executorServicePool.execute(new Consumer(data, tcpClient));
            }
//            if (pktTcpClient != null&&pktTcpClient.getChannel()!=null){
//                executorServicePool.execute(new PktConsumer(data, pktTcpClient));
//            }
        } catch (Exception e) {
            log.error("数据格式错误!", e);
        }
    }

    public static class Consumer implements Runnable {
        private byte[] data;
        private TcpClient tcpClient;


        public Consumer(byte[] data, TcpClient tcpClient) {
            this.data = data;
            this.tcpClient = tcpClient;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            try {
                tcpClient.getChannel().writeAndFlush(data);
                log.info("TA 转发 SUCCESS!{}", HexUtil.bytesToHexString(data));
            } catch (InterruptedException e) {
                log.error("TA 转发!{}", HexUtil.bytesToHexString(data));
                Thread.currentThread().interrupt();
            }
        }
    }


    public static class PktConsumer implements Runnable {
        private byte[] data;
        private TcpClient pktTcpClient;


        public PktConsumer(byte[] data, TcpClient pktTcpClient) {
            this.data = data;
            this.pktTcpClient = pktTcpClient;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            try {
                pktTcpClient.getChannel().writeAndFlush(data);
                log.info("TA 转发 pkt SUCCESS!{}", HexUtil.bytesToHexString(data));
            } catch (InterruptedException e) {
                log.error("TA 转发 pkt!{}", HexUtil.bytesToHexString(data));
                Thread.currentThread().interrupt();
            }
        }
    }
}
