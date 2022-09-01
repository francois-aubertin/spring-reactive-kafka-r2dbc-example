package com.acerill.example;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@TestConfiguration
public class TestConfig {

    @Bean
    public ProducerFactory<Object, Object> producerFactory(KafkaProperties kafkaProperties) {
        DefaultKafkaProducerFactory<Object, Object> producerFactory =
                new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
        producerFactory.setValueSerializer(new JsonSerializer<>());
        return producerFactory;
    }

}
