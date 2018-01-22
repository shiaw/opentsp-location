package com.navinfo.opentsp.platform.da.core.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/29.
 * @author gx
 * 测试使用，生产环境不启用
 */
public class KafkaProducerConfiguration {

    @Bean
    public Map<String, Object> dpProducerConfigs() {
        Map<String, Object> props = new HashMap<>(8);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "sy-lp1:9092,sy-lp2:9092,sy-lp3:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 2000);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.navinfo.opentspcore.common.kafka.serializers.KafkaJsonSerializer");
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        return props;
    }
    @Bean
    public ProducerFactory<String, Object> dpProducerFactory() {
        return new DefaultKafkaProducerFactory<>(dpProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> dpKafkaTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(dpProducerFactory());
        kafkaTemplate.setDefaultTopic("daposdonenew");
        return kafkaTemplate;
    }
}
