package com.example.order_service; // Update if your package is different

public class OrderPlacedEvent {

    private String orderId; 
    private String productId;
    private int quantity;
    private String buyerName;
    private String cardNumber;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    // ... keep your other getters and setters ...


    // Getters and Setters are required for Spring to map the JSON
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
}