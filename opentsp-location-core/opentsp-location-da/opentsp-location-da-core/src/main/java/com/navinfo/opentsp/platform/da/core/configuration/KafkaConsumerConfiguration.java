package com.navinfo.opentsp.platform.da.core.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ChenJie
 * @Description:
 * @Date 2017/10/9
 * @Modified by:
 */
@Component
public class KafkaConsumerConfiguration {

    @Value("${opentsp.kafka.bootstrap.servers}")
    protected String bootstrapServers;

    @Value("${spring.kafka.request.timeout.ms:30000}")
    private int requestTimeoutMs;

    @Value("${spring.kafka.session.timeout.ms:15000}")
    protected String sessionTimeoutMs;

    @Value("${spring.kafka.consumer.auto.commit.interval.ms:500}")
    protected String autoCommitIntervalMs;

    @Value("${spring.kafka.consumer.key.serializer.class:kafka.serializer.StringEncoder}")
    protected String consumerKeySerializerClass;

    @Value("${spring.kafka.consumer.value.serializer.class:kafka.serializer.StringEncoder}")
    protected String consumerValueSerializerClass;

    @Value("${spring.kafka.consumer.concurrency.size:1}")
    protected int concurrencySize;

    @Value("${spring.kafka.consumer.isAutoCommit:false}")
    protected boolean isAutoCommit;

    @Value("${spring.kafka.consumer.concentrated.topic:concentratedreport}")
    protected String concentratedTopic;

    @Value("${spring.kafka.consumer.can.cycle.report.topic:statisticreport}")
    protected String canCycleTopic;

    @Value("${spring.kafka.consumer.can.temp.report.topic:faultinforeport}")
    protected String canTempTopic;
    @Value("${spring.kafka.consumer.locationdata.topic}")
    protected String locationDataTopic;

    @Value("${spring.kafka.consumer.groupId}")
    protected String groupId;

    @Value("${spring.kafka.consumber.fetch.min.bytes:102400}")
    protected int maxFetchSize;

    @Value("${spring.kafka.consumer.auto.offset.reset}")
    protected String autoReset;

    @Value("${spring.kafka.consumer.0980.value.serializer.class:com.navinfo.opentspcore.common.kafka.serializers.KafkaJsonDeserializer}")
    protected String consumer0980ValueSerializerClass;
    @Value("${spring.kafka.consumer.0980.groupId}")
    protected String groupId0980;
    @Value("${spring.kafka.bootstrap.0980.servers}")
    protected String bootstrapServer0980;
    @Value("${spring.kafka.consumer.0980.auto.offset.reset}")
    protected String autoReset0980;

    @Autowired
    private KafkaConsumerListener0F37 kafkaConsumerListener0F37;
//
    @Autowired
    private KafkaConsumerListener0F38 kafkaConsumerListener0F38;
//
    @Autowired
    private KafkaConsumerListener0F39 kafkaConsumerListener0F39;

    @Autowired
    private KafkaConsumerListener0980 kafkaConsumerListener0980;

    private ConsumerFactory<String, Object> consumerFactory(String groupId) {
        return new DefaultKafkaConsumerFactory(consumerConfigs(groupId));
    }

    private Map<String, Object> consumerConfigs(String groupId) {
        Map<String, Object> props = new HashMap<>(10);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, isAutoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerKeySerializerClass);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerValueSerializerClass);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, maxFetchSize);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,autoReset);
        return props;
    }

    private ConsumerFactory<String, Object> consumer0980Factory(String groupId) {
        return new DefaultKafkaConsumerFactory(consumer0980Configs(groupId));
    }

    private Map<String, Object> consumer0980Configs(String groupId) {
        Map<String, Object> props = new HashMap<>(10);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer0980);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, isAutoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerKeySerializerClass);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumer0980ValueSerializerClass);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, maxFetchSize);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoReset0980);
        return props;
    }

    private ContainerProperties containerProperties(String topic,MessageListener messageListener) {
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener(messageListener);
        return containerProperties;
    }


    public void createContainer(String groupId,String topic,MessageListener messageListener,int concurrencySize){
        ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer(consumerFactory(groupId), containerProperties(topic,messageListener));
        container.setConcurrency(concurrencySize);
        container.getContainerProperties().setPollTimeout(requestTimeoutMs);
        container.start();
    }

    public void create0980Container(String groupId,String topic,MessageListener messageListener,int concurrencySize){
        ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer(consumer0980Factory(groupId), containerProperties(topic,messageListener));
        container.setConcurrency(concurrencySize);
        container.getContainerProperties().setPollTimeout(requestTimeoutMs);
        container.start();
    }

    /**
     * 由于kafka消费者消费实例启动和springboot的启动不再同一个线程里启动，所以等springboot容器启动完成后，在启动kafka，以避免
     * 在kafka线程先启动消费的时候调用spring容器还没有加载完的类实例,所以此处的启动挪到Application启动类的run方法里调用
     */
    public void start(){
        //0F37 密集采集数据消费
        createContainer(groupId,concentratedTopic, kafkaConsumerListener0F37,concurrencySize);
        //0F38 can数据周期上报数据消费
        createContainer(groupId,canCycleTopic, kafkaConsumerListener0F38,concurrencySize);
        //0F39 can数据临时上报数据消费
        createContainer(groupId,canTempTopic, kafkaConsumerListener0F39,concurrencySize);
        //0980 位置数据处理
        create0980Container(groupId0980,locationDataTopic, kafkaConsumerListener0980,concurrencySize);
    }

}
