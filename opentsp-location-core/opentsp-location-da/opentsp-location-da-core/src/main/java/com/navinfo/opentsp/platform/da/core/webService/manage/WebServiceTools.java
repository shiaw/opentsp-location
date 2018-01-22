package com.navinfo.opentsp.platform.da.core.webService.manage;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * 调用webService的公共方法
 *
 * @author ss
 *
 */
@SuppressWarnings("rawtypes")
public class WebServiceTools {

	/**
	 * 得到webservice指处理类
	 * */
	public static Object getOperationClass(String district, Class clazz) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		Object create=null;
		factory.setServiceClass(clazz);
		WSConfigCache wsConfigCatch=null;
		do {
			wsConfigCatch = WSConfigCacheManage.getWSConfigCatch(district);
			factory.setAddress(getUrl(district, clazz,wsConfigCatch));
			create = factory.create();
			if(create==null){
				WSConfigCacheManage.updateWSConfigCatch(wsConfigCatch);
			}

		} while (wsConfigCatch==null || create == null);
		return create;
	}


	/**
	 * 组合webservice接口url
	 * @param district 分区
	 * @param clazz 类型，目的是组装ws接口名
	 * @return
	 */
	private static String getUrl(String district, Class clazz,WSConfigCache wsConfigCatch) {

		// 得到webservice处理类名称
		String[] array = clazz.getName().split("\\.");
		String className = array[array.length - 1];
		String firstLetter=className.substring(0, 1);
		String lastLetter=className.substring(1, className.length());
		// 根据处理类名得到发布的webservice接口名称
		String name = firstLetter.toLowerCase()+lastLetter;
		// 组合webserviceURL
		// 根据分区编码得到分区webservice的ip地址
		String ip = wsConfigCatch.getIp();
		String port =wsConfigCatch.getPort();
		String url = "http://" + ip +":"+port+ "/" + name;
		return url;
	}

}
