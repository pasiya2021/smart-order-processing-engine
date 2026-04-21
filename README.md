# 🚀 Smart Order Processing & Notification Engine

> A production-style Spring Boot backend system demonstrating **Clean Architecture**, **Design Patterns**, **Redis Caching**, and **Async Messaging with RabbitMQ**.

---

## 📌 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Request Flow](#request-flow)
- [API Endpoints](#api-endpoints)
- [How to Run](#how-to-run)
- [Docker Services](#docker-services)
- [Key Learning Points](#key-learning-points)

---

## 📖 Overview

This project simulates a real-world **Order Processing System** where:

- Orders are created with **validated input** and processed with the correct **payment method**
- **Observers** automatically react to order status changes (email, inventory)
- **Notifications** are sent asynchronously via **RabbitMQ** — users never wait
- Frequent reads are served from **Redis cache** — reducing DB load
- All layers are cleanly separated following **Clean Architecture + SOLID principles**

---

## 🏗️ Architecture

```
Client (Postman / REST)
          │
          ▼
  [ Controller Layer ]       →   Receives HTTP requests, validates DTOs
          │
          ▼
  [ Service Layer ]          →   Business logic + Design Patterns
          │
          ▼
  [ Port Interfaces ]        →   SOLID Dependency Inversion (contracts)
          │
          ▼
  [ Infrastructure Layer ]   →   Redis · RabbitMQ · H2 Database · JPA
```

---

## 🎨 Design Patterns

| Pattern | Where Used | Purpose |
|---|---|---|
| 🔨 **Builder** | `Order.java` | Clean object construction with many fields |
| ⚡ **Strategy** | `PaymentContext.java` | Swap payment methods at runtime (Credit Card / PayPal / COD) |
| 👁️ **Observer** | `OrderEventPublisher.java` | Auto-notify Email + Inventory on status change |
| 🏭 **Factory** | `NotificationFactory.java` | Pick right notification sender (Email / SMS / Push) |

---

## 🧱 SOLID Principles

| Principle | Implementation |
|---|---|
| **S** — Single Responsibility | Each class has one job (e.g. `EmailSender` only sends email) |
| **O** — Open/Closed | Add new payment type → just add a new class, no existing code changes |
| **L** — Liskov Substitution | Any `PaymentStrategy` implementation works interchangeably |
| **I** — Interface Segregation | Small focused interfaces (`OrderRepositoryPort`, `NotificationSenderPort`) |
| **D** — Dependency Inversion | `OrderService` depends on `OrderRepositoryPort` interface, not JPA directly |

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Java 17** | Core language |
| **Spring Boot 3.2** | Application framework |
| **Spring Data JPA** | Database access layer |
| **Spring Data Redis** | Caching layer |
| **Spring AMQP** | RabbitMQ messaging |
| **H2 Database** | In-memory database (dev) |
| **Lombok** | Reduce boilerplate code |
| **Docker** | Run Redis + RabbitMQ locally |

---

## 🗂️ Project Structure

```
src/main/java/com/example/orderengine/
│
├── 📁 domain/                          →  Pure business objects (no Spring/JPA)
│   ├── entity/
│   │   └── Order.java                  →  Order entity + Builder Pattern
│   └── enums/
│       ├── OrderStatus.java
│       ├── PaymentType.java
│       └── NotificationType.java
│
├── 📁 application/                     →  Business logic & use cases
│   ├── service/
│   │   └── OrderService.java           →  Core service (Strategy + Observer + Cache)
│   └── port/
│       ├── OrderRepositoryPort.java    →  DB contract interface (SOLID-D)
│       └── NotificationSenderPort.java →  Notification contract interface
│
├── 📁 infrastructure/                  →  Technical implementations
│   ├── cache/
│   │   └── RedisCacheConfig.java       →  Redis cache configuration
│   ├── messaging/
│   │   ├── RabbitMQConfig.java         →  Queue + Exchange setup
│   │   ├── NotificationMessage.java    →  Message model
│   │   ├── NotificationProducer.java   →  Sends message to queue
│   │   └── NotificationConsumer.java   →  Receives + processes message
│   └── persistence/
│       ├── SpringDataOrderRepository.java   →  JPA repository
│       └── JpaOrderRepositoryAdapter.java   →  Implements OrderRepositoryPort
│
├── 📁 interfaces/                      →  Talks to outside world
│   └── controller/
│       ├── OrderController.java        →  REST API endpoints
│       └── dto/
│           ├── CreateOrderRequest.java →  Input DTO with validation
│           └── UpdateStatusRequest.java
│
└── 📁 shared/                          →  Reusable components
    ├── exception/
    │   └── GlobalExceptionHandler.java →  Handles all errors cleanly
    └── patterns/
        ├── factory/                    →  Factory Pattern (Notification)
        ├── strategy/                   →  Strategy Pattern (Payment)
        └── observer/                   →  Observer Pattern (Order Events)
```

---

## 🔄 Request Flow

### POST /api/orders — Create Order

```
1. Postman sends POST request
         │
         ▼
2. OrderController receives → @Valid validates DTO
         │
         ▼
3. OrderService.createOrder()
   ├── 🔨 Builder Pattern     →  builds Order object cleanly
   ├── ⚡ Strategy Pattern    →  PaymentContext picks right payment method
   │       └── "CREDIT_CARD"  →  CreditCardPaymentStrategy.pay()
   ├── 👁️ Observer Pattern    →  EventPublisher notifies all observers
   │       ├── EmailObserver  →  📧 email log
   │       └── InventoryObserver → 📦 stock updated
   ├── Port/Adapter           →  saves Order to H2 DB
   └── RabbitMQ Producer      →  drops message in notification.queue
         │
         ▼
4. Response returned to user ✅  (user doesn't wait for email!)

         Meanwhile in background...
         │
         ▼
5. RabbitMQ Consumer wakes up (different thread)
   └── 🏭 Factory Pattern     →  getSender("EMAIL")
           └── EmailNotificationSender → 📧 email sent
```

### GET /api/orders/{id} — Redis Cache

```
First call:
  → @Cacheable checks Redis → miss
  → hits H2 database
  → stores result in Redis
  → returns response

Second call:
  → @Cacheable checks Redis → hit ⚡
  → returns from Redis (no DB call)
```

---

## 📬 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/orders` | Create a new order |
| `GET` | `/api/orders` | Get all orders |
| `GET` | `/api/orders/{id}` | Get order by ID *(Redis cached)* |
| `PATCH` | `/api/orders/{id}/status` | Update order status |
| `DELETE` | `/api/orders/{id}` | Delete order |

### Sample Create Order Request

```json
{
    "customerName": "Pasindu",
    "productName": "Laptop",
    "quantity": 2,
    "totalPrice": 150000.0,
    "shippingAddress": "Colombo",
    "priority": 3,
    "paymentType": "CREDIT_CARD"
}
```

### Sample Update Status Request

```json
{
    "status": "CONFIRMED"
}
```

### Payment Types
```
CREDIT_CARD | PAYPAL | CASH_ON_DELIVERY
```

### Order Status Values
```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED → CANCELLED
```

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven
- Docker

### 1️⃣ Start Redis + RabbitMQ

```bash
docker run -d --name redis -p 6379:6379 redis
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```

### 2️⃣ Run the Application

```bash
mvn spring-boot:run
```

### 3️⃣ Test with Postman

```
Base URL : http://localhost:8080/api/orders
```

> ⚠️ **Note:** H2 is an in-memory database. Data resets on every restart.
> Always POST a new order before testing GET/PATCH/DELETE.

---

## 🐳 Docker Services

| Service | Port | Dashboard URL |
|---|---|---|
| **Redis** | `6379` | — |
| **RabbitMQ** | `5672` | [http://localhost:15672](http://localhost:15672) → guest / guest |
| **H2 Console** | — | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) |

### H2 Console Login
```
JDBC URL  : jdbc:h2:mem:orderdb
Username  : sa
Password  : (leave empty)
```

---

## 💡 Key Learning Points

- **Clean Architecture** keeps business logic independent of frameworks — domain layer has zero Spring annotations
- **Port/Adapter (SOLID-D)** means swapping H2 → PostgreSQL requires changing only the adapter, zero service changes
- **Strategy Pattern** eliminates if-else chains for payment — add new payment type by just adding one class
- **Observer Pattern** decouples order events from reactions — add SMS observer without touching service
- **Factory Pattern** selects notification sender dynamically — no switch/if-else needed
- **RabbitMQ** decouples notification sending — users get instant response, emails process in background
- **Redis Cache** reduces DB load — repeated GET requests served from memory

---

## 👨‍💻 Author

**Pasindu** — 4th Year IT Undergraduate, University of Moratuwa

---

> 💬 *"This project was built to demonstrate production-level backend patterns for software engineering interview preparation."*