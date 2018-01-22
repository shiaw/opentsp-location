package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCDrivingBehaviorSetting;
@LocationCommand(id = "2270")
public class DP_2270_DrivingBehaviorSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		// 缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.DrivingBehaviorSetting_VALUE);// 2270
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);// 8103
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCDrivingBehaviorSetting.DrivingBehaviorSetting drivingBehaviorSetting = LCDrivingBehaviorSetting.DrivingBehaviorSetting
					.parseFrom(packet.getContent());
			int count = 0;//统计本次设置多少项内容
			BytesArray bytesArray = new BytesArray();
			BytesArray bytesArrayFinal = new BytesArray();
			// bytesArray.append(Convert.intTobytes(11, 1));//11个

			if (drivingBehaviorSetting.hasAcceleration()) {
				bytesArray.append(Convert.intTobytes(0XF001, 4));// 急加速阀值，单位m/s
				bytesArray.append(Convert.intTobytes(2, 1));
				bytesArray.append(Convert.intTobytes(drivingBehaviorSetting.getAcceleration(), 2));
				count++;
			}
			if (drivingBehaviorSetting.hasDeceleration()) {
				bytesArray.append(Convert.intTobytes(0XF002, 4));// 急减速阀值，单位m/s
				bytesArray.append(Convert.intTobytes(2, 1));
				bytesArray.append(Convert.intTobytes(drivingBehaviorSetting.getDeceleration(), 2));
				count++;
			}
			if (drivingBehaviorSetting.hasSharpTurning()) {
				bytesArray.append(Convert.intTobytes(0XF004, 4));// 急转弯参数阀值，单位度
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getSharpTurning(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasLowOil()) {
				bytesArray.append(Convert.intTobytes(0XF005, 4));// 低油量阀值参数
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getLowOil(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasSlideNeutral()) {
				bytesArray.append(Convert.intTobytes(0XF006, 4));// 空挡滑行时间参数
				// 单位为秒（S）
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getSlideNeutral(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasRpmThreshold()) {
				bytesArray.append(Convert.intTobytes(0XF007, 4));// 发动机转速阀值 单位为转
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getRpmThreshold(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasThrottle()) {
				bytesArray.append(Convert.intTobytes(0XF008, 4));// 油门位置阀值 单位为%
				// /位
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getThrottle(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasDisparitySpeed()) {
				bytesArray.append(Convert.intTobytes(0XF009, 4));// 车速差值阀值 单位为
				// m/s
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getDisparitySpeed(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasBrakePressure()) {
				bytesArray.append(Convert.intTobytes(0XF00A, 4));// 制动气压阀值
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getBrakePressure(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasLongTimeBreaking()) {
				bytesArray.append(Convert.intTobytes(0XF00B, 4));// 长时间刹车时间阀值
				// 单位为秒（s）
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getLongTimeBreaking(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasLongTimeClutch()) {
				bytesArray.append(Convert.intTobytes(0XF00C, 4)); // 长时间离合时间阀值
				// 单位为秒（s）
				bytesArray.append(Convert.intTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getLongTimeClutch(), 4));
				count++;
			}

			if (drivingBehaviorSetting.hasParkedTimke()) {
				bytesArray.append(Convert.intTobytes(0XF014, 4));// 停车立即熄火s
				bytesArray.append(Convert.intTobytes(2, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getParkedTimke(), 2));
				count++;
			}

			if (drivingBehaviorSetting.hasColdBootTime()) {
				bytesArray.append(Convert.intTobytes(0XF015, 4));// 发动机冷启动 																	// 单位为秒（s）
				bytesArray.append(Convert.intTobytes(2, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getColdBootTime(), 2));
				count++;
			}

			if (drivingBehaviorSetting.hasIdlingTime()) {
				bytesArray.append(Convert.intTobytes(0XF016, 4)); // 过长怠速
				// 单位为秒（s）
				bytesArray.append(Convert.intTobytes(2, 1));
				bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.getIdlingTime(), 2));
				count++;
			}
			/*
			 * bytesArray.append(Convert.intTobytes(0XF011,
			 * 4));//驾驶行为异常（报警）时汇报时间间隔，单位为秒（s），>0
			 * bytesArray.append(Convert.intTobytes(4, 1));
			 * bytesArray.append(Convert.longTobytes(drivingBehaviorSetting.
			 * getReportInterval(),4));
			 */
			bytesArrayFinal.append(Convert.intTobytes(count, 1));//计算总共有多少个
			bytesArrayFinal.append(bytesArray.get());
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArrayFinal.get());

			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
