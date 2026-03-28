package com.example.order_service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SagaCompensationListener {

    @KafkaListener(topics = "payment-result-topic", groupId = "order-saga-group")
    public void handlePaymentResult(String resultMessage) {
        
        String[] parts = resultMessage.split(":");
        String productCode = parts[0];
        String status = parts[1];

        if ("SUCCESS".equals(status)) {
            System.out.println("🎉 [ORDER SERVICE SAGA] Payment cleared for " + productCode + ". Order is now FINALIZED.");
            // In a real app, you would update the DB order status to 'COMPLETED' here
        } else if ("FAILED".equals(status)) {
            System.out.println("🚨 [ORDER SERVICE SAGA] Payment failed for " + productCode + "!");
            System.out.println("🔄 Executing Compensating Transaction: Canceling order and releasing reserved stock...");
            // In a real app, you would update DB order status to 'CANCELLED' and tell the Product Service to add +1 back to stock
        }
    }
}