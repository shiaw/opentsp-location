package com.navinfo.opentsp.platform.dp.core.configuration;

import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentspcore.common.kafka.KafkaConsumerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
@Profile({"spring-kafka"})
@Configuration
public class SpringKafkaConfig {
        private static final Logger logger = LoggerFactory.getLogger(com.navinfo.opentspcore.common.kafka.KafkaConfig.class);
        @Autowired
        private ApplicationContext context;
        @Value("${opentsp.kafka.bootstrap.servers:localhost:9092}")
        private String bootstrapServers;
        @Value("${opentsp.kafka.producer.bootstrap.servers:}")
        private String producerServers;
        @Value("${opentsp.kafka.ssl.protocol:TLS}")
        private String sslProtocolConfig;
        @Value("${opentsp.kafka.ssl.truststore.password:}")
        private String sslTruststorePassword;
        @Value("${opentsp.kafka.ssl.truststore.location:}")
        private String sslTruststoreLocation;
        @Value("${opentsp.kafka.ssl.keystore.location:}")
        private String sslKeystoreLocation;
        @Value("${opentsp.kafka.ssl.keystore.password:}")
        private String sslKeystorePassword;
        @Value("${opentsp.kafka.producer.key.serializer.class:kafka.serializer.StringEncoder}")
        private String producerKeySerializerClass;
        @Value("${opentsp.kafka.producer.value.serializer.class:kafka.serializer.StringEncoder}")
        private String producerValueSerializerClass;
        @Value("${opentsp.kafka.producer.batch.size:200}")
        private int batchSize;
        @Value("${opentsp.kafka.producer.buffer.memory:33554432}")
        private int bufferMemory;
        @Value("${opentsp.kafka.producer.request.timeout.ms:30000}")
        private int requestTimeoutMs;
        @Value("${opentsp.kafka.producer.retries:0}")
        private int retries;
        @Value("${opentsp.kafka.producer.linger.ms:1}")
        private int lingerMs;
        @Value("${opentsp.kafka.producer.compression.type:none}")
        private String compressionType;
        @Value("${opentsp.kafka.consumer.concurrency.size:1}")
        private int concurrencySize;
        @Value("#{'${opentsp.kafka.consumer.listener.topics}'.split(',')}")
        private String[] topics;
        @Value("${opentsp.kafka.consumer.session.timeout.ms:15000}")
        private String sessionTimeoutMs;
        @Value("${opentsp.kafka.consumer.enable.auto.commit:true}")
        private boolean enableAutoCommit;
        @Value("${opentsp.kafka.consumer.auto.commit.interval.ms:500}")
        private String autoCommitIntervalMs;
        @Value("${opentsp.kafka.consumer.key.serializer.class:kafka.serializer.StringEncoder}")
        private String consumerKeySerializerClass;
        @Value("${opentsp.kafka.consumer.value.serializer.class:kafka.serializer.StringEncoder}")
        private String consumerValueSerializerClass;
        @Value("${opentsp.kafka.consumer.group.id:defalut_group}")
        private String groupId;
        @Value("${opentsp.kafka.consumer.fetch.max.bytes:52428800}")
        private String fetchmaxBytes;
        @Value("${opentsp.kafka.consumer.fetch.min.bytes:1}")
        private String fetchMinBytes;
        @Value("${opentsp.kafka.consumer.fetch.max.wait.ms:500}")
        private String fetchmaxWaitMs;
        @Value("${opentsp.kafka.consumer.max.partition.fetch.bytes:1048576}")
        private String maxPartitionFetchBytes;
        @Value("${opentsp.kafka.consumer.auto.offset.reset:latest}")
        private String autoOffsetReset;
        @Value("${opentsp.kafka.acktime:5000}")
        private long acktime;
        @Value("${opentsp.kafka.queue.depth:1000}")
        private int queueDepth;
        @Autowired
        private KafkaConsumerListener kafkaConsumerListener;

        public SpringKafkaConfig() {
        }

        @Bean
        public ConsumerFactory<String, Object> consumerFactory() {
            return new DefaultKafkaConsumerFactory(this.consumerConfigs());
        }

        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap();
            props.put("bootstrap.servers", this.bootstrapServers);
            props.put("group.id", this.groupId);
            props.put("auto.commit.interval.ms", this.autoCommitIntervalMs);
            props.put("session.timeout.ms", this.sessionTimeoutMs);
            props.put("key.deserializer", this.consumerKeySerializerClass);
            props.put("value.deserializer", this.consumerValueSerializerClass);
            props.put("fetch.min.bytes", this.fetchMinBytes);
            props.put("fetch.max.wait.ms", this.fetchmaxWaitMs);
            props.put("max.partition.fetch.bytes", this.maxPartitionFetchBytes);
            props.put("ssl.protocol", this.sslProtocolConfig);
            props.put("ssl.keystore.location", this.sslKeystoreLocation);
            props.put("ssl.keystore.password", this.sslKeystorePassword);
            props.put("ssl.truststore.location", this.sslTruststoreLocation);
            props.put("ssl.keystore.password", this.sslTruststorePassword);
            props.put("auto.offset.reset", this.autoOffsetReset);
            props.put("enable.auto.commit", this.enableAutoCommit);
            return props;
        }

        @Bean
        public ProducerFactory<String, Object> producerFactory() {
            return new DefaultKafkaProducerFactory(this.producerConfigs());
        }

        @Bean
        public Map<String, Object> producerConfigs() {
            Map<String, Object> props = new HashMap();
            props.put("bootstrap.servers", this.bootstrapServers);
            if (!StringUtils.isEmpty(this.producerServers)) {
                logger.info("load producerServers:{}", this.producerServers);
                props.put("bootstrap.servers", this.producerServers);
            }

            props.put("retries", this.retries);
            props.put("batch.size", this.batchSize);
            props.put("linger.ms", this.lingerMs);
            props.put("buffer.memory", this.bufferMemory);
            props.put("key.serializer", this.producerKeySerializerClass);
            props.put("value.serializer", this.producerValueSerializerClass);
            props.put("request.timeout.ms", this.requestTimeoutMs);
            props.put("compression.type", this.compressionType);
            props.put("ssl.protocol", this.sslProtocolConfig);
            props.put("ssl.keystore.location", this.sslKeystoreLocation);
            props.put("ssl.keystore.password", this.sslKeystorePassword);
            props.put("ssl.truststore.location", this.sslTruststoreLocation);
            props.put("ssl.keystore.password", this.sslTruststorePassword);
            return props;
        }

        @Bean
        public KafkaTemplate<String, Object> kafkaTemplate() {
            return new KafkaTemplate(this.producerFactory());
        }

        @Bean
        public ContainerProperties containerProperties() {
            logger.info("load listener topics :{}", this.topics);
            ContainerProperties containerProperties = new ContainerProperties(this.topics);
            containerProperties.setMessageListener(this.kafkaConsumerListener);
            //containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.TIME);
            //containerProperties.setAckTime(acktime);
            //containerProperties.setQueueDepth(queueDepth);
            return containerProperties;
        }

        @Bean
        public ConcurrentMessageListenerContainer<String, Object> concurrentMessageListenerContainer() {
            ConcurrentMessageListenerContainer<String, Object> factory = new ConcurrentMessageListenerContainer(this.consumerFactory(), this.containerProperties());
            factory.setConcurrency(this.concurrencySize);
            factory.getContainerProperties().setPollTimeout((long)this.requestTimeoutMs);
            return factory;
        }

        @Bean
        public KafkaMessageChannel kafkaMessageChannel(KafkaTemplate<String, Object> kafkaTemplate) {
            logger.info("load KafkaMessageChannel class .....................");
            return new KafkaMessageChannel(kafkaTemplate);
        }

}
