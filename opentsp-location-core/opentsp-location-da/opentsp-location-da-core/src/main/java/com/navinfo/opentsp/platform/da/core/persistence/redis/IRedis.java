package com.navinfo.opentsp.platform.da.core.persistence.redis;

public interface IRedis {
	abstract byte[] string2byte(String string);

	abstract String byte2string(byte[] bytes);

	abstract byte[] object2byte(Object object);

	abstract Object byte2object(byte[] bytes);

	abstract byte[][] string2byte(String... strings);
	
	abstract byte[][] object2byte(Object... objects);

	abstract boolean setKeyExpire(String key, int seconds);

	abstract int getKetTtl(String key);

	abstract long del(String... keys);
	
}
