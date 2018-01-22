package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMultimediaDataSave.MultimediaDataSave;
import  com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMultimediaUpload.MultimediaUpload;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.PictureEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMultimediaParaDBEntity;
@DaRmiNo(id = "0981")
public class Mutual_0981_MultimediaDataSave extends Dacommand {
	final static TermianlDynamicManage terminalDynamicManage = new TermianlDynamicManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			MultimediaDataSave multimediaDataSave = MultimediaDataSave
					.parseFrom(packet.getContent());
			long terminalId = multimediaDataSave.getTerminalId();
			MultimediaUpload multimediaUpload = multimediaDataSave
					.getMultimediaData();// 多媒体数据
			LcMultimediaParaDBEntity multimediaTopic = new LcMultimediaParaDBEntity();
			multimediaTopic.setChannels(multimediaUpload.getChannels());
			multimediaTopic.setEncode(multimediaUpload.getEncode().getNumber());
			multimediaTopic.setEvents(multimediaUpload.getEvents().getNumber());

			// 当前系统时间
			Long currentTime = System.currentTimeMillis() / 1000;

			multimediaTopic.setMedia_date(new Long(currentTime).intValue());
			multimediaTopic.setMedia_id(new Long(multimediaUpload.getMediaId())
					.intValue());
			multimediaTopic.setTerminal_id(terminalId);
			multimediaTopic.setType(multimediaUpload.getTypes().getNumber());
			String fileName=getFileName(currentTime, terminalId, multimediaUpload
					.getTypes().getNumber());
			multimediaTopic.setFile_id(fileName);
			PictureEntity pictureEntity = new PictureEntity();
			pictureEntity
					.setData(multimediaUpload.getMediaData().toByteArray());
			// 当前系统时间
			pictureEntity.setFileName(fileName);
			pictureEntity.setSaveDate(currentTime);
			pictureEntity.settId(terminalId);
			// multimediaUpload.get
			terminalDynamicManage.savePictureData(terminalId, multimediaTopic,
					pictureEntity);
			super.commonResponsesForPlatform(packet.getFrom(),
					packet.getSerialNumber(), packet.getCommand(),
					PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取媒体存储文件名称
	 * @param currentTime
	 *            当前系统时间
	 * @param terminalId
	 *            终端编号
	 * @param multimediaType
	 *            媒体文件类型
	 * @return
	 */
	private static String getFileName(Long currentTime, long terminalId,
			int multimediaType) {
		return terminalId + currentTime + multimediaType + "";
	}
}
