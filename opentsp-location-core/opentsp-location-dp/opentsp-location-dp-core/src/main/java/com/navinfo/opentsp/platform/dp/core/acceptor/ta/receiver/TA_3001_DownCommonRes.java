//package com.navinfo.opentsp.platform.dp.core.acceptor.receiver;
//
//import com.google.protobuf.ByteString;
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
//import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
//import com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTerminalOperateLogUpdate.TerminalOperateLogUpdate;
//import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCParameterSave.ParameterSave;
//import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes.DownCommonRes;
//import com.lc.dp.acceptor.tal.protocol.TACommand;
//import com.navinfo.opentsp.platform.dp.core.cache.InternalCache;
//import com.navinfo.opentsp.platform.dp.core.cache.TerminalParameterCache;
//import com.navinfo.opentsp.platform.dp.core.cache.entity.SettingParameterEntry;
//import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
//import com.navinfo.opentsp.platform.location.kit.Convert;
//import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class TA_3001_DownCommonRes extends TACommand {
//	/**终端参数设置指令集合*/
//	static Set<Integer> paramterSetiingCommands = new HashSet<Integer>();
//	static{
//		paramterSetiingCommands.add(AllCommands.Terminal.MessageTimeoutProcess_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.ConnectServerConfig_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.ReportTacticsAndInterval_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.PrivilegeNumbers_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.AlarmTriggeredSetting_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.SpeedingAlarmSetting_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.SpeedingAlarmCancel_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.FatigueDrivingSetting_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.FatigueDrivingCancel_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.MultiMediaParameter_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.VehicleInfoSetting_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.RoadTransportPermit_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.CollisionAlarm_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.RolloverAlarm_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.TakePictureControl_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.GnssSetting_VALUE);
//		paramterSetiingCommands.add(AllCommands.Terminal.CanBusSetting_VALUE);
//
//	}
//	/**终端操作指令*/
//	static Set<Integer> terminalOperateCommands = new HashSet<Integer>();
//	static{
//		terminalOperateCommands.addAll(paramterSetiingCommands);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalRegister_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalRegisterRes_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalAuth_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalLogout_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.DriverInfoReport_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalOnlineSwitch_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.WaybillReport_VALUE);
//
//		terminalOperateCommands.add(AllCommands.Terminal.DispatchMessage_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.CallListener_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TakePhotography_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.MultimediaUpload_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.OilCircuitControl_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.VehicleControl_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.EventSetting_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.EventReport_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.AskQuestion_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.AnswerQuestion_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.InfoDemandMenu_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.InfoDemandOrCancel_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.InfoService_VALUE);
//
//		terminalOperateCommands.add(AllCommands.Terminal.PhoneBookSetting_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TemporaryLocationControl_VALUE);
//
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalShutdown_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalReset_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalRestoreFactory_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalCloseGPRS_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalCloseAllWireless_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.WirelessUpdate_VALUE);
//		terminalOperateCommands.add(AllCommands.Terminal.ConnectToServer_VALUE);
//
//		terminalOperateCommands.add(AllCommands.Terminal.TerminalStatusControl_VALUE);
//	}
//	@Override
//	public int processor(Packet packet) {
//		try {
//			DownCommonRes commonRes = DownCommonRes.parseFrom(packet.getContent());
//			//当应答指令为终端参数设置类型,如果成功.需要获取设置参数存储到数据库
//			if(paramterSetiingCommands.contains(commonRes.getResponseId())){
//				//成功存储
//				if(commonRes.getResult().getNumber() == ResponseResult.success_VALUE){
//					SettingParameterEntry cacheEntity = TerminalParameterCache.getInstance().get(packet.getUniqueMark(), commonRes.getSerialNumber() , true);
//					if(cacheEntity != null){
//						this.saveParameter(cacheEntity);
//					}
//				}
//			}
//			//需要检查在何时加入此缓存数据
//			InternalCache.getInstance().delete(packet.getUniqueMark()+"_"+commonRes.getSerialNumber());
//			//更新终端操作日志
//			if(terminalOperateCommands.contains(commonRes.getResponseId())){
//				if(commonRes.getResult().getNumber() == ResponseResult.success_VALUE){
//					this.updateTerminalOperateLog(Convert.uniqueMarkToLong(packet.getUniqueMark()), commonRes.getResponseId());
//				}
//			}
//		} catch (InvalidProtocolBufferException e) {
//			e.printStackTrace();
//		}
//		super.writeToRequestProcessing(packet);
//		return 0;
//	}
//	/**
//	 * 将终端参数发送DA存储
//	 * @param packet
//	 */
//	private void saveParameter(SettingParameterEntry entry){
//		ParameterSave.Builder builder = ParameterSave.newBuilder();
//		builder.setCommandCode(entry.getCommandCode());
//		ByteString byteString = ByteString.copyFrom(entry.getParaContent());
//		builder.setParaContent(byteString);
//		builder.setTerminalId(entry.getTerminalId());
//
//		Packet _out_packet = new Packet(true);
//		_out_packet.setCommand(AllCommands.DataAccess.ParameterSave_VALUE);
//		_out_packet.setProtocol(LCMessageType.PLATFORM);
//		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
//		_out_packet.setContent(builder.build().toByteArray());
//		super.writeToDataAccess(_out_packet);
//	}
//	/**
//	 * 更新操作日志
//	 * @param terminalId
//	 * @param terminalOperation
//	 */
//	private void updateTerminalOperateLog(long terminalId , int operateCmd){
//		TerminalOperateLogUpdate.Builder builder = TerminalOperateLogUpdate.newBuilder();
//		builder.setTerminalId(terminalId);
//		builder.setFunctionCode(operateCmd);
//		builder.setResults(true);
//
//		Packet _out_packet = new Packet(true);
//		_out_packet.setCommand(AllCommands.DataAccess.TerminalOperateLogUpdate_VALUE);
//		_out_packet.setProtocol(LCMessageType.PLATFORM);
//		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
//		_out_packet.setContent(builder.build().toByteArray());
//		super.writeToDataAccess(_out_packet);
//	}
//}
