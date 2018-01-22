package com.navinfo.opentsp.platform.dp.core.rule.status;

import com.navinfo.opentsp.platform.dp.core.rule.entity.AreaFilter;
import com.navinfo.opentsp.platform.dp.core.rule.entity.OilStatus;
import com.navinfo.opentsp.platform.dp.core.rule.entity.RouteDriverTimeAddition;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.Rule_170110_Status;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.*;

/**
 * 缓存枚举配置类
 * User: zhanhk
 * Date: 16/8/9
 * Time: 下午2:03
 */
public enum RuleEum {

    //终端id+区域id
    R170100("Rule_170100", Boolean.class),

    //终端id+区域id
    R170110("Rule_170110", Rule_170110_Status.Rule_170110_StatusEntry.class),

    //终端id,上一个时间点的油量值
    R170120("Rule_170120",OilStatus.class),

    //最新一次规则状态<KEY 区域编号+区域类型>
    R170160("Rule_170160", Rule_170160_Status.Rule_170160StatusEntry.class),

    //区域限速规则状态
    R170010("Rule_170010", Rule_170010_Status.Rule_170010_StatusEntry.class),

    //持续记录区域的历史报警状态<KEY 区域编号+区域类型+进出状态>
    R170020_AlarmCache("Rule_170020_AlarmCache", Rule_170020_Status.Rule_170020StatusEntry.class),

    //最新一次规则状态<KEY 区域编号+区域类型>
    R170020_LastStatus("Rule_170020_LastStatus", Rule_170020_Status.Rule_170020StatusEntry.class),

    //路线行驶时间规则状态
    R170030("Rule_170030", Rule_170030_Status.Rule_170030_StatusEntry.class),

    //路线行驶时间规则状态,附加报警信息
    R170030_AlarmCache("Rule_170030_AlarmCache", RouteDriverTimeAddition.class),

    //路线行驶时间规则状态
    R170050("Rule_170050", Rule_170050_Status.Rule_170050_StatusEntry.class),

    //路线行驶时间规则状态
    R170060("Rule_170060", Integer.class),

    //路线行驶时间规则状态
    R170070_AlarmCache("Rule_170070_AlarmCache", Boolean.class),

    //路线行驶时间规则状态
    R170070("Rule_170070", Rule_170070_Status.Rule_170070StatusEntry.class),

    //服务站状态缓存
    R170080_B("Rule_170080_Boolean", Boolean.class),

    //入服务站时间
    R170080_L("Rule_170080_Long", Long.class),

    //终端id+区域id首次进入区域的时间,报警使用
    R170090_ParkCache("Rule_170090_ParkCache", Long.class),//滞留超时

    //终端id+区域id首次进入区域的时间,报警使用
    R170090_ParkCache1("Rule_170090_ParkCache1", Long.class),//区域停留时间

    //终端id+区域id首次进入区域的时间,通知使用
    R170090_ParkNotify("Rule_170090_ParkNotify", Long.class),

    //滞留超时延时缓存,Map<终端id,延时终止时间戳>,延时终止时间戳=延时时间+当前时间
    DelayTimeCache("DelayTimeCache",Long.class),

    //终端进入区域时的时间，通知使用---170190，进出区域通知
    R170190_NotifyCache("LATEST_PARK_DATA_ParkNotify",Object.class),

    //过滤缓存,Map<终端ID，List<服务站屏蔽对象>>
    FilterArea("FilterAreaFilter",AreaFilter[].class),

    //判断如果该终端产生报警 给da发 0994通知 Map<terminalId,List<RegularCode>> alarmTerminals 产生报警终端缓存
    FilterAlarm("FilterAlarm" ,Integer[].class);

    private final String mapKey;

    private final Class classType;

    RuleEum(String mapKey, Class classType) {
        this.mapKey = mapKey;
        this.classType = classType;
    }

    public String getMapKey() {
        return mapKey;
    }

    public Class getClassType() {
        return classType;
    }
}
