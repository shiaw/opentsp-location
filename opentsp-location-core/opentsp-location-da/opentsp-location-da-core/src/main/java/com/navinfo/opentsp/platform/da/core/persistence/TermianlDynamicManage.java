package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.MediaFileModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDrivingRecorderEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsTransferEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.PictureEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMultimediaParaDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport.CANBUSDataReport;

import java.util.List;
import java.util.Map;



public interface TermianlDynamicManage {
	/**
	 * 存储Gps位置数据<br>
	 * 一次存储某个终端一天的数据
	 *
	 * @param terminalId
	 *            终端标识
	 * @param locationData
	 *            {@link LocationData} 位置数据
	 */
	abstract void saveGpsData(long terminalId, LocationData locationData);

	abstract void saveCanData(long terminalId, CANBUSDataReport canData);

	/**
	 * 查询某终端的历史位置数据
	 *
	 * @param terminalId
	 *            终端标识
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return List<GpsDetailedEntity> 数据集合
	 */
	abstract List<GpsDetailedEntityDB> queryGpsData(long terminalId, long beginTime, long endTime);

	public Map<String, List<GpsDetailedEntityDB>> queryGpsData(List<Long> terminalIds, long beginTime, long endTime);

	/**
	 * 存储链路异常数据 里层key加nodeCodeTo
	 *
	 * @param nodeCode
	 *            {@link Integer} 节点编号
	 * @param nodeCodeTo
	 *            {@link Integer} 目标节点编码
	 * @param dataType
	 *            {@link Integer} 数据类型
	 * @param dataValue
	 *            {@link Byte}[] 数据内容
	 */
	abstract void saveLinkExceptionData(long nodeCode, long nodeCodeTo, int dataType, byte[] dataValue);

	/**
	 * 查询链路异常数据
	 *
	 * @param nodeCode
	 *            {@link Integer} 节点编号
	 * @param dataType
	 *            {@link Integer} 数据类型
	 * @param size
	 *            {@link Integer} 请求数据条数
	 * @return map<>
	 */
	abstract List<byte[]> queryLinkExceptionData(long nodeCode, long nodeCodeTo, int dataType, int size);

	/**
	 * 存储拍照图片数据 改成文件系统
	 *
	 * @param terminalId
	 * @param multimediaType
	 * @param multimediaTopic
	 * @param pictureEntity
	 */
	abstract void savePictureData(long terminalId, LcMultimediaParaDBEntity multimediaTopic, PictureEntity pictureEntity);

	/**
	 * 查询图片数据 根据文件系统修改
	 *
	 * @param terminalId
	 * @param multimediaType
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	abstract List<PictureEntity> queryPictureData(String... picName);

	/**
	 * 查询多媒体数据
	 *
	 * @param terminalId
	 * @param multimediaType
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	abstract List<LcMultimediaParaDBEntity> queryMultimedia(long[] terminalId, int multimediaType, long beginTime,
															long endTime);

	/**
	 * 存储终端透传数据
	 *
	 * @param transferEntity
	 */
	abstract void savePassThrough(List<GpsTransferEntity> transferEntity);

	/**
	 * 存储行驶记录仪数据
	 *
	 * @param gpsDrivingRecorderEntity
	 */
	abstract void saveDrivingRecorder(GpsDrivingRecorderEntity gpsDrivingRecorderEntity);

	/**
	 * 多媒体文件查询
	 *
	 * @param fileCode
	 * @param terminalId
	 * @return
	 */
	public MediaFileModel queryMediaFileRes(String fileCode, long terminalId);


	//modified by zhangyj
	public Map<Long, List<LocationData>> queryRedisGpsData(List<Long> tids, long begin, long end);

	// 以下方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存

	/**
	 * 查询一批终端的当天指定时间段[0, 288)的位置数据（5分钟一段，左闭右开区间）
	 *
	 *
	 * modified by zhangyj
	 * @param tids
	 * @param segment
	 * @return
	 */
	public Map<Long, List<LocationData>> queryGpsDataSegementThisDay(List<Long> tids, int segment);

	/**
	 * 查询指定终端的当天指定时间段[0, 288)的位置数据（5分钟一段，左闭右开区间）
	 *
	 * modified by zhangyj
	 * @param tid
	 * @param segment
	 * @return
	 */
	public List<LocationData> queryGpsDataSegementThisDay(long tid, int segment);

	// 以上方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存

	// added by zhangyj 删除分段位置数据
	public void delGpsDataSegementThisDay();
}
