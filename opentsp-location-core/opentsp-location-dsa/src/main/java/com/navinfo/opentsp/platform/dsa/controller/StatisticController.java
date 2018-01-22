package com.navinfo.opentsp.platform.dsa.controller;

import com.navinfo.opentsp.platform.dsa.schedule.AppTaskSched;
import com.navinfo.opentsp.platform.dsa.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanhk on 2017/4/11.
 */
@RestController
@RequestMapping("/location/statistic")
public class StatisticController {

    private static Map<String,String> cacheMap = new HashMap<>();

    @Autowired
    private AppTaskSched appTaskSched;

    /**
     * redis转存mongo接口触发
     * @return
     */
    @RequestMapping(value = "/saveGpsDataTask",produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public RequestResult saveGpsDataTask(){
        RequestResult requestResult = new RequestResult();
        try {
            String day = DateUtils.date2String(DateUtils.getCurrentDate(),DateUtils.YYYYMMDD);
            if(!cacheMap.containsKey(day)) {
                //修改数据转存实现方式为LTS
                //appTaskSched.saveGpsDataTask();
                cacheMap.put(day,day);
                requestResult.setMessage("ok");
            } else {
                requestResult.setMessage("is running...");
            }
        } catch (Exception e) {
            requestResult.setMessage(e.getMessage());
            return requestResult;
        }
        return requestResult;
    }

    /**
     * 区域车次接口触发
     * @return
     */
    @RequestMapping(value = "/schedOffline",produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public RequestResult schedOffline(){
        RequestResult requestResult = new RequestResult();
        try {
            appTaskSched.schedOffline();
            requestResult.setMessage("ok");
        } catch (Exception e) {
            requestResult.setMessage(e.getMessage());
            return requestResult;
        }
        return requestResult;
    }
}
