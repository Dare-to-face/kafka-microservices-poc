# Kafka Microservices POC - Order & Inventory System

This is a proof-of-concept implementation demonstrating event-driven microservices architecture using Apache Kafka. The system showcases asynchronous communication between Order and Inventory services.

## 🏗️ Architecture Overview

```
┌─────────────────┐    Kafka Topics    ┌─────────────────┐
│  Order Service  │ ──────────────────► │ Inventory Service│
│   (Port 8080)   │   order-events     │   (Port 8081)    │
│                 │ ◄────────────────── │                 │
│   📤 Producer   │ inventory-events   │   📥 Consumer    │
└─────────────────┘                    └─────────────────┘
         │                                       │
         └──────────────── Kafka ────────────────┘
                      (Port 9092)
                   Kafka UI (Port 8085)
```

### Event Flow:
1. **User places order** → Order Service creates order
2. **OrderCreatedEvent** published to Kafka
3. **Inventory Service** consumes event and updates stock
4. **InventoryUpdatedEvent** published back to Kafka
5. **Real-time monitoring** via Kafka UI

---

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose
- Git

### 1. Clone and Setup Kafka Infrastructure

```bash
# Clone the repository
git clone <your-repo-url>
cd kafka-microservices-poc

# Start Kafka infrastructure
docker-compose up -d

# Verify Kafka is running
docker-compose ps
```

**Kafka UI**: http://localhost:8085

### 2. Start the Microservices

#### Terminal 1 - Order Service
```bash
cd order-service
mvn clean spring-boot:run
```

#### Terminal 2 - Inventory Service
```bash
cd inventory-service
mvn clean spring-boot:run
```

### 3. Verify Services are Running

```bash
# Check Order Service
curl http://localhost:8080/actuator/health

# Check Inventory Service
curl http://localhost:8081/actuator/health

# Check current inventory
curl http://localhost:8081/api/inventory/current
```

**Expected inventory response:**
```json
{
  "PRODUCT-1": 50,
  "PRODUCT-2": 100,
  "PRODUCT-3": 25
}
```

---

## 🧪 Testing the POC

### Test 1: Simple Order Creation
```bash
curl -X POST http://localhost:8080/api/orders/test
```

**Expected Response:**
```json
{
  "orderId": "ORDER-12345678",
  "status": "Test order created successfully"
}
```

### Test 2: Custom Order Creation
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUSTOMER-456",
    "items": [
      {
        "productId": "PRODUCT-1",
        "productName": "Laptop",
        "quantity": 2,
        "price": 999.99
      },
      {
        "productId": "PRODUCT-2",
        "productName": "Mouse",
        "quantity": 1,
        "price": 29.99
      }
    ]
  }'
```

### Test 3: Verify Inventory Updates
```bash
# Check inventory after orders
curl http://localhost:8081/api/inventory/current
```

**Expected Response** (quantities should be reduced):
```json
{
  "PRODUCT-1": 47,
  "PRODUCT-2": 97,
  "PRODUCT-3": 25
}
```

---

## 📊 Monitoring & Observability

### 1. Application Logs

**Order Service Logs:**
```
INFO  - Creating order: ORDER-12345678 for customer: CUSTOMER-123 with total: 1059.97
INFO  - Publishing OrderCreatedEvent for order: ORDER-12345678
```

**Inventory Service Logs:**
```
INFO  - 🎯 Received OrderCreatedEvent from topic: order-events, partition: 0, offset: 0
INFO  - 📦 Processing order: ORDER-12345678 for customer: CUSTOMER-123
INFO  - Reserved 1 units of PRODUCT-1 - Remaining stock: 49
INFO  - 📤 Publishing InventoryUpdatedEvent for order: ORDER-12345678
INFO  - ✅ Successfully processed order: ORDER-12345678
```

### 2. Kafka UI Dashboard

Access **http://localhost:8085** to monitor:
- **Topics**: `order-events`, `inventory-events`
- **Messages**: Real-time message flow
- **Consumer Groups**: `inventory-service-group`
- **Partitions & Offsets**: Message processing status

### 3. Health Endpoints

```bash
# Application health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health

# Current inventory status
curl http://localhost:8081/api/inventory/current
```

---

## 🏢 Project Structure

```
kafka-microservices-poc/
├── docker-compose.yml              # Kafka infrastructure
├── order-service/
│   ├── src/main/java/com/example/orderservice/
│   │   ├── OrderServiceApplication.java
│   │   ├── controller/
│   │   │   └── OrderController.java
│   │   ├── service/
│   │   │   └── OrderService.java
│   │   └── events/
│   │       ├── OrderEvent.java
│   │       └── OrderCreatedEvent.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── inventory-service/
│   ├── src/main/java/com/example/inventoryservice/
│   │   ├── InventoryServiceApplication.java
│   │   ├── controller/
│   │   │   └── InventoryController.java
│   │   ├── service/
│   │   │   ├── InventoryService.java
│   │   │   └── OrderEventListener.java
│   │   └── events/
│   │       ├── OrderEvent.java          # Shared event
│   │       ├── OrderCreatedEvent.java   # Shared event
│   │       ├── InventoryEvent.java
│   │       └── InventoryUpdatedEvent.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── README.md
```

---

## ⚙️ Configuration Details

### Kafka Infrastructure (docker-compose.yml)
- **Zookeeper**: Port 2181
- **Kafka Broker**: Port 9092 (external), 29092 (internal)
- **Kafka UI**: Port 8085

### Order Service Configuration
- **Port**: 8080
- **Role**: Producer (publishes OrderCreatedEvent)
- **Topics**: Publishes to `order-events`

### Inventory Service Configuration
- **Port**: 8081
- **Role**: Consumer & Producer
- **Topics**: Consumes from `order-events`, publishes to `inventory-events`
- **Consumer Group**: `inventory-service-group`

---

## 🎯 Key Features Demonstrated

### 1. **Event-Driven Architecture**
- ✅ Asynchronous communication between services
- ✅ Loose coupling - services don't directly depend on each other
- ✅ Event-first approach to business processes

### 2. **Kafka Integration**
- ✅ Producer configuration with JSON serialization
- ✅ Consumer configuration with manual acknowledgment
- ✅ Topic-based message routing
- ✅ Consumer groups for load balancing

### 3. **Resilience Patterns**
- ✅ Retry mechanism for failed message processing
- ✅ Manual acknowledgment for at-least-once delivery
- ✅ Error handling without data loss

### 4. **Observability**
- ✅ Structured logging with correlation IDs
- ✅ Real-time monitoring via Kafka UI
- ✅ Health check endpoints

---

## 🧪 Advanced Testing Scenarios

### Scenario 1: Order with Insufficient Stock
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUSTOMER-999",
    "items": [
      {
        "productId": "PRODUCT-1",
        "productName": "Laptop",
        "quantity": 100,
        "price": 999.99
      }
    ]
  }'
```

**Expected Behavior:**
- Order created successfully (fast response)
- Inventory service logs insufficient stock warning
- InventoryUpdatedEvent published with FAILED status

### Scenario 2: Multiple Orders Concurrently
```bash
# Run multiple orders simultaneously
for i in {1..5}; do
  curl -X POST http://localhost:8080/api/orders/test &
done
wait
```

**Expected Behavior:**
- All orders processed independently
- Stock decremented correctly (no race conditions)
- Events processed in parallel by inventory service

### Scenario 3: Service Recovery
```bash
# Stop inventory service
# Create some orders (they'll be queued in Kafka)
curl -X POST http://localhost:8080/api/orders/test

# Restart inventory service
# Orders should be processed automatically
```

---

## 🛠️ Troubleshooting

### Common Issues

#### 1. **404 Errors on API Calls**
```bash
# Check if services are running
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health

# Verify correct ports in application.properties
server.port=8080  # Order Service
server.port=8081  # Inventory Service
```

#### 2. **Kafka Connection Issues**
```bash
# Verify Kafka is running
docker-compose ps

# Check Kafka logs
docker-compose logs kafka

# Restart Kafka if needed
docker-compose restart kafka
```

#### 3. **Messages Not Being Consumed**
- Check consumer group in Kafka UI
- Verify topic names match between producer and consumer
- Check `spring.json.trusted.packages` configuration

#### 4. **Serialization Errors**
```
# Ensure matching event classes in both services
# Check trusted packages configuration:
spring.kafka.consumer.properties.spring.json.trusted.packages=com.example.orderservice.events,com.example.inventoryservice.events
```

### Debug Commands
```bash
# Check Kafka topics
docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list

# View messages in topic
docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic order-events --from-beginning

# Check consumer groups
docker exec -it kafka kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

---

## 🔧 Development Guide

### Adding New Event Types

1. **Create Event Class**
```java
public class OrderShippedEvent extends OrderEvent {
    private String trackingNumber;
    // constructors, getters, setters
}
```

2. **Update @JsonSubTypes**
```java
@JsonSubTypes({
    @JsonSubTypes.Type(value = OrderCreatedEvent.class, name = "ORDER_CREATED"),
    @JsonSubTypes.Type(value = OrderShippedEvent.class, name = "ORDER_SHIPPED")
})
```

3. **Add Event Listener**
```java
@KafkaListener(topics = "order-events")
public void handleOrderShipped(OrderShippedEvent event) {
    // Handle shipping logic
}
```
---

## 📚 Learning Resources

### Kafka Concepts Demonstrated
- **Topics & Partitions**: Message organization
- **Producers & Consumers**: Message publishing/consuming
- **Consumer Groups**: Load balancing
- **Serialization**: JSON message format
- **At-least-once delivery**: Message reliability

### Spring Kafka Features Used
- `@KafkaListener`: Declarative consumer
- `KafkaTemplate`: Producer operations
- `JsonSerializer/Deserializer`: Automatic JSON handling
- Manual acknowledgment: Message processing control

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create Pull Request

---
