package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;
import java.util.Map;

import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBInfo;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCGetTerminalInfos;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCGetTerminalInfosRes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

@DaRmiNo(id = "0900")
public class Mutual_0900_GetTerminalInfos extends Dacommand {
	final static TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();
	private final static Logger log = LoggerFactory.getLogger(Mutual_0900_GetTerminalInfos.class);
	@Override
	public Packet processor(Packet packet) {
		try {
			//获取消息主体
			LCGetTerminalInfos.GetTerminalInfos gti = LCGetTerminalInfos.GetTerminalInfos.parseFrom(packet.getContent());
			//获取参数
//			long code=0l;
			int districtCode = gti.getDistrictCode().getNumber();

//			if(gti.hasNodeCode()){
//				code = gti.getNodeCode();
//			}
			//从数据库取数据
			List<Map<String, Object>> result=terminalInfoManage.getTerminalInfos(districtCode);
			//
			int count = 0;
			if(result != null){
				count = result.size();
			}
			log.info("区域编号districtCode="+districtCode+"，获取到终端数:"+count+" from:"+packet.getFrom());
			return buildPacket(packet ,result);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 构建packet
	 * @param list  <"terminalInfo",TerminalInfo>
	 * @return
	 */
	private Packet buildPacket(Packet packet,List<Map<String, Object>> list) {
		LCGetTerminalInfosRes.GetTerminalInfosRes.Builder list_builder=LCGetTerminalInfosRes.GetTerminalInfosRes.newBuilder();
		list_builder.setSerialNumber(packet.getSerialNumber());
		list_builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> obj:list){
				LCTerminalInfo.TerminalInfo.Builder obj_builder=LCTerminalInfo.TerminalInfo.newBuilder();
				LcTerminalInfoDBInfo terminal=(LcTerminalInfoDBInfo)obj.get("terminalInfo");//终端信息
//node_code已删除
//				if(terminal.getNode_code() == null){
//				}else{
//					obj_builder.setNodeCode(terminal.getNode_code().longValue());
//				}
				if(terminal.getTerminal_id() != null)
					obj_builder.setTerminalId(terminal.getTerminal_id());
				if(terminal.getProto_code() != null)
					obj_builder.setProtocolType(terminal.getProto_code());
				if(terminal.getRegular_in_terminal() != null)
					obj_builder.setRegularInTerminal(terminal.getRegular_in_terminal()==0?false:true);
				if(terminal.getAuth_code()!=null){
					obj_builder.setAuthCode(terminal.getAuth_code());
				}
				if(terminal.getDevice_id() != null) {
					obj_builder.setDeviceId(terminal.getDevice_id());
				}
				obj_builder.setChangeTid(terminal.getChange_tid());
				obj_builder.setType(LCTerminalInfo.BusinessType.valueOf(terminal.getBusiness_type()));
				list_builder.addTerminals(obj_builder.build());
			}
		}

		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(LCAllCommands.AllCommands.DataAccess.GetTerminalInfosRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setTo(packet.getFrom());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		_out_packet.setContent(list_builder.build().toByteArray());
		return _out_packet;
	}
	public static void main(String [] ages){
		int districtCode = 132700;
		List<Map<String, Object>> result=terminalInfoManage.getTerminalInfos(districtCode);
		log.info("区域编号districtCode="+districtCode+" from:"+result.get(0).get("terminalInfo").toString());
	}
}