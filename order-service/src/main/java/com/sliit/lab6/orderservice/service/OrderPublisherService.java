package com.sliit.lab6.orderservice.service;

import com.sliit.lab6.orderservice.model.OrderCreatedEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisherService {

    private static final Logger log = LoggerFactory.getLogger(OrderPublisherService.class);

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final String orderTopic;
    private final List<OrderCreatedEvent> submittedOrders = Collections.synchronizedList(new ArrayList<>());

    public OrderPublisherService(
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate,
            @Value("${app.kafka.order-topic:order-topic}") String orderTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderTopic = orderTopic;
    }

    public void publish(OrderCreatedEvent order) {
        log.info("Received order {} for item {} with quantity {}", order.orderId(), order.item(), order.quantity());
        submittedOrders.add(order);

        try {
            kafkaTemplate.send(orderTopic, order.orderId(), order).get(10, TimeUnit.SECONDS);
            log.info("Published order {} to topic {}", order.orderId(), orderTopic);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Kafka publish interrupted", ex);
        } catch (ExecutionException | TimeoutException ex) {
            throw new IllegalStateException("Failed to publish order event", ex);
        }
    }

    public List<OrderCreatedEvent> getSubmittedOrders() {
        return List.copyOf(submittedOrders);
    }
}