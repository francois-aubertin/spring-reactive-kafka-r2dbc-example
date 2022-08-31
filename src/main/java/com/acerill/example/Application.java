package com.acerill.example;

import com.acerill.example.domain.OrderRequest;
import com.acerill.example.service.OrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.function.Consumer;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ProducerFactory<Object, Object> producerFactory(KafkaProperties kafkaProperties) {
		DefaultKafkaProducerFactory<Object, Object> producerFactory =
				new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
		producerFactory.setValueSerializer(new JsonSerializer<>());
		return producerFactory;
	}

	@Bean
	public Consumer<OrderRequest> processOrderRequest(OrderService orderService) {
		return req -> orderService.processOrderRequest(req);
	}

}
