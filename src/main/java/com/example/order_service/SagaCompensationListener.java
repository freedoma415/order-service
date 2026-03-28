package com.example.order_service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SagaCompensationListener {

    @KafkaListener(topics = "payment-result-topic", groupId = "order-saga-group")
    public void handlePaymentResult(String resultMessage) {
        // Message format is now: "orderId:productId:STATUS"
        String[] parts = resultMessage.split(":");
        String orderId = parts[0];
        String productCode = parts[1];
        String status = parts.length > 2 ? parts[2] : "UNKNOWN";

        // Update the tracking map so React can see the result!
        OrderController.orderStatuses.put(orderId, status);

        if ("SUCCESS".equals(status)) {
            System.out.println("🎉 [SAGA] Payment cleared for " + productCode);
        } else if ("FAILED".equals(status)) {
            System.out.println("🚨 [SAGA] Payment failed for " + productCode + "!");
        }
    }
}
