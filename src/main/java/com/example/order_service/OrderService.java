package com.example.order_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public String placeOrder(Order request) {
        request.setOrderNumber(UUID.randomUUID().toString());
        
        // 1. Saves to MySQL (including the buyerName and cardNumber)
        orderRepository.save(request);

        // 2. Create the Kafka Event (Passing the buyerName, but NOT the card number)
        OrderPlacedEvent event = new OrderPlacedEvent(
                request.getOrderNumber(),
                request.getProductId(),
                request.getQuantity(),
                request.getBuyerName() // --- ADDED THIS ---
        );

        // 3. Send to Kafka
        kafkaTemplate.send("order-topic", event);

        return "Order Placed Successfully for " + request.getBuyerName() + "!";
    }
}