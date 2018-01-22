package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCVehicleInfoSetting;


import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2261")
public class DP_2261_VehicleInfoSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.VehicleInfoSetting_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			LCVehicleInfoSetting.VehicleInfoSetting vehicleInfoSetting = LCVehicleInfoSetting.VehicleInfoSetting.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			//mileageValues	车辆里程表读数，1/10km	
			if(vehicleInfoSetting.hasMileageValues()){
				bytesArray.append(Convert.longTobytes(0x80, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(vehicleInfoSetting.getMileageValues()/100, 4));
				pnumber ++;
			}
			//provinceIdentify	车辆所在的省域ID
			if(vehicleInfoSetting.hasProvinceIdentify()){
				bytesArray.append(Convert.longTobytes(0x81, 4));
				bytesArray.append(Convert.longTobytes(2, 1));
				bytesArray.append(Convert.longTobytes(vehicleInfoSetting.getProvinceIdentify(), 2));
				pnumber ++;
			}
			//cityIdentify	车辆所在的市域ID
			if(vehicleInfoSetting.hasCityIdentify()){
				bytesArray.append(Convert.longTobytes(0x82, 4));
				bytesArray.append(Convert.longTobytes(2, 1));
				bytesArray.append(Convert.longTobytes(vehicleInfoSetting.getCityIdentify(), 2));
				pnumber ++;
			}
			//vehicleLicense	公安交通管理部门颁发的机动车号牌
			if(vehicleInfoSetting.hasVehicleLicense()){
				bytesArray.append(Convert.longTobytes(0x83, 4));
				byte[] vehicleLicense = vehicleInfoSetting.getVehicleLicense().getBytes("GBK");
				bytesArray.append(Convert.longTobytes(vehicleLicense.length, 1));
				bytesArray.append(vehicleLicense);
				pnumber ++;
			}
			//licenseColor	车牌颜色按照JT/T415-2006中5.4.12的规定
			if(vehicleInfoSetting.hasLicenseColor()){
				bytesArray.append(Convert.longTobytes(0x84, 4));
				bytesArray.append(Convert.longTobytes(1, 1));
				bytesArray.append(Convert.longTobytes(vehicleInfoSetting.getLicenseColor(), 1));
				pnumber ++;
			}
			
			byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
			
			packet.setCommand(Constant.JTProtocol.TerminalSetting);
			packet.setContent(bytes);
			//return super.writeToTerminal(packet);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(packet);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
