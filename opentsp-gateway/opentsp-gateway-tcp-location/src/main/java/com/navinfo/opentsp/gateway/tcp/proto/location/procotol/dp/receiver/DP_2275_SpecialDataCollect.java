package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCCanDataCycleUploadSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: ChenJie
 * @date 2017/10/20
 * 特殊数据采集汇报设置
 * 对应终端协议的CAN数据周期上传配置
 */
@LocationCommand(id = "2275")
public class DP_2275_SpecialDataCollect extends DPCommand {

    private static Logger logger = LoggerFactory.getLogger(DP_2275_SpecialDataCollect.class);

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        try {
            LCCanDataCycleUploadSetting.CanDataCycleUploadSetting canDataCycleUploadSetting = LCCanDataCycleUploadSetting.CanDataCycleUploadSetting.parseFrom(packet.getContent());

            BytesArray bytesArray = new BytesArray();
            //波特率
            long BaudRate = canDataCycleUploadSetting.getBaudRate() & 4294967288L;
            //数据采集方式
            int collectionMode = canDataCycleUploadSetting.getCollectionMode() << 29;
            //帧类型
            int frameType = canDataCycleUploadSetting.getFrameType() << 30;
            //CAN 通道号
            int canChannel = canDataCycleUploadSetting.getCanChannel() << 31;
            //组装 CAN采集配置
            long first = BaudRate + collectionMode + frameType + canChannel;
            bytesArray.append(Convert.longTobytes(first,4));

            //上报周期
            int uploadCycle = canDataCycleUploadSetting.getUploadCycle();
            bytesArray.append(Convert.longTobytes(uploadCycle,2));

            logger.info("BaudRate:{},collectionMode:{},frameType:{},canChannel:{},first:{}, uploadCycle:{}.",
                    BaudRate, collectionMode, frameType, canChannel, first, uploadCycle);

            //不上报，则后面的字段不传
            if(uploadCycle > 0){
                //采集周期
                bytesArray.append(Convert.longTobytes(canDataCycleUploadSetting.getCollectCycle(),2));
                //采集时长  生命周期
                bytesArray.append(Convert.longTobytes(canDataCycleUploadSetting.getCollectionTime(),2));
                //警情阀值设置开关和CAN总线数据格式
                //警情阀值bit7-bit4:  0：未有警情阀值  1：有警情阀值 默认0
                // CAN上传数据格式bit3-bit0: 0：小端模式(默认) 1：大端模式
                bytesArray.append(Convert.longTobytes(0,2));

                logger.info("collectCycle:{},cllectionTime:{}.", canDataCycleUploadSetting.getCollectCycle(), canDataCycleUploadSetting.getCollectionTime());

                //设置canId等参数
                List<LCCanDataCycleUploadSetting.CanDataCycleUploadItems> itemsList = canDataCycleUploadSetting.getItemsList();
                if(itemsList != null){
                    for(int i=0;i<itemsList.size();i++){
                        LCCanDataCycleUploadSetting.CanDataCycleUploadItems canDataCycleUploadItems = itemsList.get(i);
                        //canId
                        bytesArray.append(Convert.longTobytes(canDataCycleUploadItems.getCanId(),4));
                        //参数id
                        bytesArray.append(Convert.longTobytes(canDataCycleUploadItems.getParaId(),2));
                        //起始位置
                        bytesArray.append(Convert.longTobytes(canDataCycleUploadItems.getBegin(),1));
                        //长度
                        bytesArray.append(Convert.longTobytes(canDataCycleUploadItems.getOffset(),1));

                        logger.info("canId:{}, paraId:{}, begin:{}, offset:{}.", canDataCycleUploadItems.getCanId(), canDataCycleUploadItems.getParaId(), canDataCycleUploadItems.getBegin(), canDataCycleUploadItems.getOffset());
                    }
                }

            }

            Packet outPacket = new Packet();
            outPacket.setCommand(Constant.JTProtocol.CANBUSDataQuery);
            outPacket.setUniqueMark(packet.getUniqueMark());
            outPacket.setSerialNumber(packet.getSerialNumber());
            outPacket.setContent(bytesArray.get());
            PacketResult packetResult=new PacketResult();
            packetResult.setTerminalPacket(outPacket);
            return packetResult;

        }catch (InvalidProtocolBufferException e){
            e.printStackTrace();
        }
        return null;
    }
}
