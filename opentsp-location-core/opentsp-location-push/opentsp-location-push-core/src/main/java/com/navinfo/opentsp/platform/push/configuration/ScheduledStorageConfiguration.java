package com.navinfo.opentsp.platform.push.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navinfo.opentsp.platform.push.api.ScheduledTasksStorage;
import com.navinfo.opentsp.platform.push.impl.InMemoryScheduleStorage;
import com.navinfo.opentsp.platform.push.impl.ScheduleStateWatcher;
import com.navinfo.opentsp.platform.push.impl.ScheduledTasksStorageImpl;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Configuration for composite scheduled storage
 */
@Configuration
public class ScheduledStorageConfiguration {

    @Bean
    InMemoryScheduleStorage inMemoryScheduleStorage(RedisConnectionFactory factory, ObjectMapper objectMapper, ScheduleStateWatcher watcher) {
        return new InMemoryScheduleStorage(factory, objectMapper, watcher);
    }

    @Primary
    @Bean
    ScheduledTasksStorage storage(InMemoryScheduleStorage inMemoryStorage) {
        return new ScheduledTasksStorageImpl(inMemoryStorage);
    }
}
