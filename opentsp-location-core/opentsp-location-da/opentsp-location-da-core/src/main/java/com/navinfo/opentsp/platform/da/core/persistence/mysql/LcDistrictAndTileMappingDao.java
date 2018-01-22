package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictAndTileMappingDBEntity;

import java.util.List;



public interface LcDistrictAndTileMappingDao extends BaseDao<LcDistrictAndTileMappingDBEntity>{
	
	
	public List<LcDistrictAndTileMappingDBEntity> getByZoom(Integer zoom);
	
	public List<LcDistrictAndTileMappingDBEntity> getDistrictAndTileMappingList();
	public List<LcDistrictAndTileMappingDBEntity> getDistrictAndTileMappingListPage(int pageNum, int pageSize);
	
	public List<LcDistrictAndTileMappingDBEntity> getByDistrictId(Integer districtId);
}
