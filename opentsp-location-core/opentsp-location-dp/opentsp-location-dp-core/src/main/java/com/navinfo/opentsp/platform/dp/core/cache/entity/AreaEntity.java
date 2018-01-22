package com.navinfo.opentsp.platform.dp.core.cache.entity;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;

import java.util.ArrayList;
import java.util.List;

public class AreaEntity extends Entity {

	private static AreaCache areaCache;
	private static AreaCommonCache areaCommonCache;

	private static final long serialVersionUID = 1L;
	private long areaId;
	private long terminalId;
	private long originalAreaId;
	private int areaType;
	private long createDate;
	private List<AreaDataEntity> datas;

	static {
		areaCommonCache = (AreaCommonCache)SpringContextUtil.getBean("areaCommonCache");
		areaCache = (AreaCache)SpringContextUtil.getBean("areaCache");
	}

	@Override
	public String toString() {
		return "AreaEntity [areaId=" + areaId + ", terminalId=" + terminalId
				+ ", originalAreaId=" + originalAreaId + ", areaType="
				+ areaType + ", createDate=" + createDate + ", datas=" + datas
				+ "]";
	}

	public AreaEntity() {
		super();
	}

	public AreaEntity(AreaInfo areaInfo) {
		this.setAreaId(areaInfo.getAreaIdentify());
		this.setTerminalId(areaInfo.getTerminalId());
		this.setOriginalAreaId(areaInfo.getAreaIdentify());
		this.setAreaType(areaInfo.getTypes().getNumber());
		this.setCreateDate(areaInfo.getCreateDate());
		List<AreaDataEntity> datas = new ArrayList<AreaDataEntity>();
		for(int i = 0; i < areaInfo.getDatasCount(); i++) {
			AreaDataEntity entity = new AreaDataEntity();
			entity.setDataSn(areaInfo.getDatas(i).getDataSN());
			entity.setDataStatus(areaInfo.getDatas(i).getDataStatus());
			entity.setLatitude(areaInfo.getDatas(i).getLatitude());
			entity.setLongitude(areaInfo.getDatas(i).getLongitude());
			entity.setRadiusLength(areaInfo.getDatas(i).getRadiusLength());
			entity.setDataStatus(areaInfo.getDatas(i).getDataStatus());
			datas.add(entity);
		}
		this.setDatas(datas);
	}
	
	public void addAreaDataEntity(AreaDataEntity areaDataEntity){
		if(datas == null){
			datas = new ArrayList<AreaDataEntity>();
		}
		this.datas.add(areaDataEntity);
	}
	
	public void addAreaDataEntity(int dataSn , long dataStatus,int radiusLength, int longitude , int latitude){
		AreaDataEntity areaDataEntity = new AreaDataEntity();
		areaDataEntity.setDataSn(dataSn);
		areaDataEntity.setLongitude(longitude);
		areaDataEntity.setLatitude(latitude);
		areaDataEntity.setDataStatus(dataStatus);
		areaDataEntity.setRadiusLength(radiusLength);
		this.addAreaDataEntity(areaDataEntity);
	}


	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getOriginalAreaId() {
		return originalAreaId;
	}

	public void setOriginalAreaId(long originalAreaId) {
		this.originalAreaId = originalAreaId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public int getAreaType() {
		return areaType;
	}

	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}

	public List<AreaDataEntity> getDatas() {
		return datas;
	}

	public void setDatas(List<AreaDataEntity> datas) {
		this.datas = datas;
	}

	/**
	 * 更新缓存
	 */
	private void updateCache()
	{
		// 如果区域缓存存在，则更新
		if (areaCache.isExists(this))
		{
			areaCache.addAreaEntity(this);
		}
		// 如果区域共通缓存存在，则更新
		if (areaCommonCache.isExists(this))
		{
			areaCommonCache.addAreaEntity(this);
		}
	}

}
