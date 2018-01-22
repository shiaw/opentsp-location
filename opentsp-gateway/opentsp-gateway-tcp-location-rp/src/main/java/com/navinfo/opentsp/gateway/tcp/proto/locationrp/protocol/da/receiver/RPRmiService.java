package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver;


import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.rmi.RmiBalancer;
import com.navinfo.opentsp.platform.location.rmi.RmiBalancerInterface;
import org.springframework.stereotype.Component;

/**
 * User: zhanhk
 * Date: 16/9/9
 * Time: 下午3:40
 */
@Component
public class RPRmiService extends RmiBalancer implements RmiBalancerInterface {

    public void rmiRequest() {
        //super.rmiBalancerRequest("opentsp-location-da","DaRmiInterface",DaRmiInterface.class);
    }

    /**
     * 调用rmi接口
     *
     * @param serviceId     服务id
     * @param interfaceName 接口名称
     * @param t             接口类
     * @param packetIn      入参
     * @param <T>
     * @return
     */
    public <T> Packet callRmi(String serviceId, String interfaceName, Class<T> t, Packet packetIn) {
        return rmiBalancerRequest(serviceId, interfaceName, t, packetIn);
    }

    @Override
    public <T> void collBackRmiResult(T t) {

    }
}
