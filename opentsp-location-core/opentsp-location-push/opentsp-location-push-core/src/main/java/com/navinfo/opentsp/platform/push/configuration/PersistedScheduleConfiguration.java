package com.navinfo.opentsp.platform.push.configuration;

import com.navinfo.opentsp.platform.push.impl.TerminalServiceImpl;
import com.navinfo.opentsp.platform.push.persisted.*;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Config of persisted storage for schedules.
 */
@ComponentScan(basePackageClasses = {PersistedScheduleStorage.class})
@EnableJpaRepositories(basePackageClasses = {DeliveringsRepository.class})
@EnableTransactionManagement
@EntityScan(basePackageClasses = {TaskEntry.class})
@Configuration
public class PersistedScheduleConfiguration {


    @Bean(name = "transactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new JpaTransactionManager();
    }
}
