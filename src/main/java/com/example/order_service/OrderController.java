package com.example.order_service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // A simple in-memory database to track order statuses
    public static final Map<String, String> orderStatuses = new ConcurrentHashMap<>();

    @PostMapping
    public String placeOrder(@RequestBody OrderPlacedEvent request) {
        // 1. Mark the order as PENDING
        orderStatuses.put(request.getOrderId(), "PENDING");

        // 2. Attach the orderId to the Kafka payload: "orderId:productId:cardNumber"
        String payload = request.getOrderId() + ":" + request.getProductId() + ":" + request.getCardNumber();
        kafkaTemplate.send("order-topic", payload);
        
        return "PENDING";
    }

    // NEW: The Polling Endpoint for React
    @GetMapping("/status/{orderId}")
    public String checkStatus(@PathVariable String orderId) {
        return orderStatuses.getOrDefault(orderId, "NOT_FOUND");
    }
}