package com.acerill.example.service;

import com.acerill.example.domain.Order;
import com.acerill.example.domain.OrderRequest;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<Order> processOrderRequest(OrderRequest orderRequest);

}
