package com.navinfo.opentsp.platform.dp.core.cache.init.base;

import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyu on 2016/9/21.
 */
@Component
public abstract class InitBaseCache implements CommandLineRunner
{
    @Value("${district.code}")
    public int districtCode;
    
    public Packet setPacketIn()
    {
        return null;
    }
    
    public abstract void initCache();

    @Override
    public void run(String... strings) throws Exception {
        this.initCache();
    }
}
