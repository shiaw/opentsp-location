package com.navinfo.opentsp.platform.location.rmi;

import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.da.DaRmiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * User: zhanhk Date: 16/9/9 Time: 下午3:34
 */
public abstract class RmiBalancer implements RmiBalancerInterface
{
    
    private static final Object STUB = new Object();

    @Value("${da.rmi.port:1199}")
    private Integer rmiPort;


    @Autowired
    private LoadBalancerClient balancerClient;
    
    @Override
    public <T> void rmiBalancerRequest(final String serviceId, final String interfaceName, final Class<T> classType)
    {
        
        balancerClient.execute(serviceId, new LoadBalancerRequest<Object>()
        {
            @Override
            public Object apply(ServiceInstance instance)
                throws Exception
            {
                RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
                
                rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), rmiPort, interfaceName));
                rmiProxyFactoryBean.setServiceInterface(classType);
                rmiProxyFactoryBean.afterPropertiesSet();
                T t = (T)rmiProxyFactoryBean.getObject();
                collBackRmiResult(t);
                return STUB;
            }
        });
    }

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
    @Override
    public <T> Packet rmiBalancerRequest(final String serviceId, final String interfaceName, final Class<T> classType,
        final Packet packetIn)
    {
        
        Packet packet = (Packet)balancerClient.execute(serviceId, new LoadBalancerRequest<Object>()
        {
            @Override
            public Object apply(ServiceInstance instance)
                throws Exception
            {
                RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
                
                rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), rmiPort, interfaceName));
                rmiProxyFactoryBean.setServiceInterface(classType);
                rmiProxyFactoryBean.afterPropertiesSet();
                DaRmiInterface daRmiInterface = (DaRmiInterface)rmiProxyFactoryBean.getObject();
                Packet packet = daRmiInterface.getRmiPacket(packetIn);
                return packet;
            }
        });
        
        return packet;
    }
    
    public String buildRmiUrl(String hostName, int port, String interfaceName)
    {
        return "rmi://" + hostName + ":" + port + "/" + interfaceName;
    }
}
