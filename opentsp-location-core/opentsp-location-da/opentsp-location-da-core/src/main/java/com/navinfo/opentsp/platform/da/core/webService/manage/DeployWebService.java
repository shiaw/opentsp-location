package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.ExtendConfig;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.NodePort;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.NodePort.PortType;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.LcNodeManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.webService.service.ConfigWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.DictWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.LogWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.UserWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.center.CenterConfigWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.center.CenterDictWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.center.CenterLogWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.center.CenterTerminalWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.center.CenterUserWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.district.DistrictConfigWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.district.DistrictDictWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.district.DistrictLogWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.district.DistrictTerminalWebServiceImpl;
import com.navinfo.opentsp.platform.da.core.webService.service.impl.district.DistrictUserWebServiceImpl;

public class DeployWebService {
    private static String ip = Configuration
            .getString(Constant.PropertiesKey.WebServicePropertiesKey.wsIp);
    private static String port = Configuration
            .getString(Constant.PropertiesKey.WebServicePropertiesKey.wsPort);
    private static String isCenter = Configuration
            .getString(Constant.PropertiesKey.WebServicePropertiesKey.isCenter);
    private static String module = Configuration
            .getString(Constant.PropertiesKey.WebServicePropertiesKey.wsModule);

    private static Logger logger = LoggerFactory.getLogger(DeployWebService.class);
    /**
     * webservice发布程序
     */
    public static void deployService() {
        if ("true".equals(isCenter)) {
            //初始化ws配置
            initWSConfig();
            //部署ws
            centerDeplay();
        } else {
            districtDeplay();
        }
    }

    /* 初始化ws配置信息 ，预加载分区信息*/
    private static void initWSConfig() {

        WSConfigCache object;

        LcNodeManage lcNodeManage=new LcNodeManageImpl();
        List<LcNodeConfigDBEntity> daNodes= lcNodeManage.getNodesByNodeType(NodeType.da_VALUE);
        if(daNodes!=null){
            for(LcNodeConfigDBEntity nodeConfig:daNodes){
                object=new WSConfigCache();

                String localIpAddress=nodeConfig.getLocal_ip_address();
                try {
                    ExtendConfig extendConfig = ExtendConfig.parseFrom(nodeConfig.getExt_content());
                    if(extendConfig != null){
                        List<NodePort> ports = extendConfig.getPortsList();
                        for (NodePort nodePort : ports) {
                            if(PortType.da_ws_port_VALUE== nodePort.getTypes().getNumber()){
                                object.setPort(nodePort.getPorts()+"");
                            }
                        }
                    }

                } catch (InvalidProtocolBufferException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                object.setIp(localIpAddress);
                object.setDistrict(nodeConfig.getDistrict()+"");
                object.setNodeCode(nodeConfig.getNode_code());
                WSConfigCacheManage.addWSConfigCatch(nodeConfig.getDistrict()+"",object);
            }
        }
        //int subCentCount =Configuration.getInt(Constant.PropertiesKey.WebServicePropertiesKey.subcenterCount);
        //System.out.println(subCentCount);

    }

    /* 总部ws发布 */
    private static void centerDeplay() {
        //初始化 同步数据表最大id
        logger.info("初始化同步数据最大id..............");
        try {
            SynchronousDataCache.initMaxSynchronizationLogId();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(ExceptionUtils.getFullStackTrace(e));
            e.printStackTrace();
            return;
        }
        logger.info("the center webService is starting...........");
        DictWebService dictWebService = new CenterDictWebServiceImpl();
        LogWebService logWebService = new CenterLogWebServiceImpl();
        ConfigWebService configWebService = new CenterConfigWebServiceImpl();
        TerminalWebService terminalWebService = new CenterTerminalWebServiceImpl();
        UserWebService userWebService = new CenterUserWebServiceImpl();
        // 发布dictWS
        // WS.MODULE=configWebService,dictWebService,logWebService,terminalWebService,userWebService
        String[] name = module.split(",");
        for (String str : name) {
            if (WebServiceName.configWebService.name().equals(str)) {
                deployConfigWS(configWebService);
            } else if (WebServiceName.dictWebService.name().equals(str)) {
                deployDictWS(dictWebService);
            } else if (WebServiceName.logWebService.name().equals(str)) {
                deployLogWS(logWebService);
            } else if (WebServiceName.terminalWebService.name().equals(str)) {
                deployTerminalWS(terminalWebService);
            } else if (WebServiceName.userWebService.name().equals(str)) {
                deployUserWS(userWebService);
            }
        }
        logger.info("the center webService is started......");
    }

    /* 分区ws发布 */
    private static void districtDeplay() {
        logger.info("the district webService is starting...........");
        DictWebService dictWebService = new DistrictDictWebServiceImpl();
        LogWebService logWebService = new DistrictLogWebServiceImpl();
        ConfigWebService configWebService = new DistrictConfigWebServiceImpl();
        TerminalWebService terminalWebService = new DistrictTerminalWebServiceImpl();
        UserWebService userWebService = new DistrictUserWebServiceImpl();
        // 发布dictWS
        // WS.MODULE=configWebService,dictWebService,logWebService,terminalWebService,userWebService
        String[] name = module.split(",");
        for (String str : name) {
            if (WebServiceName.configWebService.name().equals(str)) {
                deployConfigWS(configWebService);
            }else if (WebServiceName.dictWebService.name().equals(str)) {
                deployDictWS(dictWebService);
            } else if (WebServiceName.logWebService.name().equals(str)) {
                deployLogWS(logWebService);
            } else if (WebServiceName.terminalWebService.name().equals(str)) {
                deployTerminalWS(terminalWebService);
            } else if (WebServiceName.userWebService.name().equals(str)) {
                deployUserWS(userWebService);
            }
        }
        logger.info("the district webService is started......");
    }

    /* 用户ws发布 */
    private static void deployUserWS(UserWebService userWebService) {

        JaxWsServerFactoryBean user_factory = new JaxWsServerFactoryBean();
        user_factory.setServiceClass(UserWebService.class);

        user_factory.setAddress("http://" + ip + ":" + port + "/"
                + WebServiceName.userWebService);
        logger.info("==用户信息wsURL=="+"http://" + ip + ":" + port + "/"
                + WebServiceName.userWebService);
        // 设置ServiceBean对象
        user_factory.setServiceBean(userWebService);
        // 添加请求和响应的拦截器，Phase.RECEIVE只对In有效，Phase.SEND只对Out有效
        if ("true".equals(isCenter)) {
            user_factory.getInInterceptors().add(new CenterInInterceptor());
        } else {
            user_factory.getInInterceptors().add(
                    new DistrictInterceptor(Phase.RECEIVE));
        }
        user_factory.create();

    }

    /* 终端ws发布 */
    private static void deployTerminalWS(TerminalWebService terminalWebService) {

        JaxWsServerFactoryBean terminal_factory = new JaxWsServerFactoryBean();
        terminal_factory.setServiceClass(TerminalWebService.class);
        terminal_factory.setAddress("http://" + ip + ":" + port + "/"
                + WebServiceName.terminalWebService);
        logger.info("==终端信息wsURL=="+"http://" + ip + ":" + port + "/"
                + WebServiceName.terminalWebService);
        // 设置ServiceBean对象
        terminal_factory.setServiceBean(terminalWebService);
        // 添加请求和响应的拦截器，Phase.RECEIVE只对In有效，Phase.SEND只对Out有效
        if ("true".equals(isCenter)) {
            terminal_factory.getInInterceptors().add(new CenterInInterceptor());
        } else {
            terminal_factory.getInInterceptors().add(
                    new DistrictInterceptor(Phase.RECEIVE));
        }
        terminal_factory.create();

    }

    /* 配置ws发布 */
    private static void deployConfigWS(ConfigWebService configWebService) {

        JaxWsServerFactoryBean config_factory = new JaxWsServerFactoryBean();
        config_factory.setServiceClass(ConfigWebService.class);
        config_factory.setAddress("http://" + ip + ":" + port + "/"
                + WebServiceName.configWebService);
        logger.info("==配置wsURL=="+"http://" + ip + ":" + port + "/"
                + WebServiceName.configWebService);
        // 设置ServiceBean对象
        config_factory.setServiceBean(configWebService);
        // 添加请求和响应的拦截器，Phase.RECEIVE只对In有效，Phase.SEND只对Out有效
        if ("true".equals(isCenter)) {
            config_factory.getInInterceptors().add(new CenterInInterceptor());
        } else {
            config_factory.getInInterceptors().add(
                    new DistrictInterceptor(Phase.RECEIVE));
        }
        config_factory.create();

    }

    /* 字典ws发布 */
    private static void deployDictWS(DictWebService dictWebService) {
        JaxWsServerFactoryBean dict_factory = new JaxWsServerFactoryBean();
        dict_factory.setServiceClass(DictWebService.class);
        logger.info("==字典wsURL==="+"http://" + ip + ":" + port + "/"
                + WebServiceName.dictWebService);
        dict_factory.setAddress("http://" + ip + ":" + port + "/"
                + WebServiceName.dictWebService);
        // 设置ServiceBean对象
        dict_factory.setServiceBean(dictWebService);
        // 添加请求和响应的拦截器，Phase.RECEIVE只对In有效，Phase.SEND只对Out有效
        if ("true".equals(isCenter)) {
            dict_factory.getInInterceptors().add(new CenterInInterceptor());
        } else {
            dict_factory.getInInterceptors().add(
                    new DistrictInterceptor(Phase.RECEIVE));
        }
        dict_factory.create();
    }

    /* 日志ws发布 */
    private static void deployLogWS(LogWebService logWebService) {
        JaxWsServerFactoryBean log_factory = new JaxWsServerFactoryBean();
        log_factory.setServiceClass(LogWebService.class);
        log_factory.setAddress("http://" + ip + ":" + port + "/"
                + WebServiceName.logWebService);
        logger.info("==日志wsURL=="+"http://" + ip + ":" + port + "/"
                + WebServiceName.logWebService);
        // 设置ServiceBean对象
        log_factory.setServiceBean(logWebService);
        // 添加请求和响应的拦截器，Phase.RECEIVE只对In有效，Phase.SEND只对Out有效
        // 根据类型添加不同的拦截器
        if ("true".equals(isCenter)) {
            log_factory.getInInterceptors().add(new CenterInInterceptor());
        } else {
            log_factory.getInInterceptors().add(
                    new DistrictInterceptor(Phase.RECEIVE));
        }
        log_factory.create();
    }


}
