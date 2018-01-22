package com.navinfo.opentsp.platform.location.rmi;

import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * User: zhanhk Date: 16/9/9 Time: 下午3:37
 */
public interface RmiBalancerInterface
{
    
    /**
     * 根据serviceId 获取服务,RMI调用
     * 
     * @param <T>
     * @param serviceId 注册eureka服务ID
     * @param interfaceName RMI接口名称
     * @param classType RMI接口实体类
     */
    <T> void rmiBalancerRequest(String serviceId, final String interfaceName, final Class<T> classType);
    
    /**
     * 根据serviceId 获取服务,RMI调用
     * 
     * @param serviceId 注册eureka服务ID
     * @param interfaceName RMI接口名称
     * @param classType RMI接口实体类
     * @param packetIn    传入参数
     * @param <T>
     * @return
     */
    <T> Packet rmiBalancerRequest(String serviceId, final String interfaceName, final Class<T> classType,
        final Packet packetIn);
    
    /**
     * 结果回调
     * 
     * @param t 接口类型
     * @param <T>
     */
    <T> void collBackRmiResult(T t);
}
