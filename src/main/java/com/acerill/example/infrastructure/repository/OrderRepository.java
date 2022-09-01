package com.acerill.example.infrastructure.repository;

import com.acerill.example.domain.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order, String> {

}
