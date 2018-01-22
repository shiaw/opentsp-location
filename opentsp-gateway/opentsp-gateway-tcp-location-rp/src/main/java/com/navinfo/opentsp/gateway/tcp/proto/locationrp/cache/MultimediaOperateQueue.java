package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultimediaOperateQueue {
    static Map<String, LinkedList<String>> queue = new ConcurrentHashMap<String, LinkedList<String>>();

    /**
     * 向当前终端拍照队列末尾添加一个元素
     *
     * @param terminal {@link String} 终端标识
     * @param upper    {@link String} 上层业务系统标识
     */
    public static void addMultimediaOperate(String terminal, String upper) {
        LinkedList<String> linkedList = queue.get(terminal);
        if (linkedList == null) {
            linkedList = new LinkedList<String>();
        }
        linkedList.offerFirst(upper);
        queue.put(terminal, linkedList);
    }

    /**
     * 获取最先对当前拍照的业务系统标识
     *
     * @param terminal {@link String} 终端标识
     * @return {@link String} 业务系统标识
     */
    public static String getMultimediaOperateUpper(String terminal) {
        LinkedList<String> linkedList = queue.get(terminal);
        if (linkedList != null) {
            String upper = linkedList.pollFirst();
            if (linkedList.size() == 0) {
                queue.remove(terminal);
            } else {
                queue.put(terminal, linkedList);
            }
            return upper;
        }
        return null;
    }

    public static int size() {
        return queue.size();
    }
}
