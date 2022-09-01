package com.acerill.example;

import com.acerill.example.domain.BuySell;
import com.acerill.example.domain.CurrencyPair;
import com.acerill.example.domain.Order;
import com.acerill.example.domain.OrderRequest;
import com.acerill.example.infrastructure.repository.OrderRepository;
import com.acerill.example.service.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@Slf4j
@SpringBootTest
@Import(TestConfig.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class ApplicationTests {

    @Autowired
    private KafkaTemplate<String, OrderRequest> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @SpyBean
    private OrderServiceImpl orderService;

    @Test
    void processOrderRequest() throws Exception {
        CountDownLatch latch = createLatchForOrderProcessing();

        sendOrderRequest(BuySell.BUY, "GBP", "USD", new BigDecimal("10000"));

        if (!latch.await(10, SECONDS)) {
            throw new AssertionError("Latch did not reach 0 after 10 seconds");
        }

        var orders = orderRepository.findAll().collectList().block(Duration.ofSeconds(5));
        assertThat(orders.size(), equalTo(1));
        var order = orders.get(0);
        assertThat(order.getOrderId(), is(notNullValue()));
        assertThat(order.getBuySell(), equalTo(BuySell.BUY));
    }

    private CountDownLatch createLatchForOrderProcessing() {
        var latch = new CountDownLatch(1);
        doAnswer(
            invocation -> ((Mono<Order>) invocation.callRealMethod()).doOnNext(o -> latch.countDown())
        ).when(orderService).processOrderRequest(any());
        return latch;
    }

    private void sendOrderRequest(BuySell buySell, String baseCcy, String quoteCcy, BigDecimal quantity) throws Exception {
        var sendResult = kafkaTemplate
                .send("order-requests", OrderRequest.builder()
                        .buySell(buySell)
                        .currencyPair(CurrencyPair.builder()
                                .base(baseCcy)
                                .quote(quoteCcy)
                                .build()
                        )
                        .quantity(quantity)
                        .build()
                )
                .get(5, SECONDS);
        log.info("Sent order request message {}", sendResult);
    }

}
