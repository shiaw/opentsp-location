package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help.InOutMark;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("rule_170160_Status")
public class Rule_170160_Status implements Serializable, RuleStatus {
    private static final long serialVersionUID = 1L;

    @Resource
    private IRedisService redisService;

    @Value("${rule.process.limit:300}")
    private int ruleProcessLimit;

    private Map<String, Rule_170160StatusEntry> cache = new ConcurrentHashMap<String, Rule_170160StatusEntry>();

    public static class Rule_170160StatusEntry {
        //对应某个区域的状态
        private InOutMark inOutMark;
        //区域类型
        private int areaType;
        //时间戳
        private long timestemp;
        //更新时间
        private long updTime;

        public Rule_170160StatusEntry() {
            super();
        }

        public int getAreaType() {
            return areaType;
        }

        public void setAreaType(int areaType) {
            this.areaType = areaType;
        }

        public InOutMark getInOutMark() {
            return inOutMark;
        }

        public void setInOutMark(InOutMark inOutMark) {
            this.inOutMark = inOutMark;
        }

        public long getTimestemp() {
            return timestemp;
        }

        public void setTimestemp(long timestemp) {
            this.timestemp = timestemp;
        }

        public long getUpdTime() {
            return updTime;
        }

        public void setUpdTime(long updTime) {
            this.updTime = updTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Rule_170160StatusEntry entry = (Rule_170160StatusEntry) o;

            return inOutMark == entry.inOutMark;
        }

        @Override
        public int hashCode() {
            return inOutMark != null ? inOutMark.hashCode() : 0;
        }
    }

    /*******************
     * 获取最近一次位置数据状态
     *
     * @param areaId
     * @param areaType
     * @return
     */
    public Rule_170160StatusEntry getLastStatus(long terminalId, long areaId, int areaType) {
        String key = terminalId + "_" + areaId + "_" + areaType;
        Rule_170160StatusEntry entry = cache.get(key);
        if (entry == null) {
            entry = redisService.
                    getHashValue(RuleEum.R170160.getMapKey(), key, Rule_170160StatusEntry.class);
            if (entry != null) {
                cache.put(key, entry);
            }
        }
        return entry;
    }


    /**
     * 删除状态结果
     *
     * @param terminalId
     */
    public void del(long terminalId) {
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, Rule_170160StatusEntry> entry : cache.entrySet()) {
            if (entry.getKey().contains(Long.toString(terminalId))) {
                keys.add(entry.getKey());
            }
        }

        for (String key : keys) {
            cache.remove(key);
            redisService.delete(RuleEum.R170160.getMapKey(), key);
        }
    }


    /**
     * 设置区域的最新状态
     *
     * @param gpsDate
     * @param areaId
     * @param areaType
     * @param inOutStatus
     */
    public void updateLatestStatus(long gpsDate, long terminalId, long areaId, int areaType, InOutMark inOutStatus) {
        Rule_170160StatusEntry statusEntry = this.getLastStatus(terminalId, areaId, areaType);
        String key = terminalId + "_" + areaId + "_" + areaType;
        if (statusEntry == null) { //首次创建缓存标记
            statusEntry = new Rule_170160StatusEntry();
            statusEntry.setInOutMark(inOutStatus);
            statusEntry.setAreaType(areaType);
            statusEntry.setTimestemp(gpsDate);
            statusEntry.setUpdTime(DateTime.now().getMillis());
            cache.put(key, statusEntry);
            redisService.hset(RuleEum.R170160.getMapKey(), key, statusEntry);
        } else {//更新最新的缓存状态
            statusEntry.setInOutMark(inOutStatus);
            statusEntry.setAreaType(areaType);
            statusEntry.setTimestemp(gpsDate);
            //如果状态发生变化
            if (statusEntry.getInOutMark() != inOutStatus) {
                statusEntry.setUpdTime(DateTime.now().getMillis());
                redisService.hset(RuleEum.R170160.getMapKey(), key, statusEntry);
            } else {
                long period = DateTime.now().getMillis() - statusEntry.getUpdTime();
                //如果到达同步时间阈值
                if (period > (ruleProcessLimit * 1000)) {
                    statusEntry.setUpdTime(DateTime.now().getMillis());
                    redisService.hset(RuleEum.R170160.getMapKey(), key, statusEntry);
                }
            }
            cache.put(key, statusEntry);
        }
    }

    @Override
    public void alarmHandle() {

    }

    public static void main(String[] args) {
        System.out.println(DateTime.now().getMillis());
    }
}
