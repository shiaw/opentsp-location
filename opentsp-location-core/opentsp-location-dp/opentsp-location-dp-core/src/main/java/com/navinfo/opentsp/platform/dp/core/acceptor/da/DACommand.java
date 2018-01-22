package com.navinfo.opentsp.platform.dp.core.acceptor.da;

import com.navinfo.opentsp.platform.dp.core.cache.DataAccessCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.DataAccessCacheEntry;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DACommand {

	public static Logger logger = LoggerFactory.getLogger(DACommand.class);
	//DA存储数据超时时间
	private static final int DATA_ACCESS_CACHE_TIME_OUT = 600;

	public int writeForCache(Packet packet){
		//收集发送给DA存储的数据
		//1、当DA存储失败,重新存储;2、当收到DA成功应答,向上层模块反馈数据
		DataAccessCacheEntry cacheEntry = new DataAccessCacheEntry();
		cacheEntry.setObject(packet);
		cacheEntry.setTimeout(DATA_ACCESS_CACHE_TIME_OUT);
		cacheEntry.setObjectType(packet.getCommand());
		DataAccessCache.getInstance().addDataAccessCacheEntry(packet.getUniqueMark(),packet.getCommand(), packet.getSerialNumber(), cacheEntry);
		return this.write(packet);
	}

	public int write(Packet packet) {
		// TODO 方案rmi?
//		DalMutualSession mutualSession = DalMutualSessionManage.getInstance().getActiveSession();
//		if(mutualSession != null){
//			IoSession ioSession = mutualSession.getIoSession();
//			if (ioSession != null && !ioSession.isClosing()) {
//				ioSession.write(packet);
//			} else {
//				logger.error("DP-DA链路关闭,发送数据失败.");
//				// 链路断开,将发送数据存入分布式缓存
//				// DistributedCache.getInstance().writeArrays(Constant.CacheKey.PassUpData,
//				// packet, 0);
//			}
//		}else{
//			logger.error("DP-DA链路未找到,发送数据失败.");
//		}
		return 0;
	}
	public int commonResponses(String uniqueMark, int responsesSerialNumber,
							   int responsesId, LCPlatformResponseResult.PlatformResponseResult result , long to) {
		Packet outPacket = new Packet(true);
		LCServerCommonRes.ServerCommonRes.Builder builder = LCServerCommonRes.ServerCommonRes.newBuilder();
		builder.setSerialNumber(responsesSerialNumber);
		builder.setResponseId(responsesId);
		builder.setResults(result);

		outPacket.setUniqueMark(uniqueMark);
		outPacket.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
		outPacket.setProtocol(LCConstant.LCMessageType.PLATFORM);
		outPacket.setContent(builder.build().toByteArray());
		outPacket.setUniqueMark(uniqueMark);
		outPacket.setTo(to);
		return this.write(outPacket);
	}


	/**
	 * 包指令处理
	 * @param packet
	 * @return
	 */
	public abstract int processor(Packet packet);
}
