package com.example.ordermanagement.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.ordermanagement.event.InventoryEvent;
import com.example.ordermanagement.event.OrderEvent;
import com.example.ordermanagement.event.PaymentEvent;
@Service
public class KafkaProducerService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendOrderEvent(OrderEvent event) {
		kafkaTemplate.send("order-topic", event);
	}

	public void sendPaymentEvent(PaymentEvent event) {
		kafkaTemplate.send("payment-topic", event);
	}

	public void sendInventoryEvent(InventoryEvent event) {
		kafkaTemplate.send("inventory-topic", event);
	}
}