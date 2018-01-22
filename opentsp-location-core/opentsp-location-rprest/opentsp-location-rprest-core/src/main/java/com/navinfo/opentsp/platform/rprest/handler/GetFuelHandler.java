package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.common.utils.StringUtils;
import com.navinfo.opentsp.platform.rprest.GetFuelCommand;
import com.navinfo.opentsp.platform.rprest.constant.Constant;
import com.navinfo.opentsp.platform.rprest.entity.MileageFuel;
import com.navinfo.opentsp.platform.rprest.kit.DateUtils;
import com.navinfo.opentsp.platform.rprest.service.MileageFuelService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

/**
 * 油耗里程统计分析handler
 * @Time 2017年11月28日 15:53:40
 * @Author wzw
 *
 */

@Component
public class GetFuelHandler extends AbstractCommandHandler<GetFuelCommand, GetFuelCommand.Result> {

    private static final Logger logger = LoggerFactory.getLogger(GetFuelHandler.class);

    @Autowired
    private MileageFuelService mileageFuelService;

    @Value("${opentsp.mileagefuel.days:30}")
    private int days;

    public GetFuelHandler() {
        super(GetFuelCommand.class, GetFuelCommand.Result.class);
    }

    @Override
    public GetFuelCommand.Result handle(GetFuelCommand command) {
        logger.debug("查询油耗里程统计Handler层开始" + GetFuelHandler.class.getName());
        logger.info(command.toString());
        GetFuelCommand.Result commandResult = new GetFuelCommand.Result();
        try {
            commandResult = checkDate(commandResult,command);
            if(commandResult.getResultCode() == ResultCode.CLIENT_ERROR.code()){
                return commandResult;
            }else {
                List<MileageFuel> list = mileageFuelService.getMileageFuel(command);
                commandResult.setData(list);
                commandResult.setResultCode(ResultCode.OK.code());
            }
        } catch (Exception e) {
            logger.error("查询油耗里程统计出错！{}", command, e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage(ResultCode.SERVER_ERROR.result());
        }
        logger.debug("查询油耗里程统计Handler层结束" + GetFuelHandler.class.getName());
        return commandResult;
    }

    /**
     * 入参校验
     * @param commandResult
     * @param command
     * @return
     */
    private GetFuelCommand.Result checkDate(GetFuelCommand.Result commandResult,GetFuelCommand command){

        logger.error("GetFuelHandler checkDate start");

        Long id = command.getId();
        String start = command.getStart();
        String end = command.getEnd();

        if (null == id) {
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("通信号不能为空");
            return commandResult;
        }
        if(StringUtils.isNull(start)){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("开始时间不能为空");
            return commandResult;
        }
        if(StringUtils.isNull(end)){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("结束时间不能为空");
            return commandResult;
        }
        Date startDate = DateUtils.parse(Constant.DATE_PATTERN, start);
        if(startDate == null){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("开始时间格式不正确");
            return commandResult;
        }
        Date endDate = DateUtils.parse(Constant.DATE_PATTERN, end);
        if(endDate == null){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("结束时间格式不正确");
            return commandResult;
        }
        if(startDate.after(endDate)){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("开始时间不能大于结束时间");
            return commandResult;
        }
        Date tempDate = org.apache.commons.lang3.time.DateUtils.addDays(startDate,days);
        if(tempDate.after(endDate)){
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("开始时间、结束时间相差不能大于"+days+"天");
            return commandResult;
        }
        logger.error("GetFuelHandler checkDate end");
        return commandResult;
    }
}
