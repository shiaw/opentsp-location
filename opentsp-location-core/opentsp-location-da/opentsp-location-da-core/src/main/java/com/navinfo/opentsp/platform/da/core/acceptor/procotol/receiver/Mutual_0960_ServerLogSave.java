package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.LogManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceLogDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCServerLogSave;

@DaRmiNo(id = "0960")
public class Mutual_0960_ServerLogSave extends Dacommand {
	final static LogManage logManage=new LogManageImpl();
	@Override
	public Packet processor(Packet packet) {
		try {
			LCServerLogSave.ServerLogSave serverLogSave= LCServerLogSave.ServerLogSave.parseFrom(packet.getContent());
			LCServerType.ServerType serverType=serverLogSave.getTypes();
			if(serverType.getNumber()==0x01){
				LcServiceLogDBEntity lcServiceLogDBEntity=new LcServiceLogDBEntity();
				lcServiceLogDBEntity.setLog_district(serverLogSave.getDistrictCode().getNumber());
				lcServiceLogDBEntity.setLog_content(serverLogSave.getLogContent());
				lcServiceLogDBEntity.setLog_time((int)serverLogSave.getLogDate());
				lcServiceLogDBEntity.setLog_ip(serverLogSave.getServerCode());
				lcServiceLogDBEntity.setLog_typecode(serverLogSave.getTypeCoding());
				logManage.serverLogSave(lcServiceLogDBEntity);
				
			}else {
				LcNodeLogDBEntity lcNodeLogDBEntity=new LcNodeLogDBEntity();
				lcNodeLogDBEntity.setLog_district(serverLogSave.getDistrictCode().getNumber());
				lcNodeLogDBEntity.setLog_content(serverLogSave.getLogContent());
				lcNodeLogDBEntity.setLog_time((int)serverLogSave.getLogDate());
				lcNodeLogDBEntity.setLog_ip(serverLogSave.getServerCode());
				lcNodeLogDBEntity.setLog_typecode(serverLogSave.getTypeCoding());
				lcNodeLogDBEntity.setNode_code(Integer.parseInt(serverLogSave.getServerCode()));
				logManage.nodeLogSave(lcNodeLogDBEntity);
			}
			super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
