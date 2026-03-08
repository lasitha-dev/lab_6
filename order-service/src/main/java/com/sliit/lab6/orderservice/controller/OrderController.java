package com.sliit.lab6.orderservice.controller;

import com.sliit.lab6.orderservice.model.OrderCreatedEvent;
import com.sliit.lab6.orderservice.service.OrderPublisherService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderPublisherService orderPublisherService;

    public OrderController(OrderPublisherService orderPublisherService) {
        this.orderPublisherService = orderPublisherService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@Valid @RequestBody OrderCreatedEvent order) {
        orderPublisherService.publish(order);
        return ResponseEntity.accepted().body(Map.of(
                "message", "Order Created & Event Published",
                "orderId", order.orderId()
        ));
    }

    @GetMapping
    public List<OrderCreatedEvent> listOrders() {
        return orderPublisherService.getSubmittedOrders();
    }
}