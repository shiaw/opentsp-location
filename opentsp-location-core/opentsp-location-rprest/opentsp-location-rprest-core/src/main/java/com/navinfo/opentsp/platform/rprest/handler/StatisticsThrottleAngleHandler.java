package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.StatisticsThrottleAngleCommand;
import com.navinfo.opentsp.platform.rprest.kit.DateUtils;
import com.navinfo.opentsp.platform.rprest.service.StatisticsThrottleAngleService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 油门开度统计分析
 * Created by Sunyu on 2017/11/29.
 */
@Component
public class StatisticsThrottleAngleHandler extends AbstractCommandHandler<StatisticsThrottleAngleCommand, StatisticsThrottleAngleCommand.Result> {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsThrottleAngleHandler.class);

    @Autowired
    private StatisticsThrottleAngleService statisticsThrottleAngleService;

    @Value("${INTERVAL_DATE:30}")
    private int interval;

    public StatisticsThrottleAngleHandler() {
        super(StatisticsThrottleAngleCommand.class, StatisticsThrottleAngleCommand.Result.class);
    }

    @Override
    public StatisticsThrottleAngleCommand.Result handle(StatisticsThrottleAngleCommand command) {
        StatisticsThrottleAngleCommand.Result commandResult = new StatisticsThrottleAngleCommand.Result();
        try {
            if (null == command.getId() || StringUtils.isEmpty(command.getStart()) || StringUtils.isEmpty(command.getEnd())) {
                Date startDate = DateUtils.parse(command.getStart(), DateUtils.DateFormat.YYYY_MM_DD);
                Date endDate = DateUtils.parse(command.getEnd(), DateUtils.DateFormat.YYYY_MM_DD);
                if (startDate != null && endDate != null) {
                    if (DateUtils.compareStartDifferEnd(startDate, endDate, interval)) {
                        Map<String, Object> dataMap = statisticsThrottleAngleService.statistics(startDate, endDate, command.getId());
                        commandResult.setResultCode(ResultCode.OK.code());
                        commandResult.setMessage("");
                        commandResult.setData(dataMap);
                    } else {
                        commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                        commandResult.setMessage("结束日期不小于开始日期并且间隔不能超过" + interval + "天");
                    }
                } else {
                    commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                    commandResult.setMessage("查询日期格式不合法");
                }
            } else {
                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                commandResult.setMessage("查询参数不可以为空");
            }
        } catch (Exception e) {
            logger.error("statisticsThrottleAngle油门开度统计出错！{}", command, e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("服务器错误");
        }
        return commandResult;
    }
}