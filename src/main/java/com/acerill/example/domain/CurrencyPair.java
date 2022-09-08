package com.acerill.example.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@RequiredArgsConstructor
@Jacksonized @Builder
public class CurrencyPair {
    private final String base;
    private final String quote;
}
