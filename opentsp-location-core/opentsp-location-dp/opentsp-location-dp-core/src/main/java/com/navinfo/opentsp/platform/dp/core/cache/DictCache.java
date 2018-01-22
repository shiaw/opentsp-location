package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.cache.entity.DictEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictType.DictType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("dictCache")
public class DictCache {
	@Resource
	private IRedisService redisService;


	private DictCache() {
	}

	public void addDict(DictEntity dictEntity) {
		Map<Integer, DictEntity> dicts = this.getDicts(dictEntity.getDictType());
		if (dicts == null) {
			dicts = new ConcurrentHashMap<Integer, DictEntity>();
		}
		dicts.put(dictEntity.getDataCode(), dictEntity);
		redisService.hset(Constant.DICT_CACHE_KEY, String.valueOf(dictEntity.getDictType().ordinal()), dicts);
	}

	public DictEntity getDictForDataCode(DictType dictType, int dataCode) {
		Map<Integer, DictEntity> dicts = this.getDicts(dictType);
		if (dicts != null) {
			return dicts.get(dataCode);
		}
		return null;
	}

	public Map<Integer, DictEntity> getDicts(DictType dictType) {
		return redisService.getHashValue(Constant.DICT_CACHE_KEY, String.valueOf(dictType.ordinal()), Map.class);
	}

	public void remove(DictType dictType) {
		redisService.delete(Constant.DICT_CACHE_KEY,String.valueOf(dictType.ordinal()));
	}

	public void remove(DictType dictType, int dataCode) {
		Map<Integer, DictEntity> dicts = this.getDicts(dictType);
		if(dicts != null){
			Iterator<Map.Entry<Integer, DictEntity>> iterator = dicts.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Integer, DictEntity> entry = iterator.next();
				if(entry.getValue().getDataCode() == dataCode){
					iterator.remove();
				}
			}
			redisService.hset(Constant.DICT_CACHE_KEY, String.valueOf(dictType.ordinal()), dicts);

		}
	}
//	public int size(){
//		return cache.size();
//	}

}
