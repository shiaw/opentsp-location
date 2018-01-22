package com.navinfo.opentsp.gateway.tcp.proto.locationrp.rmi;

import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.rmi.RmiBalancer;
import org.springframework.stereotype.Component;

/**
 * User: zhanhk Date: 16/9/13 Time: 上午11:25
 */
@Component
public class DaRmiService extends RmiBalancer
{
    
    public void rmiRequest(String serviceId, String interfaceName, Packet packet)
    {
        // super.rmiBalancerRequest("opentsp-rmi-server","ArticleService",ArticleService.class);
    }
    
    /**
     * 调用rmi接口
     * 
     * @param serviceId 服务id
     * @param interfaceName 接口名称
     * @param t 接口类
     * @param packetIn 入参
     * @param <T>
     * @return
     */
    public <T> Packet callRmi(String serviceId, String interfaceName, Class<T> t, Packet packetIn)
    {
        return rmiBalancerRequest(serviceId, interfaceName, t, packetIn);
    }
    
    @Override
    public <T> void collBackRmiResult(T t)
    {
        
    }
}
