package com.example.order_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SagaCompensationListener {

    // 🚨 Injects your database connection
    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(topics = "payment-result-topic", groupId = "order-saga-group")
    public void handlePaymentResult(Map<String, String> paymentResult) {
        
        // Extract the data directly from the JSON Map
        String orderNumber = paymentResult.get("orderId");
        String productCode = paymentResult.get("productId");
        String status = paymentResult.getOrDefault("status", "UNKNOWN");

        // --- PART 1: UPDATE THE MYSQL DATABASE ---
        
        // Note: Ensure 'findByOrderNumber' matches the exact method name in your OrderRepository.
        Order existingOrder = orderRepository.findByOrderNumber(orderNumber);
        
        if (existingOrder != null) {
            existingOrder.setStatus(status);     // Update the status field
            orderRepository.save(existingOrder); // Save it back to the database
            System.out.println("💾 MySQL updated! Order " + orderNumber + " is now " + status);
        } else {
            System.err.println("⚠️ Could not find order " + orderNumber + " in database!");
        }

        // --- PART 2: UPDATE THE FRONTEND STATE ---
        
        // Update the tracking map so your React app can see the result
        OrderController.orderStatuses.put(orderNumber, status);

        if ("SUCCESS".equals(status)) {
            System.out.println("🎉 [SAGA] Payment cleared for " + productCode + " (Order: " + orderNumber + ")");
        } else if ("FAILED".equals(status)) {
            System.out.println("🚨 [SAGA] Payment failed for " + productCode + "! (Order: " + orderNumber + ")");
        }
    }
}