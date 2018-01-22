package com.navinfo.opentsp.platform.dp.core.common.hash;

public interface Hash {
	abstract long hash(Object key);

	abstract long hash(byte[] digest, int nTime);

	abstract byte[] computeMd5(String k);
}
