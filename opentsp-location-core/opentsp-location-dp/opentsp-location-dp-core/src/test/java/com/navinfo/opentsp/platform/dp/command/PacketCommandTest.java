package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.tester.TestMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * PacketCommand测试
 * User: zhanhk
 * Date: 16/9/8
 * Time: 上午11:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestsConfiguration.class)
public class PacketCommandTest {

    @Autowired
    private MessageChannel messageChanel;

    @Test
    public void testContext() throws Exception {
        assertThat(messageChanel, notNullValue());
    }

    @Test
    public void sendPacketCommand() {
        PacketCommand packetCommand = new PacketCommand();
        Packet packet = new Packet();
        packet.setCommand(2305);
        packetCommand.setPacket(packet);
        try {
            CommandResult result = messageChanel.sendAndReceive(packetCommand);
            assertThat(result, TestMatchers.isCommandOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
