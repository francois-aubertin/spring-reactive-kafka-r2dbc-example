package com.acerill.example.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@Table(name="fx_order")
public class Order {

    @Id
    private final String orderId;
    private final BuySell buySell;
    private final String baseCcy;
    private final String quoteCcy;
    private final BigDecimal quantity;
}
