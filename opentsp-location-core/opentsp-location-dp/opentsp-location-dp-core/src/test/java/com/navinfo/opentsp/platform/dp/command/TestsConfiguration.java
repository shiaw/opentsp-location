package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.transport.direct.DirectResultHolder;
import com.navinfo.opentsp.common.utils.ExecutorUtils;
import com.navinfo.opentsp.platform.configuration.CacheConfiguration;
import com.navinfo.opentsp.platform.configuration.SecurityApplicationConfiguration;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.handler.PacketHandler;
import com.navinfo.opentspcore.common.CoreCommandHandler;
import com.navinfo.opentspcore.common.dispatching.CommandsExecutor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import java.util.concurrent.Executor;

@Configuration
@EnableAutoConfiguration
@Import({SecurityApplicationConfiguration.class, CacheConfiguration.class})
@ComponentScan(lazyInit = true, basePackageClasses = {
        CoreCommandHandler.class,
        DirectResultHolder.class
        } ,basePackages = "com.navinfo.opentsp.platform.dp.core")
public class TestsConfiguration {
    @Bean(name = CommandsExecutor.EXECUTOR_QUALIFIER)
    Executor executor() {
        return ExecutorUtils.DIRECT;
    }
}
