package com.example.order_service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public String placeOrder(Order request) {
        // 1. Generate a random Order Number
        request.setOrderNumber(UUID.randomUUID().toString());

        // 2. Save the order to the local Order MySQL Database
        orderRepository.save(request);

        // 3. Create the Kafka Event
        OrderPlacedEvent event = new OrderPlacedEvent(
                request.getOrderNumber(),
                request.getProductId(),
                request.getQuantity()
        );

        // 4. Send the event to the Kafka Broker (Topic: "order-topic")
        kafkaTemplate.send("order-topic", event);

        return "Order Placed Successfully!";
    }
}