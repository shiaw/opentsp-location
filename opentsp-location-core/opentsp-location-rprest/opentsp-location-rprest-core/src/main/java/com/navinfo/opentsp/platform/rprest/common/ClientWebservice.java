package com.navinfo.opentsp.platform.rprest.common;

import com.navinfo.opentsp.platform.rprest.utils.Configuration;
import java.io.PrintStream;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class ClientWebservice
{
    public static Object getWebserviceD(Class class1)
    {
        JaxWsProxyFactoryBean factory_3 = new JaxWsProxyFactoryBean();
        factory_3.setServiceClass(class1);
        String ip = Configuration.getString("DA.WS.IP");
        String port = Configuration.getString("DA.WS.PORT");
        String client = Configuration.getString("DA.WS.SERVICENAME");
        System.out.println("http://" + ip + ":" + port + "/" + client);
        factory_3.setAddress("http://" + ip + ":" + port + "/" + client);
        Object logWS = factory_3.create();
        return logWS;
    }
}