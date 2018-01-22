package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.TerminalManage;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.SubscribeEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.TerminalEntity;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.platform.LCDataUnsubscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 取消数据订阅
 */
@LocationCommand(id = "0202")
public class Mutual_0202_DataUnsubscribe extends RPCommand {

    private static final Logger log = LoggerFactory.getLogger(Mutual_0202_DataUnsubscribe.class);

    @Value("${opentsp.cache.terminalCacheKey:com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage:0}")
    private String terminalCacheKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            LCDataUnsubscribe.DataUnsubscribe unsubscribe = LCDataUnsubscribe.DataUnsubscribe.parseFrom(packet.getContent());
            List<Long> terminals = unsubscribe.getTerminalIdentifyList();
            try {
                if (terminals != null) {
                    for (Long terminal : terminals) {
                        String json = stringRedisTemplate.opsForValue().get(terminalCacheKey + terminal);
                        long id = 0;
                        if (!StringUtils.isEmpty(json)) {
                            JSONObject jobj = JSONObject.parseObject(json);
                            id = jobj.getLong("terminalId");
                        }
                        TerminalEntity terminalInfo = null;
                        if (id != 0) {
                            terminalInfo = new TerminalEntity();
                            terminalInfo.setTerminalId(id);
                            SubscribeEntry subscribeEntry = TerminalManage.getInstance().getSubscribe(terminal + connection.getId());
                            if (subscribeEntry != null) {
                                subscribeEntry.dataUnsubscribe(connection.getId());
                            }
                        }
                        //TerminalEntity terminalInfo = TerminalManage.getInstance().getTerminal(terminal);
                        /*if (terminalInfo != null) {
                            SubscribeEntry subscribeEntry = TerminalManage.getInstance().getSubscribe(terminal);
                            if (subscribeEntry != null) {
                                subscribeEntry.dataUnsubscribe(packet.getFrom());
                            }
                        }*/
                    }
                }
                return this.commonResponses(packet.getUniqueMark(),
                        packet.getSerialNumber(), packet.getCommand(),
                        LCPlatformResponseResult.PlatformResponseResult.success, packet.getFrom());
            } catch (Exception e) {
                return this.commonResponses(packet.getUniqueMark(),
                        packet.getSerialNumber(), packet.getCommand(),
                        LCPlatformResponseResult.PlatformResponseResult.failure, packet.getFrom());
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

}
