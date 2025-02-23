package com.example.ordermanagement.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.ordermanagement.event.InventoryEvent;
import com.example.ordermanagement.event.OrderEvent;
import com.example.ordermanagement.event.PaymentEvent;

@Service
public class KafkaConsumerService {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService ; 

    public KafkaConsumerService(OrderService orderService, PaymentService paymentService , InventoryService inventoryService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.inventoryService =inventoryService;
    }
    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void handleOrderEvent(OrderEvent event) {
        if ("ORDER_CREATED".equals(event.getStatus())) {
            System.out.println("Received Order Event, initiating payment...");
            paymentService.processPayment(event.getOrderId(), event.getUserId().toString());
        }
    }


    // Consume payment events and trigger rollback if payment fails
    @KafkaListener(topics = "payment-topic", groupId = "order-group")
    public void handlePaymentEvent(PaymentEvent event) {
        if ("PAYMENT_FAILED".equals(event.getStatus())) {
            System.out.println("Payment failed, rolling back order: " + event.getOrderId());
            orderService.cancelOrder(event.getOrderId(),"CANCELLED");
        } else if ("PAYMENT_SUCCESS".equals(event.getStatus())) {
            System.out.println("Payment successful, proceeding to inventory update.");
            inventoryService.processInventory(event.getOrderId(), event.getUserId().toString());
         }
        else if("REFUND_SUCCESS".equals(event.getStatus())) {
        	System.out.println("Refund successfull , Happy shopping...");
        	orderService.cancelOrder(event.getOrderId(), "REFUNDED");
        }
    }

    // Consume inventory events
    @KafkaListener(topics = "inventory-topic", groupId = "order-group")
    public void handleInventoryEvent(InventoryEvent event) {
        if ("INVENTORY_FAILURE".equals(event.getStatus())) {
            System.out.println("Inventory update failed, initiating refund for order: " + event.getOrderId());
            paymentService.processRefund(event.getOrderId(), event.getUserId()); // Rollback: Refund payment
        } else if ("INVENTORY_SUCCESS".equals(event.getStatus())) {
            System.out.println("Inventory update successful, order shipped: " + event.getOrderId());
            orderService.shipOrder(event.getOrderId());        }
    }
}
