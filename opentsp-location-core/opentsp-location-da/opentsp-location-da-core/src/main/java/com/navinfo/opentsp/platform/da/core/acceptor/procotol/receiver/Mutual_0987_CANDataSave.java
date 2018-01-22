package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCCANDataSave.CANDataSave;
import  com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport.CANBUSDataReport;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACanDataDetail;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.CanDataMongodbService;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.CanDataMongodbServiceImpl;
@DaRmiNo(id = "0987")
public class Mutual_0987_CANDataSave extends Dacommand {
	private TermianlDynamicManage terminalDynamicManage = new TermianlDynamicManageImpl();
	private CanDataMongodbService canMongo = new CanDataMongodbServiceImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			CANDataSave canData = CANDataSave.parseFrom(packet.getContent());
			long terminalId = canData.getTerminalId();
			int type = canData.getType();
			List<CANBUSDataReport> datasList = canData.getDatasList();
			if(2==type){
				//主动汇报数据  保存
				CANBUSDataReport can = datasList.get(0);
				terminalDynamicManage.saveCanData(terminalId, can);
			}else if(1 == type){
				//查询数据 直接保存到mongo中
				CanDataEntity entity = new CanDataEntity();
				entity.settId(terminalId);
				entity.setTime(datasList.get(0).getReportDate());
				List<DACanDataDetail> entities = new ArrayList<DACanDataDetail>();
				for(CANBUSDataReport report : datasList){
					DACanDataDetail detail = new DACanDataDetail();
					long reportDate = report.getReportDate();
					detail.setTime(reportDate);
					detail.setData(report.toByteArray());
					entities.add(detail);
				}
				entity.setDataList(entities);
				try {
					MongoManager.start(LCMongo.DB.LC_GPS_CANDATA,LCMongo.Collection.LC_CANDATA);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean saveCanData = canMongo.saveCanData(entity);
				if(!saveCanData){
					logger.error("save CanData failed.");
				}
				MongoManager.close();
			}else{
				logger.error("type error");
			}
			super.commonResponsesForPlatform(packet.getFrom(), 
					packet.getSerialNumber(), packet.getCommand(),
					PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

}
