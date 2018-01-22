package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver.jt2013;


import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCQueryTerminalPropertyRes.CommunicationModule;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCQueryTerminalPropertyRes.GNSSModelProperty;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCQueryTerminalPropertyRes.QueryTerminalPropertyRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCQueryTerminalPropertyRes.TerminalType;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@LocationCommand(id = "0107",version = "200110")
@Component("JT_0107_TerminalAttributeRes_200110")
public class JT_0107_TerminalAttributeRes extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		PacketResult result=new PacketResult();
		log.error("收到查询终端属性应答,终端["+packet.getFrom()+"]");
		byte[] content = packet.getContent();

			int serialNumber=0;//TerminalPropertyAnswerCommandCache.getInstance().getAnswerEntry(packet.getUniqueMark(), AllCommands.Terminal.QueryTerminalProperty_VALUE, true);
			//终端类型
			int terminalType = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
			TerminalType.Builder type= TerminalType.newBuilder();
			if(terminalType>0){
				type.setIsSuitForPassengerVehicle((terminalType&1)==1?true:false);
				type.setIsSuitForDangerousGoods((terminalType&2)==2?true:false);
				type.setIsSuitForFreightTransport((terminalType&4)==4?true:false);
				type.setIsSuitForTaxi((terminalType&8)==8?true:false);
				type.setIsSuitDiskForVideo((terminalType&64)==64?true:false);
				type.setIntegratedTerminal((terminalType&128)==128?true:false);
		
			}
			
			//制造商ID
			String manufacturerId = Convert.bytesToHexString(ArraysUtils.subarrays(content, 2, 5));
			//终端型号
			String terminalModel = Convert.bytesToHexString(JT_0107_TerminalAttributeRes.filterBytes(ArraysUtils.subarrays(content, 7, 20),0x00));
			//终端ID
			String terminalIdentity = Convert.bytesToHexString(JT_0107_TerminalAttributeRes.filterBytes(ArraysUtils.subarrays(content, 27, 7),0x00));
			//终端SIM 卡ICCID
			long	terminalICCID = Convert.byte2Long(ArraysUtils.subarrays(content, 34, 10),10);
			//终端硬件版本号长度
			int hardwareVersionLength = Convert.byte2Int(ArraysUtils.subarrays(content, 44, 1),1);
			//终端硬件版本号
			String hardwareVersion = null;
			try {
				hardwareVersion = new String(ArraysUtils.subarrays(content, 45, hardwareVersionLength),"gbk");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//终端固件版本号长度
			int firmwareVersionLength=Convert.byte2Int(ArraysUtils.subarrays(content, 45+hardwareVersionLength, 1),1);
			//终端固件版本号
			String firmwareVersion = null;
			try {
				firmwareVersion = new String(ArraysUtils.subarrays(content, 46+hardwareVersionLength, firmwareVersionLength),"gbk");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 
			int GNSSModel= Convert.byte2Int(ArraysUtils.subarrays(content,46+hardwareVersionLength+firmwareVersionLength, 1),1);
	        //GNSS模块属性
			GNSSModelProperty.Builder gNSSModelProperty= GNSSModelProperty.newBuilder();
			if(GNSSModel>0){
				//是否支持GPS定位 true：是；false：否
				gNSSModelProperty.setIsSupportGps((GNSSModel&1)==1?true:false);
				//是否支持北斗定位 true：是；false：否
				gNSSModelProperty.setIsSupportBeidou((GNSSModel&2)==2?true:false);
				//是否支持GLONASS定位 true：是；false：否
				gNSSModelProperty.setIsSupportGlonass((GNSSModel&4)==4?true:false);
				//是否支持Galileo定位  true：是；false：否
				gNSSModelProperty.setIsSupportGalileo((GNSSModel&8)==2?true:false);
		
				
			}
			int CommunicationModuleValue=Convert.byte2Int(ArraysUtils.subarrays(content, 47+hardwareVersionLength+firmwareVersionLength, 1),1);
			//通信模块属性
			CommunicationModule.Builder communicationModule= CommunicationModule.newBuilder();
          if(CommunicationModuleValue>0){
			// isSupportGPRS 是否支持GPRS通信 true：是；false：否
        	  communicationModule.setIsSupportGPRS((CommunicationModuleValue&1)==1?true:false);
			// isSupportCDMA 是否支持CDMA通信 true：是；false：否
        	  communicationModule.setIsSupportCDMA((CommunicationModuleValue&2)==2?true:false);
			// isSupportTD_SCDMA 是否支持TD-SCDMA通信 true：是；false：否
        	  communicationModule.setIsSupportTDSCDMA((CommunicationModuleValue&4)==4?true:false);
			// isSupportWCDMA 是否支持WCDMA通信 true：是；false：否
        	  communicationModule.setIsSupportWCDMA((CommunicationModuleValue&8)==8?true:false);
			// isSupportCDMA2000 是否支持CDMA2000通信 true：是；false：否
        	  communicationModule.setIsSupportCDMA2000((CommunicationModuleValue&16)==16?true:false);
			// isSupportTD_LTE 是否支持TD-LTE通信true：是；false：否
        	  communicationModule.setIsSupportTDLTE((CommunicationModuleValue&32)==32?true:false);
			// isSupportOther 是否支持其他通信true：是；false：否
        	  communicationModule.setIsSupportOther((CommunicationModuleValue&128)==128?true:false);
          }
		
		QueryTerminalPropertyRes.Builder builder = QueryTerminalPropertyRes.newBuilder();
		builder.setSerialNumber(serialNumber);
		builder.setResult(LCResponseResult.ResponseResult.success);
		builder.setType(type);
		builder.setManufacturerId(manufacturerId);
		builder.setTerminalModel(terminalModel);
		builder.setTerminalIdentity(terminalIdentity);
		builder.setTerminalICCID(terminalICCID+"");
		builder.setFirmwareVersion(firmwareVersion);
		builder.setHardwareVersion(hardwareVersion);
		builder.setProperty(gNSSModelProperty.build());
		builder.setModule(communicationModule.build());
		Packet _out_packet = new Packet();
		_out_packet.setCommand(AllCommands.Terminal.QueryTerminalPropertyRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());
		//super.writeToDataProcessing(_out_packet);

      //  super.writeKafKaToDP(_out_packet, TopicConstants.POSRAW);
		result.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0107, LCResultCode.JTTerminal.SUCCESS));
		result.setKafkaPacket(_out_packet);
		return result;
		//return 0;
	}
public static byte[] filterBytes(byte[] arrays,int surfix){
	if(arrays==null||arrays.length==0){
		return arrays;
	}else{
		for(int i=arrays.length-1;i>=0;i--){
			if(arrays[i]==surfix){
				continue;
			}else{
				byte[] rbytes = new byte[i+1];
				System.arraycopy(arrays, 0, rbytes, 0, i+1);
				return rbytes;
			}
			
		}
	}
	return arrays;
	
}
   public static void main(String args[]) throws UnsupportedEncodingException {
	
	   String s="00073730313138474B2D31313052362D474300000000000000000000000000000000046002308320170000001168616C2D313130723667326D2D332E30371A63303232363030632D313130723667326D2D3730312D332E30370301";
	   
	 // byte [] bt=s.getBytes();
	   byte[] content = Convert.hexStringToBytes(s);

		//int serialNumber=TerminalPropertyAnswerCommandCache.getInstance().getAnswerEntry(packet.getUniqueMark(), AllCommands.Terminal.QueryTerminalProperty_VALUE, true);
		//终端类型
		int terminalType = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
		TerminalType.Builder type= TerminalType.newBuilder();
		if(terminalType>0){
			type.setIsSuitForPassengerVehicle((terminalType&1)==1?true:false);
			type.setIsSuitForDangerousGoods((terminalType&2)==2?true:false);
			type.setIsSuitForFreightTransport((terminalType&4)==4?true:false);
			type.setIsSuitForTaxi((terminalType&8)==8?true:false);
			type.setIsSuitDiskForVideo((terminalType&64)==64?true:false);
			type.setIntegratedTerminal((terminalType&128)==128?true:false);
	
		}
		
		//制造商ID
		String manufacturerId = Convert.bytesToHexString(ArraysUtils.subarrays(content, 2, 5));
		//终端型号
		String terminalModel = Convert.bytesToHexString(JT_0107_TerminalAttributeRes.filterBytes(ArraysUtils.subarrays(content, 7, 20),0x00));
		//终端ID
		String terminalIdentity = Convert.bytesToHexString(JT_0107_TerminalAttributeRes.filterBytes(ArraysUtils.subarrays(content, 27, 7),0x00));
		//终端SIM 卡ICCID
		String terminalICCID = new String(ArraysUtils.subarrays(content, 34, 10),"gbk");
		//终端硬件版本号长度
		int hardwareVersionLength = Convert.byte2Int(ArraysUtils.subarrays(content, 44, 1),1);
		//终端硬件版本号
		String hardwareVersion = new String(ArraysUtils.subarrays(content, 45, hardwareVersionLength));
		//终端固件版本号长度
		int firmwareVersionLength=Convert.byte2Int(ArraysUtils.subarrays(content, 45+hardwareVersionLength, 1),1);
		//终端固件版本号
		String firmwareVersion = new String(ArraysUtils.subarrays(content, 46+hardwareVersionLength, firmwareVersionLength));
		// 
		int GNSSModel= Convert.byte2Int(ArraysUtils.subarrays(content,46+hardwareVersionLength+firmwareVersionLength, 1),1);
       //GNSS模块属性
		GNSSModelProperty.Builder gNSSModelProperty=GNSSModelProperty.newBuilder();
		if(GNSSModel>0){
			//是否支持GPS定位 true：是；false：否
			gNSSModelProperty.setIsSupportGps((GNSSModel&1)==1?true:false);
			//是否支持北斗定位 true：是；false：否
			gNSSModelProperty.setIsSupportBeidou((GNSSModel&2)==2?true:false);
			//是否支持GLONASS定位 true：是；false：否
			gNSSModelProperty.setIsSupportGlonass((GNSSModel&4)==4?true:false);
			//是否支持Galileo定位  true：是；false：否
			gNSSModelProperty.setIsSupportGalileo((GNSSModel&8)==2?true:false);
	
			
		}
		int CommunicationModuleValue=Convert.byte2Int(ArraysUtils.subarrays(content, 47+hardwareVersionLength+firmwareVersionLength, 1),1);
		//通信模块属性
		CommunicationModule.Builder communicationModule=CommunicationModule.newBuilder(); 
     if(CommunicationModuleValue>0){
		// isSupportGPRS 是否支持GPRS通信 true：是；false：否
   	  communicationModule.setIsSupportGPRS((CommunicationModuleValue&1)==1?true:false);
		// isSupportCDMA 是否支持CDMA通信 true：是；false：否
   	  communicationModule.setIsSupportCDMA((CommunicationModuleValue&2)==2?true:false);
		// isSupportTD_SCDMA 是否支持TD-SCDMA通信 true：是；false：否
   	  communicationModule.setIsSupportTDSCDMA((CommunicationModuleValue&4)==4?true:false);
		// isSupportWCDMA 是否支持WCDMA通信 true：是；false：否
   	  communicationModule.setIsSupportWCDMA((CommunicationModuleValue&8)==8?true:false);
		// isSupportCDMA2000 是否支持CDMA2000通信 true：是；false：否
   	  communicationModule.setIsSupportCDMA2000((CommunicationModuleValue&16)==16?true:false);
		// isSupportTD_LTE 是否支持TD-LTE通信true：是；false：否
   	  communicationModule.setIsSupportTDLTE((CommunicationModuleValue&32)==32?true:false);
		// isSupportOther 是否支持其他通信true：是；false：否
   	  communicationModule.setIsSupportOther((CommunicationModuleValue&128)==128?true:false);
     }
	
	QueryTerminalPropertyRes.Builder builder = QueryTerminalPropertyRes.newBuilder();
	builder.setSerialNumber(01);
	builder.setResult(LCResponseResult.ResponseResult.success);
	builder.setType(type);
	builder.setManufacturerId(manufacturerId);
	builder.setTerminalModel(terminalModel);
	builder.setTerminalIdentity(terminalIdentity);
	builder.setTerminalICCID(terminalICCID);
	builder.setFirmwareVersion(firmwareVersion);
	builder.setHardwareVersion(hardwareVersion);
	builder.setProperty(gNSSModelProperty.build());
	builder.setModule(communicationModule.build());
	Packet _out_packet = new Packet();
	_out_packet.setCommand(AllCommands.Terminal.QueryTerminalPropertyRes_VALUE);
	_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
	
	_out_packet.setContent(builder.build().toByteArray());
}
}
