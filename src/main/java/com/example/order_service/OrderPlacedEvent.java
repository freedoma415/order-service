package com.example.order_service; // Change this to com.example.Product when pasting into the Product service!

public class OrderPlacedEvent {
    private String orderNumber;
    private String productId;
    private int quantity;
    private String buyerName; // --- NEW FIELD ---

    public OrderPlacedEvent() {}

    public OrderPlacedEvent(String orderNumber, String productId, int quantity, String buyerName) {
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.quantity = quantity;
        this.buyerName = buyerName;
    }

    // Existing Getters and Setters
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // --- NEW Getter and Setter ---
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
}