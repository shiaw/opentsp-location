
package com.navinfo.opentsp.platform.da.core.acceptor.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeInfo.NodeInfo;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeType.NodeType;
import com.navinfo.opentsp.platform.da.core.cache.NodeList;

public class MutualSessionManage {
    // 链路缓存，Key链路ID，Value自定义链路对象
    private static Map<Long, MutualSession> sessionMap = new ConcurrentHashMap<Long, MutualSession>();
    // 节点编号与链路ID映射缓存，Key节点编号，Value链路ID
    private static Map<Long, Long> sessionMapping = new ConcurrentHashMap<Long, Long>();
    private static Logger logger = LoggerFactory.getLogger(MutualSessionManage.class);
    private static MutualSessionManage instance = new MutualSessionManage();
    private MutualSessionManage() {
    }

    public static Map<Long, Long> getSessionMapping() {
        return sessionMapping;
    }

    public static long getNodeCodeBySessionId(long sessionId){
        for(Map.Entry<Long, Long> entity : sessionMapping.entrySet()){
            if(entity.getValue() == sessionId){
                return entity.getKey();
            }
        }
        return 0;
    }

    public final static MutualSessionManage getInstance() {
        return instance;
    }

    /**
     * 添加一个链路对象
     *
     * @param key：链路ID
     * @param mutualSession
     * @return
     */
    public MutualSession addMutualSession(Long key, MutualSession mutualSession) {
        logger.info("bind mutualSession:"+key+"--->"+mutualSession);
        return sessionMap.put(key, mutualSession);
    }

    /**
     * 根据链路ID移除一个链路对象
     *
     * @param key
     * @return
     */
    public boolean delMutualSession(Long key) {
        MutualSession mutualSession = sessionMap.get(key);
        if (mutualSession != null) {
            if (mutualSession.getIoSession() != null && !mutualSession.getIoSession().isClosing())
                mutualSession.getIoSession().close(true);
        }
        logger.info("remove Session:"+key);
        return sessionMap.remove(key) != null;
    }

    /**
     * 根据链路ID获取一个链路对象
     *
     * @param key
     * @return
     */
    public MutualSession getMutualSessionForSessionId(long key) {
        return sessionMap.get(key);
    }
    /**
     * 根据节点类型获取节点的Session
     * @param nodeType
     * @return
     */
    public List<MutualSession> getSessionListByNodeType(NodeType nodeType){
        List<MutualSession> result = new ArrayList<MutualSession>();
        for (Entry<Long, Long> e : sessionMapping.entrySet()) {
            long nodeCode = e.getValue();
            NodeInfo nodeInfo = NodeList.get(nodeCode);
            if(nodeInfo != null){
                if(nodeType.getNumber() == nodeInfo.getTypes().getNumber()){
                    result.add(sessionMap.get(nodeCode));
                }
            }
        }
        return result;
    }
    /**
     * 根据节点类型获取全部节点编号
     * @param nodeType
     * @return
     */
    public List<Long> getNodeListByNodeType(NodeType nodeType){
        List<Long> result = new ArrayList<Long>();
        for (Entry<Long, Long> e : sessionMapping.entrySet()) {
            long nodeCode = e.getKey();
            NodeInfo nodeInfo = NodeList.get(nodeCode);
            if(nodeInfo != null){
                if(nodeType.getNumber() == nodeInfo.getTypes().getNumber()){
                    result.add(nodeCode);
                }
            }
        }
        return result;
    }

    /**
     * 获取服务的所有链路列表
     *
     * @return
     */
    public Map<Long, MutualSession> get() {
        Map<Long, MutualSession> temp = sessionMap;
        return temp;
    }

    /////////////////////节点编号与链路映射管理///////////////////////////
    public void bind(long nodeCode , long sessionId){
        logger.info("bind Nodecode-SessionId:"+nodeCode+"--->"+sessionId);
        sessionMapping.put(nodeCode, sessionId);
    }
    public void unbind(long nodeCode){
        logger.info("unbind Nodecode-SessionId:"+nodeCode);
        sessionMapping.remove(nodeCode);
    }
    /**
     * 根据节点编号获取链路
     * @param nodeCode
     * @return
     */
    public MutualSession getMutualSessionForNodeCode(long nodeCode){
        Long sessionId = sessionMapping.get(nodeCode);
        if(sessionId != null){
            return this.getMutualSessionForSessionId(sessionId);
        }
        logger.info("找不到"+nodeCode+"对应的sessionID");
        return null;
    }
}
