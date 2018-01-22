package com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.TerminalEntity;
import com.navinfo.opentsp.platform.dp.core.common.RmiConstant;
import com.navinfo.opentsp.platform.dp.core.rmi.DaRmiService;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCGetTerminalInfosRes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 查询终端信息应答<br>
 * 收到应答后,终端列表不为空情况下.向DA分别发送获取区域列表请求以及获取规则列表请求
 *
 * @author ysh
 *
 */
@Deprecated
@Component(value = "da_1900_GetTerminalInfosRes")
public class DA_0900_GetTerminalInfosRes extends DACommand {

	@Resource
	private TerminalCache terminalCache;

	@Autowired
	private DaRmiService daRmiService;// rmi调用通用类

	@Autowired
	private DA_0932_QueryRegularDataRes dA_1932_QueryRegularDataRes;// 规则数据

	@Autowired
	private DA_0930_QueryAreaInfoRes dA_1930_QueryAreaInfoRes;// 区域数据

	@Value("${district.code}")
	public int districtCode;

	@Override
	public int processor(Packet packet) {
		try {

			LCGetTerminalInfosRes.GetTerminalInfosRes getTerminalInfos = LCGetTerminalInfosRes.GetTerminalInfosRes.parseFrom(packet.getContent());
			List<LCTerminalInfo.TerminalInfo> terminalInfos = getTerminalInfos.getTerminalsList();
			int size = terminalInfos.size();
			if (terminalInfos != null && size > 0) {
//				// 启动获取规则数据线程
//				queryRegularData();
////				Thread ruleThread = new Thread(new QueryRegularDataThread(terminalInfos.get(0).getNodeCode()));
////				ruleThread.start();
//				// 启动获取区域数据线程
//				queryAreaInfo();
//				Thread areaThread = new Thread(new QueryAreaInfoThread(terminalInfos.get(0).getNodeCode()));
//				areaThread.start();
				for (LCTerminalInfo.TerminalInfo terminalInfo : terminalInfos) {
					TerminalEntity entity = new TerminalEntity();
					entity.setTerminalId(terminalInfo.getTerminalId());
//					entity.setTerminalProtocolCode(terminalInfo.getProtocolType());
//					entity.setTaNodeCode(terminalInfo.getNodeCode());
//					entity.setAuthCode(terminalInfo.getAuthCode());
//					entity.setRegularInTerminal(terminalInfo.getRegularInTerminal());
					terminalCache.addTerminal(entity);
				}
				logger.info("加载DA-RMI(0900)获取终端信息数据成功!终端数量:"+size);
			}
//			DA_1100_ServerCommonRes.isLoadTerminal = true;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();

		}
		return 0;
	}
	/**
	 * 获取区域数据
	 *
	 * @return
	 */
	private void queryAreaInfo()
	{
		try
		{
			// 设置入参
			Packet packetIn = new Packet();
			packetIn.setCommand(LCAllCommands.AllCommands.DataAccess.QueryAreaInfo_VALUE);

			LCQueryAreaInfo.QueryAreaInfo.Builder builder = LCQueryAreaInfo.QueryAreaInfo.newBuilder();
			builder.setDistrictCode(LCDistrictCode.DistrictCode.valueOf(districtCode));
			builder.setNodeCode(0);

			packetIn.setContent(builder.build().toByteArray());

			Packet packet =
					daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
							RmiConstant.RMI_INTERFACE_NAME,
							RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
							packetIn);

			if (packet != null)
			{
				// 获取区域数据
				dA_1930_QueryAreaInfoRes.processor(packet);
			}
			else
			{
				logger.info("获取RMI终端区域数据失败！");
			}
		}
		catch (Exception e)
		{
			logger.error("获取终端区域数据异常" + e.getMessage());
		}
	}

	/**
	 * 获取规则数据
	 *
	 * @return
	 */
	private void queryRegularData()
	{
		try
		{
			// 设置入参
			Packet packetIn = new Packet();
			packetIn.setCommand(LCAllCommands.AllCommands.DataAccess.QueryRegularData_VALUE);

			LCQueryRegularData.QueryRegularData.Builder builder = LCQueryRegularData.QueryRegularData.newBuilder();
			builder.setDistrictCode(LCDistrictCode.DistrictCode.valueOf(districtCode));
			builder.setNodeCode(0);

			packetIn.setContent(builder.build().toByteArray());

			Packet packet =
					daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
							RmiConstant.RMI_INTERFACE_NAME,
							RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
							packetIn);

			if (packet != null)
			{
				// 获取规则数据
				dA_1932_QueryRegularDataRes.processor(packet);
			}
			else
			{
				logger.info("获取RMI终端规则数据失败！");
			}
		}
		catch (Exception e)
		{
			logger.error("获取终端规则数据异常" + e.getMessage());
		}
	}

//	class QueryAreaInfoThread extends DACommand implements Runnable {
//		private long taNodeCode;
//
//		public QueryAreaInfoThread(long taNodeCode) {
//			this.taNodeCode = taNodeCode;
//		}
//
//		@Override
//		public void run() {
//			// 获取区域数据
//			LCQueryAreaInfo.QueryAreaInfo.Builder builder = LCQueryAreaInfo.QueryAreaInfo.newBuilder();
//			builder.setDistrictCode(Constant.DISTRICT_CODE);
//			builder.setNodeCode(this.taNodeCode);
//
//			Packet _out_packet = new Packet(true);
//			_out_packet.setCommand(LCAllCommands.AllCommands.DataAccess.QueryAreaInfo_VALUE);
//			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
//			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
//			_out_packet.setContent(builder.build().toByteArray());
//			this.write(_out_packet);
//		}
//
//		public int processor(Packet packet) {
//			return 0;
//		}
//	}
//
//	class QueryRegularDataThread extends DACommand implements Runnable {
//		private long taNodeCode;
//
//		public QueryRegularDataThread(long taNodeCode) {
//			this.taNodeCode = taNodeCode;
//		}
//
//		@Override
//		public void run() {
//			// 获取区域规则数据
//			LCQueryRegularData.QueryRegularData.Builder builder = LCQueryRegularData.QueryRegularData.newBuilder();
//			builder.setDistrictCode(Constant.DISTRICT_CODE);
//			builder.setNodeCode(this.taNodeCode);
//
//			Packet _out_packet = new Packet(true);
//			_out_packet
//					.setCommand(LCAllCommands.AllCommands.DataAccess.QueryRegularData_VALUE);
//			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
//			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
//			_out_packet.setContent(builder.build().toByteArray());
//			this.write(_out_packet);
//		}
//
//		public int processor(Packet packet) {
//			return 0;
//		}
//	}

//	class HashTerminal implements Runnable {
//		private List<LCTerminalInfo.TerminalInfo> terminalInfos;
//
//		public HashTerminal(List<LCTerminalInfo.TerminalInfo> terminalInfos) {
//			this.terminalInfos = terminalInfos;
//		}
//
//		@Override
//		public void run() {
//
//			while (!RPNodeList.isNormal()) {
//				try {
//					System.err.println("有RP节点没有连接上DP,现在等待5秒执行!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//					Thread.sleep(1000 * 5);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//
//			System.err.println("所有的RP节点都连接上DP成功,现在给RP广播终端信息!!!!!!!!!!!!!  terminalInfos.size():"+terminalInfos.size());
//			Packet _out_packet = new Packet(true);
//			_out_packet
//					.setCommand(LCAllCommands.AllCommands.NodeCluster.DPNodeTerminalInfo_VALUE);
//			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
//			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
//			_out_packet.setFrom(NodeHelper.getNodeCode());
//			// 这种方式，只判断DP节点上的 RP集群 有一个RP成功连接，则为 normal，现在修改为DP上的RP集群构建完毕才为Normal
//			/*
//			 * if(!RPClusters.isNormal()){ return ; }
//			 */
//			LCDPNodeTerminalInfo.DPNodeTerminalInfo.Builder builder = LCDPNodeTerminalInfo.DPNodeTerminalInfo.newBuilder();
//			for (LCTerminalInfo.TerminalInfo terminalInfo : terminalInfos) {
//				LCTerminalRPHashInfo.TerminalRPHashInfo.Builder hashBuilder = LCTerminalRPHashInfo.TerminalRPHashInfo
//						.newBuilder();
////				Node rp = RPClusters.getNode(terminalInfo.getTerminalId());
////				if (rp != null) {
////					hashBuilder.setRpNodeCode(rp.getNodeCode());
////					hashBuilder.setInfo(terminalInfo);
////					builder.addRpHashInfo(hashBuilder);
////				}
//			}
//			_out_packet.setContent(builder.build().toByteArray());
////			RPClusters.broadcast(_out_packet);
//			System.err.println("所有的RP节点都连接上DP成功,现在给RP广播终端信息22222222222222!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//		}
//	}
}
