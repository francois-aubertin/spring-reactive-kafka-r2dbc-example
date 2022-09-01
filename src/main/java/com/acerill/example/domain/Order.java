package com.acerill.example.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(name="fx_order")
public class Order {

    @Id
    private final String orderId;
    private final BuySell buySell;

}
