package com.navinfo.opentsp.platform.da.core.persistence;


import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;

public interface CollectionCheckManager {
    
	public LCPlatformResponseResult.PlatformResponseResult checkCollection(String date);
}
