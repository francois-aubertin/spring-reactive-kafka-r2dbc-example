package com.acerill.example;

import com.acerill.example.domain.OrderRequest;
import com.acerill.example.service.OrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Function<Mono<OrderRequest>, Mono<Void>> processOrderRequest(OrderService orderService) {
		return req -> req.flatMap(orderService::processOrderRequest).then();
	}

}
