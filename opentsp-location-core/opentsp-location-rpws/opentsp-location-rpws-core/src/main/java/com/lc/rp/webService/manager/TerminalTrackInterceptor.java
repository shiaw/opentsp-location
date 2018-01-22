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

public class TerminalTrackInterceptor extends AbstractSoapInterceptor {

    // logger
    Logger log = LoggerFactory.getLogger(TerminalTrackInterceptor.class);

    public TerminalTrackInterceptor() {
        // 指定该拦截器在执行前阶段被激发
        super(Phase.PRE_INVOKE);
    }

    //	@SuppressWarnings("unchecked")
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
//		long start = System.currentTimeMillis() / 1000;
        // 获取webservice的入参
        List<Object> result = MessageContentsList.getContentsList(message);
        QName qName = (QName) message.get(SoapMessage.WSDL_OPERATION);
        String methodName = qName.getLocalPart();
        //	log.info("----调用RP-ws接口--->:" + methodName + "时间： "+ DateUtils.format(start, DateFormat.YY_YY_MM_DD_HH_MM_SS));
        if ("getTerminalTrack".equals(methodName)) {
            long terminalIds = (Long) result.get(0);
            long startTime = (Long) result.get(1);
            long endTime = (Long) result.get(2);
            if (startTime >= endTime) {
                throw new Fault(new SOAPException("开始时间大于结束时间，请检查！！"));
            }
            /*int isFilterAlarm = 1;
			int isAllAlarm = 1;

			if (result.get(3).equals("true")) {
				isFilterAlarm = 0;
			}
			if (result.get(4).equals("true")) {
				isAllAlarm = 0;
			}
			long alarms = (Long) result.get(5);*/
            int isFilterBreakdown = 1;
            if ((Boolean) result.get(3) == true) {
                isFilterBreakdown = -1;
            }
            long breakdownCode = (Long) result.get(4);
            boolean isThin = (Boolean) result.get(5);
            int level = (Integer) result.get(6);
            int isAll = (Integer)result.get(7);
            CommonParameter commonParameter = (CommonParameter) result.get(8);
            if (endTime > System.currentTimeMillis() / 1000) {
                endTime = System.currentTimeMillis() / 1000;
                result.set(2, endTime);
            }
            if (commonParameter.isMultipage() == true) {
                if (commonParameter.getPageSize() > 5000) {
                    commonParameter.setPageSize(5000);
                }
            }
            // 计算MD5key
            String mdKey = computerQueryKey(methodName + terminalIds
                    + startTime + endTime
                    + isFilterBreakdown + breakdownCode
                    + commonParameter.getAccessTocken());
            commonParameter.setQueryKey(mdKey);
			/*DSANodeRequester requester = new DSANodeRequester();
			// 获取可用的DSA节点
			long dsaNode = requester.getDSANode(null, startTime, endTime,
					commonParameter, mdKey);*/
//			DSANodeCache dsaNodeCache = DSANodeCache.getInstance();
//			long dsaNodeCode = dsaNodeCache.getDSANodeCode();
            //log.info("DSA_NodeCode:"+dsaNode);
            //commonParameter.setCode(dsaNode);
//			long da = NodeList.getNodeCodeByType(NodeType.da);
//			commonParameter.setCode(da);
            message.setContent(CommonParameter.class, commonParameter);
        }

    }

    public static void main(String[] args) {
        String ss = computerQueryKey("dkfjasldjfl1212324324124asjdfsad4444444444444433234ljfsa");
        System.err.println(ss);
    }

    /**
     * 计算MD5key
     *
     * @param key
     * @return
     */
    private static String computerQueryKey(String key) {
        return MD5.computeMd5(key);
    }
}
