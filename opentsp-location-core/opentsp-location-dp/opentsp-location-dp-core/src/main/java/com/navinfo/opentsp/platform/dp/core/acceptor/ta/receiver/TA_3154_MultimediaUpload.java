package com.navinfo.opentsp.platform.dp.core.acceptor.ta.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.dp.core.handler.SendKafkaHandler;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMultimediaDataSave.MultimediaDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMultimediaUpload.MultimediaUpload;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@KafkaConsumerHandler(topic = "posraw",commandId = "3154")
public class TA_3154_MultimediaUpload  extends AbstractKafkaCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(TA_3154_MultimediaUpload.class);

    protected TA_3154_MultimediaUpload() {
        super(KafkaCommand.class);
    }

    @Autowired
    private SendKafkaHandler sendKafkaHandler;

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(),Packet.class);
            log.info("TA --> DP SUCCESS!{}",packet.toString());
            //转发DA层
            MultimediaDataSave.Builder builder = MultimediaDataSave.newBuilder();
            builder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
            builder.setMultimediaData(MultimediaUpload.parseFrom(packet.getContent()));
            Packet _outpacket_da = new Packet(true);
            _outpacket_da.setCommand(AllCommands.DataAccess.MultimediaDataSave_VALUE);
            _outpacket_da.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _outpacket_da.setUniqueMark(NodeHelper.getNodeUniqueMark());
            _outpacket_da.setContent(builder.build().toByteArray());
            sendKafkaHandler.writeKafKaToDA(_outpacket_da);
        } catch (IOException e) {
            log.error("数据格式错误!",e);
        }
    }
}
