package com.navinfo.opentsp.platform.da.core.common.hash;

public interface Hash {
	abstract long hash(Object key);

	abstract long hash(byte[] digest, int nTime);

	abstract byte[] computeMd5(String k);
	
	abstract String computeMd5String(String src, String encoding);
}
