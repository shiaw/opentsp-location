package com.navinfo.opentsp.platform.dsa.rmiclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * User: zhanhk Date: 16/9/9 Time: 下午3:34
 */
public abstract class RmiBalancer implements RmiBalancerInterface {

    @Value("${da.rmi.port:1199}")
    private Integer rmiPort;

    @Autowired
    private LoadBalancerClient balancerClient;

    @Override
    public <T> T rmiBalancerRequest(final String serviceId, final String interfaceName, final Class<T> classType) {

        return (T) balancerClient.execute(serviceId, new LoadBalancerRequest<Object>() {
            @Override
            public Object apply(ServiceInstance instance)
                    throws Exception {
                RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();

                rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), rmiPort, interfaceName));
                rmiProxyFactoryBean.setServiceInterface(classType);
                rmiProxyFactoryBean.afterPropertiesSet();
                T t = (T) rmiProxyFactoryBean.getObject();
                return t;
            }
        });
    }

    public String buildRmiUrl(String hostName, int port, String interfaceName) {
        return "rmi://" + hostName + ":" + port + "/" + interfaceName;
    }
}
