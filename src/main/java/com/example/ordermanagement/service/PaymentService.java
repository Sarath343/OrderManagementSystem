package com.example.ordermanagement.service;

import org.springframework.stereotype.Service;

import com.example.ordermanagement.event.PaymentEvent;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {
	private final KafkaProducerService kafkaProducerService;
	private final OrderService orderService;

	public PaymentService(KafkaProducerService kafkaProducerService, OrderService orderService) {
		this.kafkaProducerService = kafkaProducerService;
		this.orderService = orderService;
	}

	@Transactional
	public void processPayment(Long orderId, String userId) {
		boolean paymentSuccess = Math.random() > 0.2; // Simulating random payment failures

		if (paymentSuccess) {
			kafkaProducerService.sendPaymentEvent(new PaymentEvent(orderId, Long.valueOf(userId), "PAYMENT_SUCCESS"));
		} else {
			kafkaProducerService.sendPaymentEvent(new PaymentEvent(orderId, Long.valueOf(userId), "PAYMENT_FAILED"));
			orderService.cancelOrder(orderId,"PAYMENT_FAILED");
		}
	}

	public void processRefund(Long orderId, Long userId) {
		//Assumes the refund is success
		kafkaProducerService.sendPaymentEvent(new PaymentEvent(orderId, Long.valueOf(userId), "REFUND_SUCCESS"));

	}
}