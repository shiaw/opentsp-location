package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.web;

import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.StatisticsUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class StatisticsController {

    //statistics
    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> qpsmap() {
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("808接收到的包总数", StatisticsUtils.sendTotalPacketNum808.get());
        model.put("808发送到kafka成功的包总数", StatisticsUtils.sendTotalPacketNum808_kafka.get());
        model.put("808发送到kafka失败的包总数", StatisticsUtils.sendTotalPacketNum808_kafka_fail.get());
        model.put("内部接收到的包总数", StatisticsUtils.sendTotalPacketNumInner.get());
        model.put("内部发送到kafka成功的包总数", StatisticsUtils.sendTotalPacketNumInner_kafka.get());
        model.put("内部发送到kafka失败的包总数", StatisticsUtils.sendTotalPacketNumInner_kafka_fail.get());
        model.put("当前时间", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
        return model;
    }

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ResponseBody
//    public Map<String, String> test() {
//        return Collections.singletonMap("message", "hello world");
//    }

}
