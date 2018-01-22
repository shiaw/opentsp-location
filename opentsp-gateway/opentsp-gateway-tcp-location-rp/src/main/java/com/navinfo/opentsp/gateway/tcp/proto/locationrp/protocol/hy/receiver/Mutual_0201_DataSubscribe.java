package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.TerminalManage;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.SubscribeEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.TerminalEntity;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty.NettyChannelMap;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.platform.LCDataSubscribe;
import com.navinfo.opentsp.platform.location.protocol.platform.LCDataSubscribeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据订阅
 */
@LocationCommand(id = "0201")
public class Mutual_0201_DataSubscribe extends RPCommand {
    private static final Logger log = LoggerFactory.getLogger(Mutual_0201_DataSubscribe.class);

    @Value("${opentsp.cache.terminalCacheKey:com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage:0}")
    private String terminalCacheKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            LCDataSubscribe.DataSubscribe dataSubscribe = LCDataSubscribe.DataSubscribe.parseFrom(packet.getContent());
            List<Long> terminals = dataSubscribe.getTerminalIdentifyList();
            List<Long> failure_terminals = new ArrayList<>();
            log.error("业务系统请求订阅 [" + terminals.size() + "] 个终端");
            for (Long terminalId : terminals) {
                String json = stringRedisTemplate.opsForValue().get(terminalCacheKey + terminalId);
                long id = 0;
                if (!StringUtils.isEmpty(json)) {
                    JSONObject jobj = JSONObject.parseObject(json);
                    id = jobj.getLong("terminalId");
                }
                TerminalEntity terminalInfo = null;
                if (id != 0) {
                    terminalInfo = new TerminalEntity();
                    terminalInfo.setTerminalId(id);
                }
                //TerminalEntity terminalInfo = TerminalManage.getInstance().getTerminal(terminalId);
                if (terminalInfo != null) {
                    SubscribeEntry subscribeEntry = TerminalManage.getInstance().getSubscribe(terminalId + connection.getId());
                    if (subscribeEntry == null) {
                        subscribeEntry = new SubscribeEntry();
                        subscribeEntry.setTerminalId(terminalId + connection.getId());
                        subscribeEntry.dataSubscribe(connection.getId());
                    } else {
                        subscribeEntry.dataSubscribe(connection.getId());
                    }
                    TerminalManage.getInstance().updateSubscribe(subscribeEntry);
                    log.error("业务系统终端 [" + terminalId + "] 订阅成功");
                } else {
                    failure_terminals.add(terminalId);
                }
            }
            for (Long terminalId : failure_terminals) {
                log.error("业务系统终端[" + terminalId + "] 订阅失败 **************");
            }
            log.error("业务系统请求订阅 [" + terminals.size() + "] 个终端,失败 [" + failure_terminals.size() + "] 个");
            //构建响应数据包
            Packet _out_packet = new Packet(true);
            _out_packet.setCommand(LCAllCommands.AllCommands.Platform.DataSubscribeRes_VALUE);
            _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _out_packet.setTo(packet.getFrom());
            _out_packet.setUniqueMark(packet.getUniqueMark());
            LCDataSubscribeRes.DataSubscribeRes.Builder builder = LCDataSubscribeRes.DataSubscribeRes.newBuilder();
            builder.setSerialNumber(packet.getSerialNumber());
            if (failure_terminals.size() == 0) {
                builder.setResult(true);
            } else {
                builder.setResult(false);
                builder.addAllTerminalIdentify(failure_terminals);
            }
            _out_packet.setContent(builder.build().toByteArray());
            return _out_packet;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

}
