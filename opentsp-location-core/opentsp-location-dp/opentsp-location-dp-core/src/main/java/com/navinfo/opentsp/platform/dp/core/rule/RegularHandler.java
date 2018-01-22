package com.navinfo.opentsp.platform.dp.core.rule;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.da.send.kafka.DA_0980_LocationDataSave;
import com.navinfo.opentsp.platform.dp.core.cache.CallNameResCache;
import com.navinfo.opentsp.platform.dp.core.cache.LatestGpsDataCache;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.handler.SendKafkaHandler;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRegularHandler;
import com.navinfo.opentsp.platform.dp.core.rule.handler.correctgps.GpsTransform;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularHandler;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCShortLocationData.ShortLocationData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLocationDataSave.LocationDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCCallNameRes.CallNameRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegularHandler {

	private static final Logger log = LoggerFactory.getLogger(RegularHandler.class);
	private static long num = 0l;
	private static long single = 0l;
	private static long common = 0l;
	private static long save = 0l;
	private static long total = 0l;

	@Autowired
	CommonRegularHandler commonRegularHandler;

	@Autowired
	SingleRegularHandler singleRegularHandler;

	@Autowired
	private SendKafkaHandler sendKafkaHandler;

	@Autowired
	private DA_0980_LocationDataSave da_0980_LocationDataSave;

	/**
	 * 规则处理
	 */
	public void handler(final GpsLocationDataEntity dataEntity) {
		num++;
		long s = System.currentTimeMillis();
		long s1 = s;
		// 纠偏处理
		int[] dataEncrypt = GpsTransform.transform(((double) dataEntity.getOriginalLng()) / 1000000, ((double) dataEntity.getOriginalLat()) / 1000000);
		dataEntity.setLongitude(dataEncrypt[0]);
		dataEntity.setLatitude(dataEncrypt[1]);
		//构建精简位置数据,向RP转发
//		forwardShortLocationData(dataEntity);


		//个性规则运算
		singleRegularHandler.process(dataEntity);
		single = single + (System.currentTimeMillis() - s);
		if (num % 1000 == 0) {
			log.error("{} 条位置数据--数据偏移和专用规则处理时间:{} ms", num, single);
			single = 0;
		}
		dataEntity.setRuleSignTime(System.currentTimeMillis()-s);

		//通用规则运算
		s = System.currentTimeMillis();
		commonRegularHandler.process(dataEntity);
		common = common + (System.currentTimeMillis() - s);
		if (num % 1000 == 0) {
			log.error("{} 条位置数据--通用规则处理时间:{} ms", num, common);
			common = 0;
		}
		dataEntity.setRuleProcessTime(System.currentTimeMillis()-s);

		//转发3002
		s = System.currentTimeMillis();
		forward(dataEntity);
		save = save + (System.currentTimeMillis() - s);
		total = total + (System.currentTimeMillis() - s1);
		dataEntity.setForwardTime(System.currentTimeMillis()-s);
		if (num % 1000 == 0) {
			log.error("{} 条位置数据--存储转发时间:{} ms", num, save);
			log.error("{} 条位置数据--总处理时间时间:{} ms", num, total);
			save = 0;
			total = 0;
		}
		dataEntity.setEncryptTime(System.currentTimeMillis() - s1);
	}


	/**
	 * 构建精简位置数据,向RP转发
	 * @param dataEntity
	 */
	private static void forwardShortLocationData(GpsLocationDataEntity dataEntity) {
		ShortLocationData shortLocationData = dataEntity.toShortLocationData(false);
		Packet _out_packet = new Packet();
		_out_packet.setSerialNumber(dataEntity.getSerialNumber());
		_out_packet.setCommand(AllCommands.Terminal.ShortLocationDataReport_VALUE);
		_out_packet.setContent(shortLocationData.toByteArray());
		_out_packet.setFrom(dataEntity.getTerminalId());
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		// TODO: 16/8/24
		//RPClusters.execute(_out_packet);
	}


	/**
	 * 转发数据
	 * @param dataEntity
	 * @throws InvalidProtocolBufferException
	 */
	private void forward(GpsLocationDataEntity dataEntity)  {
		// 数据来源,位置汇报
		if (dataEntity.getCommandId() == AllCommands.Terminal.ReportLocationData_VALUE) {
			// 转发RP层
			Packet _out_packet_rp = new Packet();
			_out_packet_rp.setUniqueMark(dataEntity.getUniqueMark());
			_out_packet_rp.setFrom(dataEntity.getTerminalId());
			_out_packet_rp.setCommand(AllCommands.Terminal.ReportLocationData_VALUE);
			_out_packet_rp.setProtocol(LCConstant.LCMessageType.TERMINAL);
			_out_packet_rp.setSerialNumber(dataEntity.getSerialNumber());
			_out_packet_rp.setContent(dataEntity.toLocationData(true).toByteArray());
			// TODO: 16/8/24 RP转发
			//RPClusters.execute(_out_packet_rp);
			sendKafkaHandler.writeKafKaToRP(_out_packet_rp);

			// 转发DA层存储
			LocationDataSave.Builder builder = LocationDataSave.newBuilder();
			builder.setTerminalId(Convert.uniqueMarkToLong(dataEntity.getUniqueMark()));
			builder.setLocationData(dataEntity.toLocationData(true));
			Packet _out_packet_da = new Packet(true);
			_out_packet_da.setUniqueMark(dataEntity.getUniqueMark());
			_out_packet_da.setCommand(AllCommands.DataAccess.LocationDataSave_VALUE);
			_out_packet_da.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet_da.setContent(builder.build().toByteArray());
			// TODO: 16/8/24 to DA
//			sendKafkaHandler.writeKafKaToDA(_out_packet_da);
			da_0980_LocationDataSave.processor(_out_packet_da);
			//DAMutualCommandFacotry.processor(_out_packet_da);
		} else if (dataEntity.getCommandId() == AllCommands.Terminal.CallNameRes_VALUE) {
			// 需要通过一致性hash算法,将终端数据分布到上层RP节点
			CallNameRes.Builder builder = CallNameRes.newBuilder();
			builder.setResult(ResponseResult.success);
			builder.setSerialNumber(CallNameResCache.getCallNameRes(
					dataEntity.getTerminalId(), dataEntity.getSerialNumber(),
					true));
			builder.setData(dataEntity.toLocationData(false));

			Packet pk = new Packet(true);
			pk.setUniqueMark(dataEntity.getUniqueMark());
			pk.setFrom(dataEntity.getTerminalId());
			pk.setCommand(AllCommands.Terminal.CallNameRes_VALUE);
			pk.setProtocol(LCConstant.LCMessageType.TERMINAL);
			pk.setSerialNumber(dataEntity.getSerialNumber());
			pk.setContent(builder.build().toByteArray());
			// TODO: 16/8/24
			//RPClusters.execute(pk);
			sendKafkaHandler.writeKafKaToRP(pk);
		}
		// 缓存最新数据
		LatestGpsDataCache.getInstance().add(dataEntity);
		if(dataEntity.getGpsDate()>=LatestGpsDataCache.beginDate && dataEntity.getGpsDate()<=LatestGpsDataCache.endDate){
			LatestGpsDataCache.currentTimes++;
		}
	}
}

