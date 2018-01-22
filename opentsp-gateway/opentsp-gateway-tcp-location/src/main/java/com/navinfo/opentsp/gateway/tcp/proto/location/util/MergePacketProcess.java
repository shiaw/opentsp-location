package com.navinfo.opentsp.gateway.tcp.proto.location.util;


import com.navinfo.opentsp.gateway.tcp.proto.location.netty.PacketProcessing;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;

/**
 * 分包数据的统一处理
 * User: zhanhk
 * Date: 16/6/16
 * Time: 下午3:02
 */
public class MergePacketProcess extends PacketProcessing {

    /**
     * 分包数据上行write方式
     * 如果是分包数据,按照分包方式回写,否则直接写
     *
     * @param outpacket
     * @param bytes
     * @param out
     */
//    public static void writeMergePacketToConnector(Packet outpacket, byte[] bytes, ProtocolDecoderOutput out) {
//        if (outpacket != null && bytes.length > 0) {
//            int cmdProperty = Convert.byte2Int(ArraysUtils.subarrays(bytes, 3, 2), 2);
//            if ((cmdProperty & 8192) > 0) {
//                String uniqueMark = Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 5, 6));
//                int total = Convert.byte2Int(ArraysUtils.subarrays(bytes, 13, 2), 2);// 消息包封装项,包总数
//                int serial = Convert.byte2Int(ArraysUtils.subarrays(bytes, 15, 2), 2);// 消息包封装项,包序号
//                int blockId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 17, 2), 2);//消息包封装项,包块ID
//                byte[] content = ArraysUtils.subarrays(bytes, 19, bytes.length - 20);
//                outpacket.setPacketTotal(total);
//                outpacket.setPacketSerial(serial);
//                outpacket.setContent(content);
//                outpacket.setBlockId(blockId);
//                boolean isComplete = PacketProcessing.mergeBlock(outpacket);
//                if (isComplete) {
//                    outpacket = PacketProcessing.getCompletePacket(PacketProcessing.getCacheBlockId(uniqueMark, blockId));
//                    out.write(outpacket);
//                }
//            } else {
//                byte[] content = ArraysUtils.subarrays(bytes, 13, bytes.length - 14);
//                outpacket.setContent(content);
//                out.write(outpacket);
//            }
//        }
//    }

    /**
     * 终端的分包数据聚合
     *
     * @param outpacket
     * @param bytes
     * @return
     */
    public static Packet addMergePacketToAcceptor(Packet outpacket, byte[] bytes) {
        int cmdProperty = Convert.byte2Int(ArraysUtils.subarrays(bytes, 2, 2), 2);
        //判断是否分包
        if ((cmdProperty & 8192) > 0) {
            String uniqueMark = Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 4, 6));
            int total = Convert.byte2Int(ArraysUtils.subarrays(bytes, 12, 2), 2);// 消息包封装项,包总数
            int serial = Convert.byte2Int(ArraysUtils.subarrays(bytes, 14, 2), 2);// 消息包封装项,包序号
            byte[] content = ArraysUtils.subarrays(bytes, 16, bytes.length - 17);
            outpacket.setUniqueMark(uniqueMark);
            outpacket.setPacketTotal(total);
            outpacket.setPacketSerial(serial);
            outpacket.setContent(content);
        } else {
            byte[] content = ArraysUtils.subarrays(bytes, 12, bytes.length - 13);
            outpacket.setContent(content);
        }
        return outpacket;
    }

//    /**
//     * 定时清理分包丢失数据的缓存
//     */
//    public class InnerClearMergeDataCache extends ITask {
//        @Override
//        public void run() {
//            //Packets中,为空的包位置集合
//            int i = 0;
//            List<Integer> errorPositionList;
//            if (!_packet_cache.isEmpty()) {
//                for (Map.Entry<Long, Block> e : _packet_cache.entrySet()) {
//                    Block block = e.getValue();
//                    if (block != null) {
//                        if(System.currentTimeMillis() / 1000 - block.getCreateTime() > Configuration.getInt("LOST_PACKET_LIMIT_TIME")) {
//                            errorPositionList = new ArrayList<>();
//                            int packetsLenght = block.getPackets().length;
//                            String uniqueMark = "";
//                            if(packetsLenght > 0) {
//                                for (int position = 0 ; position < packetsLenght ; position++) {
//                                    if(block.getPackets()[position] == null) {
//                                        errorPositionList.add(position+1);
//                                        log.info(block.toString() +"; position:" + (position + 1));
//                                    }else {
//                                        if(uniqueMark.equals("")) {
//                                            uniqueMark = block.getPackets()[position].getUniqueMark();
//                                        }
//                                    }
//                                }
//                            }
//                            //丢包数据的处理,回写终端
//                            Packet packet = new Packet(true);
//                            if(!errorPositionList.isEmpty()) {
//                                packet.setCommand(0x8003);
//                                byte[] content = new byte[2+1+2*errorPositionList.size()];
//                                ArraysUtils.arrayappend(content, 0, Convert.intTobytes(packet.getSerialNumber(),2));
//                                ArraysUtils.arrayappend(content, 2, Convert.intTobytes(errorPositionList.size(),1));
//                                for(int j = 0 ; j < errorPositionList.size() ; j++) {
//                                    ArraysUtils.arrayappend(content,3+j*2, Convert.intTobytes(errorPositionList.get(j),2));
//                                }
//                                packet.setUniqueMark(uniqueMark);
//                                packet.setContent(content);
//                            }
//                            Command command  = CommandCache.getInstance().getCommand(packet.getCommandForHex());
//                            if(command != null) {
//                                command.processor(packet);
//                            } else {
//                                log.error("load 0x8003 class error !");
//                            }
//                        }
//                        if(System.currentTimeMillis() / 1000 - block.getCreateTime() > Configuration.getInt("MERGE_PACKET_LIMIT_TIME")) {
//                            _packet_cache.remove(e.getKey());
//                            log.debug("清除分包缓存[ " + e.getKey() + " ] ,block.total=" + block.getTotal() + ", block.packets.length=" + block.getPackets().length);
//                            i++;
//                        }
//                    }
//                }
//                log.error("本次任务清除:" + i + "条缓存.");
//            } else {
//                log.error("分包缓存数据为空,无需清理,任务结束.");
//            }
//        }
//    }
}
