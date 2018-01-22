package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.cache.entity.TerminalEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.redis.TerminalRedisServiceImple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 终端列表缓存<br>
 * key=终端ID,value=终端信息
 * @author lgw
 *
 */
@Component("terminalCache")
public class TerminalCache {
	private static final Logger log = LoggerFactory.getLogger(TerminalCache.class);

	@Resource
	private TerminalRedisServiceImple terminalRedisServiceImple;

	@Value("${opentsp.cache.terminalCacheKey:com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage:0}")
	private String terminalCacheKey;

	private TerminalCache() {
	}

	public void addTerminal(TerminalEntity terminal) {
		terminalRedisServiceImple.hset(Constant.TERMINAL_CACHE_KEY, String.valueOf(terminal.getTerminalId()), terminal);
	}
	public Set<String> getAllTerminalIds(){
		return this.get().keySet();
	}
	public Map<String, TerminalEntity> get(){
		return terminalRedisServiceImple.getAllKeys(Constant.TERMINAL_CACHE_KEY, TerminalEntity.class);
	}
	public Set<TerminalEntity> getAllTerminalEntities(){
		Set<TerminalEntity> results = new HashSet<>();
		Map<String, TerminalEntity> map = this.get();
		for(String key : map.keySet()){
			results.add( map.get(key) );
		}
		return results;
	}
	public TerminalEntity getTerminal(long terminalId) {
		return terminalRedisServiceImple.get(terminalCacheKey+terminalId,TerminalEntity.class);
	}
	public void removeTerminalEntity(long terminalId)
	{
		terminalRedisServiceImple.delete(Constant.TERMINAL_CACHE_KEY, String.valueOf(terminalId));
	}
}
