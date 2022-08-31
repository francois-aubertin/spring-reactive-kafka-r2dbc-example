package com.acerill.example;

import com.acerill.example.domain.BuySell;
import com.acerill.example.domain.CurrencyPair;
import com.acerill.example.domain.OrderRequest;
import com.acerill.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class ApplicationTests {

    @Autowired
    private KafkaTemplate<String, OrderRequest> kafkaTemplate;

    @SpyBean
    private OrderService orderService;

    @Test
    void processOrderRequest() throws Exception {
        kafkaTemplate
                .send("order-requests", OrderRequest.builder()
                        .orderId(UUID.randomUUID().toString())
                        .buySell(BuySell.BUY)
                        .currencyPair(CurrencyPair.builder()
                                .base("GBP")
                                .quote("USD")
                                .build()
                        )
                        .quantity(new BigDecimal("10000"))
                        .build()
                )
                .get(5, SECONDS);

        var latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            try {
                return invocation.callRealMethod();
            } finally {
                latch.countDown();
            }
        }).when(orderService).processOrderRequest(any());

        if (!latch.await(10, SECONDS)) {
            throw new AssertionError("Latch did not reach 0 after 10 seconds");
        }


    }

}
