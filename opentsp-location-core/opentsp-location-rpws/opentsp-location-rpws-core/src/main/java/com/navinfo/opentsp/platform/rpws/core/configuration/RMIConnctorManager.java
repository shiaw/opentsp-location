package com.navinfo.opentsp.platform.rpws.core.configuration;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.synchronousdata.SynchronousTerminalDataService;
import com.navinfo.opentsp.platform.rpws.core.common.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

@Component("rmiConnctorManager")
public class RMIConnctorManager {
	private Logger logger = LoggerFactory.getLogger(RMIConnctorManager.class);

	@Value("${da.rmi.eureka.registerName:opentsp-location-da}")
	private String SERVICEID;

	@Value("${da.rmi.port:1199}")
	private int port;

	@Autowired
	private LoadBalancerClient balancerClient;

	private static RMIConnctorManager _instance = null;

	public static RMIConnctorManager getInstance() {
		if (_instance == null) {
			_instance = (RMIConnctorManager) SpringContextUtil.getBean("rmiConnctorManager");
		}

		return _instance;
	}

	//保留
	public TermianlRuleAndParaService getTermianlRuleAndParaService()
			throws Exception {
		TermianlRuleAndParaService client = balancerClient.execute(SERVICEID, new LoadBalancerRequest<TermianlRuleAndParaService>() {
			@Override
			public TermianlRuleAndParaService apply(ServiceInstance instance) throws Exception {
				RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
				rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), port, "TermianlRuleAndParaService"));
				rmiProxyFactoryBean.setServiceInterface(TermianlRuleAndParaService.class);
				rmiProxyFactoryBean.afterPropertiesSet();
				TermianlRuleAndParaService client = (TermianlRuleAndParaService) rmiProxyFactoryBean.getObject();

				return client;
			}
		});
		return client;
	}

	//保留
	public RpQueryKeyService getRpQueryKeyService() throws Exception {
		RpQueryKeyService client = balancerClient.execute(SERVICEID, new LoadBalancerRequest<RpQueryKeyService>() {
			@Override
			public RpQueryKeyService apply(ServiceInstance instance) throws Exception {
				RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
				rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), port, "RpQueryKeyService"));
				rmiProxyFactoryBean.setServiceInterface(RpQueryKeyService.class);
				rmiProxyFactoryBean.afterPropertiesSet();
				RpQueryKeyService client = (RpQueryKeyService) rmiProxyFactoryBean.getObject();
				return client;
			}
		});

		return client;
	}

	//保留
	public AlarmStatisticsQueryService getAlarmStatisticsQueryService() throws Exception {
		AlarmStatisticsQueryService client = balancerClient.execute(SERVICEID, new LoadBalancerRequest<AlarmStatisticsQueryService>() {
			@Override
			public AlarmStatisticsQueryService apply(ServiceInstance instance) throws Exception {
				RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
				rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), port, "AlarmStatisticsQueryService"));
				rmiProxyFactoryBean.setServiceInterface(AlarmStatisticsQueryService.class);
				rmiProxyFactoryBean.afterPropertiesSet();
				AlarmStatisticsQueryService client = (AlarmStatisticsQueryService) rmiProxyFactoryBean.getObject();
				return client;
			}
		});

		return client;
	}

	/**
	 * 获取统计查询服务RMI对象（新）——2016年5月26日新增，王景康  （保留）
	 *
	 * @return
	 * @throws Exception
	 */
	public StatisticsQueryService getStaticsQueryService() throws Exception {
		StatisticsQueryService client = balancerClient.execute(SERVICEID, new LoadBalancerRequest<StatisticsQueryService>() {
			@Override
			public StatisticsQueryService apply(ServiceInstance instance) throws Exception {
				RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
				rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), port, "StatisticsQueryService"));
				rmiProxyFactoryBean.setServiceInterface(StatisticsQueryService.class);
				rmiProxyFactoryBean.afterPropertiesSet();
				StatisticsQueryService client = (StatisticsQueryService) rmiProxyFactoryBean.getObject();
				return client;
			}
		});

		return client;
	}

	//保留
	public TermianlDataService getTermianlDataService() throws Exception {
		TermianlDataService client = balancerClient.execute(SERVICEID, new LoadBalancerRequest<TermianlDataService>() {
			@Override
			public TermianlDataService apply(ServiceInstance instance) throws Exception {
				RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
				rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), port, "TermianlDataService"));
				rmiProxyFactoryBean.setServiceInterface(TermianlDataService.class);
				rmiProxyFactoryBean.afterPropertiesSet();
				TermianlDataService client = (TermianlDataService) rmiProxyFactoryBean.getObject();
				return client;
			}
		});

		return client;
	}

	//保留
	public SynchronousTerminalDataService getSynchronousTermianlDataService()
			throws Exception {
		SynchronousTerminalDataService client = balancerClient.execute(SERVICEID, new LoadBalancerRequest<SynchronousTerminalDataService>() {
			@Override
			public SynchronousTerminalDataService apply(ServiceInstance instance) throws Exception {
				RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
				rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), port, "SynchronousTerminalDataService"));
				rmiProxyFactoryBean.setServiceInterface(SynchronousTerminalDataService.class);
				rmiProxyFactoryBean.afterPropertiesSet();
				SynchronousTerminalDataService client = (SynchronousTerminalDataService) rmiProxyFactoryBean.getObject();
				return client;
			}
		});

		return client;
	}

	//保留
	public AlarmStatisticsStoreService getAlarmStatisticsStoreService() throws Exception {
		AlarmStatisticsStoreService client = balancerClient.execute(SERVICEID, new LoadBalancerRequest<AlarmStatisticsStoreService>() {
			@Override
			public AlarmStatisticsStoreService apply(ServiceInstance instance) throws Exception {
				RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
				rmiProxyFactoryBean.setServiceUrl(buildRmiUrl(instance.getHost(), port, "AlarmStatisticsStoreService"));
				rmiProxyFactoryBean.setServiceInterface(AlarmStatisticsStoreService.class);
				rmiProxyFactoryBean.afterPropertiesSet();
				AlarmStatisticsStoreService client = (AlarmStatisticsStoreService) rmiProxyFactoryBean.getObject();
				return client;
			}
		});

		return client;
	}

	private String buildRmiUrl(String hostName ,int port,String interfaceName){
		return "rmi://"+hostName+":" +port+ "/"+interfaceName;
	}
}
