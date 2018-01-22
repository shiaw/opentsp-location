package com.lc.rp.webService.manager;

import com.lc.rp.common.MD5;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import java.util.List;

/**
 * RP-WS自定义拦截器，用于对统计报表查询的唯一key进行MD5计算，并对该查询key分配唯一的DSA节点
 *
 * @author jin_s
 */

public class DateAnalysisQueryInterceptor extends AbstractSoapInterceptor {

    // logger
    Logger log = LoggerFactory.getLogger(DateAnalysisQueryInterceptor.class);

    public DateAnalysisQueryInterceptor() {
        // 指定该拦截器在执行前阶段被激发
        super(Phase.PRE_INVOKE);
    }

    /**
     * 拦截器主要考虑做MD5的字符串拼接，然后计算MD5的key，此处代码过于复杂可以考虑优化，参数里面是类的考虑实现toString方法。——HK
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        // 获取webservice的入参
        List<Object> result = MessageContentsList.getContentsList(message);
        QName qName = (QName) message.get(SoapMessage.WSDL_OPERATION);
        // 请求服务名
        String methodName = qName.getLocalPart();
        log.info("----调用RP-ws接口--->[" + methodName + "]");
        /**
         * 该类报表没有分页处理，不需要进行MD5的key计算
         */
        //		if("getVehiclePassTimesRecords".equals(methodName) || "getVehiclePassInArea".equals(methodName) ||
//			"delOvertimeParkRecords".equals(methodName)||"getVehiclePassTimesBytileId".equals(methodName) ||
//			"getGridCrossCounts".equals(methodName)||"getLastestVehiclePassInArea".equals(methodName)||
//			"getMileageAndOilData".equals(methodName)||"stagnationTimeoutCancelOrNot".equals(methodName)){
//			return;
//		}
        if ("delOvertimeParkRecords".equals(methodName) || "delStaytimeParkRecords".equals(methodName) ||
                "getGridCrossCounts".equals(methodName) || "getLastestVehiclePassInArea".equals(methodName) ||
                "getMileageAndOilData".equals(methodName) || "stagnationTimeoutCancelOrNot".equals(methodName)||"calculateMileageConsumption".equals(methodName)) {
            return;
        }
        long startTime = 0;
        long endTime = 0;
        /*
		 * 瓦片ID查询车次、网格车次检索
		 */
        if ("getVehiclePassTimesRecords".equals(methodName) || "getVehiclePassTimesBytileId".equals(methodName)) {
            startTime = (Long) result.get(1);
            endTime = (Long) result.get(2);
            VertifyDateTime(startTime, endTime);
            return;
        }
		/*
		 * 区域车次、
		 */
        if ("getVehiclePassInArea".equals(methodName)) {
            startTime = (Long) result.get(2);
            endTime = (Long) result.get(3);
            VertifyDateTime(startTime, endTime);
            return;
        }
        // 终端列表
        List<Long> terminalIds = (List<Long>) result.get(0);
        if (null == terminalIds) {
            throw new Fault(new SOAPException("终端参数不能为空，请检查！！"));
        }
        log.info("terminalIds:" + terminalIds.size());

        int type = -1;
        CommonParameter commonParameter = null;
        String mdKey = "";
        // 驾驶员登陆
        if (methodName.equals("getDriverLoginRecords")) {
            startTime = (Long) result.get(2);
            endTime = (Long) result.get(3);
            VertifyDateTime(startTime, endTime);
            commonParameter = null;
            if (result != null && result.size() == 5) {
                commonParameter = (CommonParameter) result.get(4);
            } else {
                commonParameter = (CommonParameter) result.get(3);
            }
            // 计算MD5key
            mdKey = computerQueryKey(methodName, terminalIds, startTime,
                    endTime, commonParameter, type);
            if (endTime > System.currentTimeMillis() / 1000) {
                endTime = System.currentTimeMillis() / 1000;
                result.set(2, endTime);
            }
        } else if (methodName.equals("getOvertimeParkRecords")||methodName.equals("getStaytimeParkRecords")) {
            startTime = (Long) result.get(2);
            endTime = (Long) result.get(3);
            VertifyDateTime(startTime, endTime);
            commonParameter = null;
            if (result != null && result.size() == 5) {
                commonParameter = (CommonParameter) result.get(4);
            } else {
                commonParameter = (CommonParameter) result.get(3);
            }
            // 计算MD5key
            mdKey = computerQueryKey(methodName, terminalIds, startTime,
                    endTime, commonParameter, type);
            if (endTime > System.currentTimeMillis() / 1000) {
                endTime = System.currentTimeMillis() / 1000;
                result.set(3, endTime);
            }
        } else if (methodName.equals("getFaultCodeRecords")) {
            startTime = (Long) result.get(3);
            endTime = (Long) result.get(4);
            VertifyDateTime(startTime, endTime);
            if (result != null && result.size() == 6) {
                commonParameter = (CommonParameter) result.get(5);
            } else {
                commonParameter = (CommonParameter) result.get(4);
            }
            // 计算MD5key
            mdKey = computerQueryKey(methodName, terminalIds, startTime,
                    endTime, commonParameter, type);
            if (endTime > System.currentTimeMillis() / 1000) {
                endTime = System.currentTimeMillis() / 1000;
                result.set(4, endTime);
            }
        } else {
            startTime = (Long) result.get(1);
            endTime = (Long) result.get(2);
            VertifyDateTime(startTime, endTime);
            if (result != null && result.size() == 5) {
                type = (Integer) result.get(3);
                commonParameter = (CommonParameter) result.get(4);
            } else {
                commonParameter = (CommonParameter) result.get(3);
            }
            // 计算MD5key
            mdKey = computerQueryKey(methodName, terminalIds, startTime,
                    endTime, commonParameter, type);
            if (endTime > System.currentTimeMillis() / 1000) {
                endTime = System.currentTimeMillis() / 1000;
                result.set(2, endTime);
            }
        }

        commonParameter.setQueryKey(mdKey);
//		DSANodeRequester requester = new DSANodeRequester();
//		// 获取可用的DSA节点
//		long dsaNode = requester.getDSANode(terminalIds, startTime, endTime,
//				commonParameter, mdKey);
//		if (dsaNode == 0) {
//			log.error("获取到的DSA节点编号是0.");
//		}
//		long da = NodeList.getNodeCodeByType(NodeType.da);
//		commonParameter.setCode(da);
		/*DSANodeCache dsaNodeCache = DSANodeCache.getInstance();
		long dsaNodeCode = dsaNodeCache.getDSANodeCode();
		log.info("DSA_NodeCode:"+dsaNodeCode);
		commonParameter.setCode(dsaNodeCode);*/
        message.setContent(CommonParameter.class, commonParameter);
    }

    private void VertifyDateTime(long startTime, long endTime) throws Fault {
        if (startTime >= endTime) {
            throw new Fault(new SOAPException("开始时间大于结束时间，请检查！！"));
        }
    }

    /**
     * 计算MD5key
     *
     * @param methodName
     * @param terminalIds
     * @param startTime
     * @param endTime
     * @param commonParameter
     * @param type
     * @return
     */
    private String computerQueryKey(String methodName, List<Long> terminalIds,
                                    long startTime, long endTime, CommonParameter commonParameter,
                                    int type) {
        // 生成KEY
        StringBuffer sb = new StringBuffer();
        for (long tid : terminalIds) {
            sb.append(tid);
        }
        sb.append(startTime);
        sb.append(endTime);
        if (type != -1) {
            sb.append(type);
        }
        sb.append(commonParameter.getAccessTocken());
        sb.append(methodName);
        return MD5.computeMd5(sb.toString());
    }
}
