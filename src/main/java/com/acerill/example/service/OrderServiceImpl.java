package com.acerill.example.service;

import com.acerill.example.domain.Order;
import com.acerill.example.domain.OrderRequest;
import com.acerill.example.infrastructure.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Mono<Order> processOrderRequest(OrderRequest orderRequest) {
        log.info("Received order request {}", orderRequest);
        var currencyPair = orderRequest.getCurrencyPair();
        var order = Order.builder()
                .buySell(orderRequest.getBuySell())
                .baseCcy(currencyPair.getBase())
                .quoteCcy(currencyPair.getQuote())
                .quantity(orderRequest.getQuantity())
                .build();
        return orderRepository.save(order)
                .doOnSuccess(savedOrder -> log.info("Saved order {}", savedOrder));
    }

}
