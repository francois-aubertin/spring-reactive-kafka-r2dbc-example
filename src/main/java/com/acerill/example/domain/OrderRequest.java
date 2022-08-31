package com.acerill.example.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Data
@Jacksonized @Builder
public class OrderRequest {
    private final String orderId;
    private final BuySell buySell;
    private final CurrencyPair currencyPair;
    private final BigDecimal quantity;
}
