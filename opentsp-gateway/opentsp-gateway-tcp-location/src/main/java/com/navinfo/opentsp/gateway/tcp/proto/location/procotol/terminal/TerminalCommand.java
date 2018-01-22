package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.DownCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TAMonitorCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.netty.AppendToFile;
import com.navinfo.opentsp.platform.auth.DownStatusCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.Command;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.push.DownCommandState;
import com.navinfo.opentspcore.common.kafka.serializers.KafkaJsonDeserializer;
import com.navinfo.opentspcore.common.kafka.serializers.KafkaJsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.*;

public abstract class TerminalCommand extends Command {

    public static Logger log = LoggerFactory.getLogger(TerminalCommand.class);

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Autowired
    private MessageChannel messageChannel;

    @Value("${opentsp.down.command.reply.maxCount:2}")
    private int replyMaxCount;
    /**
     * 点缓存队列
     */
    static BlockingQueue<KafkaCommand> queue = new LinkedBlockingQueue<>(100);

    /**
     * 消费执行线程池
     */
    static ExecutorService executorServicePool = Executors.newFixedThreadPool(1);
    /**
     * 发送通用应答
     *
     * @param uniqueMark
     * @param responsesSerialNumber
     * @param responsesId
     * @param result
     * @return
     */
    public Packet commonResponses(String uniqueMark, int responsesSerialNumber,
                                  int responsesId, int result) {
        Packet packet = new Packet(false, 5);
        packet.setSerialNumber(responsesSerialNumber);
        packet.setUniqueMark(uniqueMark);
        packet.setCommand(0x8001);
        packet.appendContent(Convert.longTobytes(responsesSerialNumber, 2));
        packet.appendContent(Convert.longTobytes(responsesId, 2));
        packet.appendContent(Convert.longTobytes(result, 1));
        return packet;
    }

   public void sendCommandStatus(String uniqueMark ,int serialNumber,Packet packet){
       String sendId = DownCommandCache.getInstance().get(uniqueMark + "-" + serialNumber);
       String cacheKey=uniqueMark + "-" + serialNumber;
       if(StringUtils.isEmpty(sendId)){
           for(int n=1;n<=replyMaxCount;n++) {
               String key=uniqueMark + "-" + serialNumber+"-"+n;
               String tempSendId=DownCommandCache.getInstance().get(key);
               if (!StringUtils.isEmpty(tempSendId)){
                   sendId=tempSendId;
                   cacheKey=key;
                   break;
               }
           }
       }
       log.info("通知Push:[key={},sendId={},执行状态={},uniqueMark={},serialNumber={},commandId={}]",cacheKey,sendId,DownCommandState.T_EXECUTE.getValue(),uniqueMark,serialNumber,packet.getCommandForHex());
       if (!org.springframework.util.StringUtils.isEmpty(sendId)) {
           DownStatusCommand downStatusCommand = new DownStatusCommand();
           downStatusCommand.setState(DownCommandState.T_EXECUTE);
           downStatusCommand.setId(sendId);
           downStatusCommand.setData(packet.getContentForHex());
           downStatusCommand.setCommandId(packet.getCommandForHex());
           downStatusCommand.setCommand(packet.getCommand());
           downStatusCommand.setSerialNumber(packet.getSerialNumber());
           downStatusCommand.setProtocol(packet.getProtocol());
           messageChannel.send(downStatusCommand);
           DownCommandCache.getInstance().remove(cacheKey);
           AnswerCommandCache.getInstance().remove(uniqueMark, serialNumber);

       }
   }
//
//	/**
//	 * 向终端发送数据
//	 */
//	public int write(NettyClientConnection connection, Packet packet) {
//        connection.get
//		AnswerEntry answerEntry = AnswerCommandCache.getInstance().getAnswerEntry(packet.getUniqueMark(), packet.getSerialNumber(), true);
//		if(answerEntry != null){
//			LCDownCommonRes.DownCommonRes.Builder builder = LCDownCommonRes.DownCommonRes.newBuilder();
//			builder.setResponseId(answerEntry.getInternalCommand());
//			builder.setSerialNumber(packet.getSerialNumber());
//			builder.setResult(LCResponseResult.ResponseResult.failure);
//
//			Packet _out_packet = new Packet();
//			_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.DownCommonRes_VALUE);
//			_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
//			_out_packet.setUniqueMark(packet.getUniqueMark());
//			_out_packet.setSerialNumber(packet.getSerialNumber());
//			_out_packet.setContent(builder.build().toByteArray());
//			this.writeKafKaToDP(_out_packet,_out_packet.getCommandForHex());
//		}
//		return result;
//	}


    /**
     * 发送kafka数据到DP
     * @param packet
     * @param topicName
     */
	public void writeKafKaToDP(Packet packet,String topicName){

        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
         //   log.error("content大小：{}",packet.getContent().length);
            long startTime=System.currentTimeMillis();
            kafkaCommand.setMessage(packet.getContent());
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(topicName);
            kafkaCommand.setKey(packet.getUniqueMark());
//            try {
//                if (!queue.offer(kafkaCommand, 100, TimeUnit.MICROSECONDS)) {
//                    log.debug("放入数据失败：" + kafkaCommand);
//                }
//                log.debug("缓存队列:" + queue.size());
//                executorServicePool.execute(new Consumer(queue,kafkaMessageChannel));
//            } catch (InterruptedException e) {
//                log.error("放入队列异常",e);
//            }

             kafkaMessageChannel.send(kafkaCommand);

            TAMonitorCache.addKafkaTime(System.currentTimeMillis()-startTime);
            TAMonitorCache.addKafkaCount();
        } catch (Exception e) {
            log.error("序列化出错!{}",kafkaCommand,e);
        }
	}

    public static class Consumer implements Runnable {
        private BlockingQueue<KafkaCommand> queue;

        private KafkaMessageChannel kafkaMessageChannel;

        public Consumer(BlockingQueue<KafkaCommand> queue,KafkaMessageChannel kafkaMessageChannel) {
            this.queue = queue;
            this.kafkaMessageChannel = kafkaMessageChannel;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            boolean isRunning = true;
            try {
                while (isRunning) {
                    KafkaCommand kafkaCommand = queue.poll(100, TimeUnit.MILLISECONDS);
                    if (null != kafkaCommand) {
                        long s = System.currentTimeMillis();
                         kafkaMessageChannel.send(kafkaCommand);
                         long time = System.currentTimeMillis()-s;
                        log.debug(threadName+"-"+kafkaCommand.getTopic()+"消费完成数据：" + kafkaCommand.getCommandId() + ",待处理队列:" + queue.size() +",消息发送耗时:" +time + "ms");
                    } else {
                        // 超过2s还没数据，认为所有生产线程err都已经退出，自动退出消费线程。
                        isRunning = false;
                    }
                }
            } catch (InterruptedException e) {
                log.error("消费数据错误",e);
                Thread.currentThread().interrupt();
            } finally {
                log.debug("退出消费者线程！" + threadName);
            }
        }
    }

//	public int writeToDataAccess(Packet packet){
//		DAMutualSession daMutualSession = DAMutualSessionManage.getInstance().getActiveSession();
//		if(daMutualSession != null){
//			IoSession ioSession = daMutualSession.getIoSession();
//			if(ioSession != null && ioSession.isConnected()){
//				ioSession.write(packet);
//				return 1;
//			}else{
//				log.error("TA-DA链路关闭,发送数据失败.");
//			}
//		}else{
//			log.error("未找到TA-DA链路,发送数据失败.");
//		}
//		return 0;
//	}
//
//	public void collect(int cmdId, byte[] bytes) {
//
//	}
//
//	/**
//	 * 重复发送,直到终端确认收到
//	 * @param packet
//	 * @return
//     */
//	public int repeatWrite(Packet packet) {
//		int result = this.write(packet);
//		if(result != 1) {
//			RepeatSendEntry repeatSendEntry = new RepeatSendEntry();
//			repeatSendEntry.setPacket(packet);
//			RepeatSendCache.getInstance().addEntry(packet.getUniqueMark(),packet.getSerialNumber(),repeatSendEntry);
//			log.info("放入重发缓存队列,"+repeatSendEntry.toString());
//		}
//		return 0;
//	}
}
