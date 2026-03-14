package com.example.order_service;

public class OrderPlacedEvent {
    private String orderNumber;
    private String productId;
    private int quantity;

    public OrderPlacedEvent(String orderNumber, String productId, int quantity) {
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}