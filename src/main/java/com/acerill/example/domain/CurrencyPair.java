package com.acerill.example.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized @Builder
public class CurrencyPair {
    private final String base;
    private final String quote;
}
