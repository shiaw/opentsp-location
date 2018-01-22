package com.navinfo.opentsp.platform.dp.core.rule.handler.common;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.location.kit.PacketProcessing;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;

/**
 * User: zhanhk
 * Date: 16/7/20
 * Time: 上午11:23
 */
public class RegularHandlerTest {

    public static void main(String args[]) {
        byte[] packet = Convert
                .hexStringToBytes("7E023002019C01827460000100010802108382F00118A5FDCD332090F5AD0E289EDBCD3330CC89AE0E38014000480150A4F9B7BC0558C0A90760A0F9B7BC05680070007800A00102A80100B801FFFF018202CC010A05080110F02E0A0508021090030A06080410A09C010A05080510A3370A0508061094230A0608071094EB010A05080810CF080A05080310F02E0A05080910B8170A04080C10000A05080E10D00F0A04080A10640A05080B1098750A06081010A09C010A05081110B8170A05081210E02B0A05081310E80C0A04081410640A05081510E02B0A05081610D8360A05081710A01F0A05081810C8010A0A081910A09982E5A8FE0A0A06081A10A0E1670A06081B1090DE4E0A05081C10E0120A06080F1080EC0E0A05081D10CC1E8A02200A04080010000A060880C801101C0A04086410110A04086410130A0408641003920208FFFFFFFF1FDFFFFF9A02023004A00200B00200BA024E0800100018002000280035000000003D00000000400048005000580065000000006D0000000070007D01000000008001008801009001009D0100000000A50100000000A80100B00100B80100C00100187B");
        byte[] tempBytes = new byte[packet.length - 2];
        System.arraycopy(packet, 1, tempBytes, 0, tempBytes.length);
        byte[] bytes = PacketProcessing.unEscape(tempBytes, LCConstant.toEscapeByte, LCConstant.escapeByte);

        int msgType = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 1), 1);
//        int cmdId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 1, 2), 2);
//        if(cmdId == 0x3002){
//            NodeHelper.TA3002Num++;
//        }
        int cmdProperty = Convert.byte2Int(ArraysUtils.subarrays(bytes, 3, 2), 2);
        String uniqueMark = Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 5, 6));
        int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 11, 2), 2);

        Packet outpacket = new Packet();
        //outpacket.setCommand(cmdId);
        outpacket.setSerialNumber(serialNumber);
        outpacket.setProtocol(msgType);
        outpacket.setOriginalPacket(packet);
        outpacket.setUniqueMark(uniqueMark);
        outpacket.setFrom(Long.parseLong(uniqueMark));

        //判断是否分包
        if((cmdProperty & 8192) > 0){
            int total = Convert.byte2Int(ArraysUtils.subarrays(bytes, 13, 2), 2);// 消息包封装项,包总数
            int serial = Convert.byte2Int(ArraysUtils.subarrays(bytes, 15, 2), 2);// 消息包封装项,包序号
            int blockId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 17, 2), 2);//消息包封装项,包块ID
            byte[] content = ArraysUtils.subarrays(bytes, 19 , bytes.length - 20);
            outpacket.setPacketTotal(total);
            outpacket.setPacketSerial(serial);
            outpacket.setBlockId(blockId);
            outpacket.setContent(content);
//            boolean isComplete = PacketProcessing.mergeBlock(outpacket);
//            if( isComplete ){
//                outpacket = PacketProcessing.getCompletePacket(PacketProcessing.getCacheBlockId(uniqueMark, blockId));
//            }
        }else{
            byte[] content = ArraysUtils.subarrays(bytes, 13, bytes.length - 14);
            outpacket.setContent(content);
        }

        LCLocationData.LocationData locationData = null;
        try {
            locationData = LCLocationData.LocationData.parseFrom(outpacket.getContent());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        GpsLocationDataEntity dataEntity = new GpsLocationDataEntity(locationData , outpacket.getUniqueMark() , outpacket.getSerialNumber() , LCAllCommands.AllCommands.Terminal.ReportLocationData_VALUE);
        //RegularHandler.handler(dataEntity);
    }
}
