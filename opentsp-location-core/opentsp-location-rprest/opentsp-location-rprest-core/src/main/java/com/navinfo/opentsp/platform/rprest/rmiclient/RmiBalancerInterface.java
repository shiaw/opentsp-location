package com.navinfo.opentsp.platform.rprest.rmiclient;

/**
 * User: zhanhk Date: 16/9/9 Time: 下午3:37
 */
public interface RmiBalancerInterface {

    /**
     * 根据serviceId 获取服务
     *
     * @param <T>
     * @param serviceId     注册eureka服务ID
     * @param interfaceName RMI接口名称
     * @param classType     RMI接口实体类
     */
    <T> T rmiBalancerRequest(String serviceId, final String interfaceName, final Class<T> classType);

}
