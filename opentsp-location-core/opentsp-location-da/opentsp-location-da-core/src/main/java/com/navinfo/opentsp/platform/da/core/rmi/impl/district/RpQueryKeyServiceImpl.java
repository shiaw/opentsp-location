package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.VehicleDrivingNumber;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.VehicleDrivingNumberImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DASQueryKeyServiceImp;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.QueryKeyEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.RpQueryKeyService;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCCrossGridCounts.CrossGridCounts;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * DSA统计数据查询key缓存服务
 * @author jin_s
 *
 */
@Service
public class RpQueryKeyServiceImpl implements RpQueryKeyService {
	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;

	public RpQueryKeyServiceImpl() {
		super();

	}
	DASQueryKeyServiceImp service=new DASQueryKeyServiceImp();
	/**
	 * 查询key
	 */
	@Override
	public Object getQueryKeyData(String queryKey) {
		Object findQueryKey = service.findQueryKey(queryKey);
		if(null!=findQueryKey){

//		  ((QueryKeyEntity) findQueryKey).setLastUpdateTime(System.currentTimeMillis()/1000);
//		     service.putData(queryKey, findQueryKey);
			return  ((QueryKeyEntity) findQueryKey).getNodeCode();
		}
		return 0l;

	}
	/**
	 * 存储查询的key
	 */
	@Override
	public void saveQueryKeyData(String queryKey, Object nodeCode) {
		QueryKeyEntity entity=new QueryKeyEntity();
		entity.setQueryKey(queryKey);
		entity.setNodeCode( (long) nodeCode);
		entity.setLastUpdateTime(System.currentTimeMillis()/1000);
		service.putData(queryKey, entity);
	}
	/**
	 * 删除Key
	 */
	@Override
	public void removeQueryKeyData(String queryKey) {
		service.removeQueryKey(queryKey);
	}



	/**
	 * 查询key
	 */
	@Override
	public Map<String, Object> findQueryKeyDatas() {
		return service.findQueryKeys();
	}
	@Override
	public CrossGridCounts getGridCrossCounts(List<Long> terminalIds,
											  List<Long> tileId,long startDate, long endDate, long accessTocken) {
		VehicleDrivingNumber number = new VehicleDrivingNumberImpl();
		CrossGridCounts gridCrossCounts = number.getGridCrossCounts(terminalIds, tileId, startDate, endDate);
		return gridCrossCounts;
	}

}
