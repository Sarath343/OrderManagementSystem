package com.example.ordermanagement.service;

import org.springframework.stereotype.Service;

import com.example.ordermanagement.event.InventoryEvent;

@Service
public class InventoryService {
	private final KafkaProducerService kafkaProducerService;
	private final OrderService orderService;

	public InventoryService(KafkaProducerService kafkaProducerService, OrderService orderService) {
		this.kafkaProducerService = kafkaProducerService;
		this.orderService = orderService;
	}



	public void processInventory(Long orderId, String userId) {
		 
		boolean isAvailable = Math.random() > 0.2; // Simulating random payment failure
		if(isAvailable) {
			kafkaProducerService.sendInventoryEvent(new InventoryEvent(orderId, Long.valueOf(userId), "INVENTORY_SUCCESS"));  
		}else {
			kafkaProducerService.sendInventoryEvent(new InventoryEvent(orderId, Long.valueOf(userId), "INVENTORY_FAILURE")); 
			orderService.cancelOrder(orderId,"INVENTORY_FAILED");
		}
		
		
	}
}