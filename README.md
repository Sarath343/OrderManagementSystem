# OrderManagementSystem

This project is an Order Management System (OMS) built using Spring Boot, Spring Data JPA and Kafka  . 
It follows a microservices architecture and uses the Saga pattern to ensure eventual consistency in distributed transactions.
It deals with discounting logic for products in the order


# Workflow
1. User Places an Order
The OrderService saves the order and publishes an ORDER_CREATED event to Kafka (order-topic).

2. Payment Service Listens & Processes Payment
PaymentService listens to order-topic.
If payment is successful, it publishes a PAYMENT_SUCCESS event.
If payment fails, it publishes a PAYMENT_FAILED event and triggers rollback.

3. Inventory Service Reserves Stock
InventoryService listens to payment-topic.
If stock is available, it publishes an INVENTORY_SUCCESS event.
If stock is not available, it publishes INVENTORY_FAILURE, triggering a refund & order rollback.

4. Order Completion or Rollback
If all steps succeed, the order is shipped.
If any step fails, a rollback (Saga pattern) is triggered (e.g., payment refund on inventory failure).


# Pre requisites:
Kafka should be available in the machine to make the project working

1.Start Kafka & Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

2.Create Kafka Topics
bin/kafka-topics.sh --create --topic order-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
bin/kafka-topics.sh --create --topic payment-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
bin/kafka-topics.sh --create --topic inventory-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
