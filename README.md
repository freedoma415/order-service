### 2. Order Service

```markdown
# 📦 Order Service

The core orchestration service for the Tech Store e-commerce platform. It provides REST APIs for the frontend, stores the single source of truth for order states in MySQL, and initiates the Saga distributed transaction pattern using Apache Kafka.

## 🛠️ Tech Stack
* Java 17
* Spring Boot 3.4+ (Web, Data JPA)
* MySQL
* Apache Kafka (Spring Kafka)

## 📡 Kafka Integration
* **Produces to:** `order-topic` (Broadcasts new `PENDING` orders)
* **Consumes from:** `payment-result-topic` (Listens for `SUCCESS`/`FAILED` compensation events)

## ⚙️ Environment Configuration
Ensure your local MySQL and Kafka instances are running.
* **Port:** `8081`
* **Database:** `jdbc:mysql://localhost:3306/order_db`

## 💻 API Endpoints

**POST `/api/orders`**
Places a new order and triggers the Saga.
```json
{
  "orderId": "ORD-1234",
  "productId": "lap-mac-m4",
  "buyerName": "Fridom Araya",
  "cardNumber": "1111222233334444",
  "quantity": 1
}
GET /api/orders/status/{orderNumber}
Retrieves the real-time status of a specific order (PENDING, SUCCESS, FAILED, NOT_FOUND).

GET /api/orders
Retrieves a list of all orders.
