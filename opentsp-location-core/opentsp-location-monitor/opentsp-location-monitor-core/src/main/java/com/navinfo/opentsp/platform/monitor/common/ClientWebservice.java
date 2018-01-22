package com.navinfo.opentsp.platform.monitor.common;

import com.navinfo.opentsp.platform.monitor.utils.Configuration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class ClientWebservice {

    /**
     * 方法为调用web service原始接口
     * @param class1  web service 接口类
     * @return web service 接口类名称
     */
    public static  Object getWebservice(Class class1){
        JaxWsProxyFactoryBean factory_3 = new JaxWsProxyFactoryBean();
        factory_3.setServiceClass(class1);
        String ip= Configuration.getString("BASICDQ.WS.IP");
        String port=Configuration.getString("BASICDQ.WS.PORT");
        String client=Configuration.getString("BASICDQ.WS.SERVICENAME");
/*        String ip = "192.168.135.156";
        String port ="30506";
        String client ="BasicDQWS";*/
        System.out.println("http://" + ip + ":" + port + "/" + client);
        factory_3.setAddress("http://" + ip + ":" + port + "/" + client);
        Object logWS = (Object) factory_3.create();
        return logWS;
    }
    /**
     * 方法为调用web service统计接口
     * @param class1  web service 接口类
     * @return web service 接口类名称
     */
    public static  Object getWebserviceD(Class class1){
        JaxWsProxyFactoryBean factory_3 = new JaxWsProxyFactoryBean();
        factory_3.setServiceClass(class1);
        String ip=Configuration.getString("DA.WS.IP");
        String port=Configuration.getString("DA.WS.PORT");
        String client=Configuration.getString("DA.WS.SERVICENAME");
        System.out.println("http://" + ip + ":" + port + "/" + client);
        factory_3.setAddress("http://" + ip + ":" + port + "/" + client);
        Object logWS = (Object) factory_3.create();
        return logWS;
    }
    /**
     * 方法为调用web service终端接口
     * @param class1  web service 接口类
     * @return web service 接口类名称
     */
    public static  Object getWebserviceT(Class class1){
        JaxWsProxyFactoryBean factory_3 = new JaxWsProxyFactoryBean();
        factory_3.setServiceClass(class1);
        String ip=Configuration.getString("TERMINAL.WS.IP");
        String port=Configuration.getString("TERMINAL.WS.PORT");
        String client=Configuration.getString("TERMINAL.WS.SERVICENAME");
        System.out.println("http://" + ip + ":" + port + "/" + client);
        factory_3.setAddress("http://" + ip + ":" + port + "/" + client);
        Object logWS = (Object) factory_3.create();
        return logWS;
    }
}