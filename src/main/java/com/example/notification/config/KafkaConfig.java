package com.example.notification.config;

import com.example.notification.template.AlarmTalk;
import com.example.notification.template.EmailSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Profile("local")
@Configuration
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress; // kafka address

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("alarmtalk.notification", 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, AlarmTalk> alarmTalkProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, AlarmTalk> alarmTalkConsumerFactory() {
        JsonDeserializer<AlarmTalk> deserializer = new JsonDeserializer<>(AlarmTalk.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConsumerFactory<String, EmailSend> sendEmailConsumerFactory() {
        JsonDeserializer<EmailSend> deserializer = new JsonDeserializer<>(EmailSend.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AlarmTalk>
    alarmTalkKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AlarmTalk> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(alarmTalkConsumerFactory());
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailSend>
    sendEmailKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmailSend> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(sendEmailConsumerFactory());
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, AlarmTalk> kafkaTemplate() {
        return new KafkaTemplate<>(alarmTalkProducerFactory());
    }

    @Bean
    public SeekToCurrentErrorHandler errorHandler() {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate(),
                (record, exception) -> {
                    return new TopicPartition(record.topic() + ".failures", record.partition());
                });
        SeekToCurrentErrorHandler handler = new SeekToCurrentErrorHandler(recoverer, new FixedBackOff(5000, 5));
        handler.addNotRetryableException(IllegalArgumentException.class);
        return handler;
    }

}
