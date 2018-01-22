package com.navinfo.opentsp.gateway.tcp.proto.location;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * User: zhanhk
 * Date: 16/8/8
 * Time: 上午11:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedisConf.class)
@WebAppConfiguration
@ActiveProfiles({"no-auth","security-stub", "dev"})
@TestPropertySource(properties = {
        "url.permit.all=/**"})
@IntegrationTest("server.port:0")
public class RedisSerializerTest {

    @Autowired
    private RedisTemplate lcRedisTemplate;

    @Before
    public void setup() throws Exception {
        initMocks(this);

    }

    @After
    public void reset_mocks() {

    }




    @Test
    public void testRedisSerializer() throws Exception {
        TerminalInfo terminalInfo = new TerminalInfo();
        terminalInfo.setAuthCode("1111");
        long time = 0;
        for(int i = 0; i<5000 ; i++) {
            long s = System.currentTimeMillis();
            lcRedisTemplate.opsForHash().put("aaaa","1",terminalInfo);
            TerminalInfo result = (TerminalInfo) lcRedisTemplate.opsForHash().get("aaaa","1");
            result.getAuthCode();
            lcRedisTemplate.delete("aaaa");

            time += System.currentTimeMillis()-s;
        }
        System.out.println("--------------------"+time);
    }

}
