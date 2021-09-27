package com.sltb.kiosks.travelservice.travelservice;

import com.sltb.kioskslib.library.model.kafka.CheckIn;
import com.sltb.kioskslib.library.model.kafka.TopUp;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.sltb.kioskslib.library")
@EntityScan("com.sltb.kioskslib.library.model")
public class KafkaConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapServers;

    @Value("${topupreply.topic.name}")
    private String topupReplyTopic;

    @Value("${kakfa.consumer.group}")
    private String consumerGroup;

    @Value("${checkinreply.topic.name}")
    private String checkinReplyTopic;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        return props;
    }

    @Bean
    public ProducerFactory<String, TopUp> topUpProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, TopUp> topUpKafkaTemplate() {
        return new KafkaTemplate<>(topUpProducerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, TopUp, TopUp> topUpReplyKafkaTemplate(ProducerFactory<String, TopUp> pf, KafkaMessageListenerContainer<String, TopUp> container){
        return new ReplyingKafkaTemplate<>(pf, container);

    }

    @Bean
    public KafkaMessageListenerContainer<String, TopUp> topUpReplyContainer(ConsumerFactory<String, TopUp> cf) {
        ContainerProperties containerProperties = new ContainerProperties(topupReplyTopic);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConsumerFactory<String, TopUp> topUpConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),new JsonDeserializer<>(TopUp.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, TopUp>> topUpKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TopUp> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(topUpConsumerFactory());
        factory.setReplyTemplate(topUpKafkaTemplate());
        return factory;
    }

    ////
    @Bean
    public ProducerFactory<String, CheckIn> checkInProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, CheckIn> checkInKafkaTemplate() {
        return new KafkaTemplate<>(checkInProducerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, CheckIn, CheckIn> checkInReplyKafkaTemplate(ProducerFactory<String, CheckIn> pf, KafkaMessageListenerContainer<String, CheckIn> container){
        return new ReplyingKafkaTemplate<>(pf, container);

    }

    @Bean
    public KafkaMessageListenerContainer<String, CheckIn> replyContainer(ConsumerFactory<String, CheckIn> cf) {
        ContainerProperties containerProperties = new ContainerProperties(checkinReplyTopic);
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConsumerFactory<String, CheckIn> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),new JsonDeserializer<>(CheckIn.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CheckIn>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CheckIn> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(checkInKafkaTemplate());
        return factory;
    }

}
