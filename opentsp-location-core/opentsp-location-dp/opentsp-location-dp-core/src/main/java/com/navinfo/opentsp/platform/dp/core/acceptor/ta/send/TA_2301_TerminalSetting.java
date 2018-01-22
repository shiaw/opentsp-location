//package com.navinfo.opentsp.platform.dp.core.acceptor.send;
//
//import com.lc.dp.acceptor.tal.protocol.TACommand;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//
//public class TA_2301_TerminalSetting extends TACommand {
//	//终端参数设置
//	//1、缓存此设置到内部缓存(Key：唯一标识_流水号)
//	//2、转发指令到TA
//	//3、等待应答,当应答成功,从缓存中获取数据进行存储
//
//	@Override
//	public int processor(Packet packet) {
////		InternalCacheEntity cacheEntity = new InternalCacheEntity();
////		cacheEntity.setObject(packet);
////		cacheEntity.setTimeout(Configuration.getInt("dp_terminalSettingThreshold"));
////		InternalCache.getInstance().add(packet.getUniqueMark()+"_"+packet.getSerialNumber(), cacheEntity);
//		super.write(packet);
//		return 0;
//	}
//
//}
