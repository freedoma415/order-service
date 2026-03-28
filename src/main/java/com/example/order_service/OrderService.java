package com.example.order_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RestClient inventoryRestClient; 

    public String processNewOrder(OrderRequest request) {
        System.out.println("⚙️ [ORDER] Processing order for: " + request.getBuyerName());
        
        // 1. SYNCHRONOUS HTTP CALL: Ask Inventory Service if it's in stock
        System.out.println("🔍 [ORDER] Checking stock with INVENTORY-SERVICE...");
        
        try {
            Boolean inStock = inventoryRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/inventory/{productId}")
                            .queryParam("quantity", request.getQuantity())
                            .build(request.getProductId()))
                    .retrieve()
                    .body(Boolean.class); 

            if (Boolean.FALSE.equals(inStock)) {
                System.out.println("🚫 [ORDER] Order rejected: Item is out of stock.");
                return "OUT_OF_STOCK"; // Fails immediately, skipping Kafka entirely
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ [ORDER] Failed to reach Inventory Service: " + e.getMessage());
            return "INVENTORY_ERROR"; 
        }

        // 2. ASYNCHRONOUS KAFKA CALL: Proceed with the payment Saga
        System.out.println("✅ [ORDER] Stock confirmed. Sending to Payment Saga...");
        String payload = request.getOrderId() + ":" + request.getProductId() + ":" + request.getCardNumber();
        kafkaTemplate.send("order-topic", payload);
        
        return "PENDING";
    }
}