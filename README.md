# Kafka KRaft Microservices Lab

This workspace contains a minimal lab-complete Spring Boot implementation for an event-driven architecture using Kafka in KRaft mode.

## Services

- `gateway` on port `8080`
- `order-service` on port `8081`
- `inventory-service` on port `8082`
- `billing-service` on port `8083`
- Kafka on port `9092`
- Kafka UI on port `8088`

## Order Flow

1. Send `POST /orders` to the gateway.
2. The gateway forwards the request to `order-service`.
3. `order-service` stores the order in memory and publishes an `OrderCreatedEvent` to Kafka topic `order-topic`.
4. `inventory-service` consumes the event and logs a stock update.
5. `billing-service` consumes the event and logs invoice generation.

## Start Kafka

Make sure Docker Desktop is running before starting Kafka.

```powershell
docker compose up -d
```

## Build The Project

```powershell
mvn clean package
```

## Run The Services

Open four terminals from the workspace root and run:

```powershell
mvn -pl gateway spring-boot:run
```

```powershell
mvn -pl order-service spring-boot:run
```

```powershell
mvn -pl inventory-service spring-boot:run
```

```powershell
mvn -pl billing-service spring-boot:run
```

If Kafka is running somewhere else, set `KAFKA_BOOTSTRAP_SERVERS` before starting the services.

## Postman Request

`POST http://localhost:8080/orders`

```json
{
  "orderId": "ORD-1001",
  "item": "Laptop",
  "quantity": 1
}
```

## Expected Logs

- `order-service`: receives the order and publishes to `order-topic`
- `inventory-service`: consumes the order and logs stock reservation/update
- `billing-service`: consumes the order and logs invoice generation

## Submission Checklist

- Working API call from Postman through the gateway
- Kafka running in KRaft mode with Docker Compose
- Order, Inventory, and Billing service logs
- `docker-compose.yml`
