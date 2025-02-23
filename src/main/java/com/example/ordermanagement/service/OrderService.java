package com.example.ordermanagement.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.ProductDTO;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.OrderItem;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.entity.User;
import com.example.ordermanagement.event.OrderEvent;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final KafkaProducerService kafkaProducerService;

	public OrderService(OrderRepository orderRepository, KafkaProducerService kafkaProducerService,
			UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.kafkaProducerService = kafkaProducerService;
		this.userRepository = userRepository;
	}

	@Transactional
	public Order placeOrder(OrderDTO orderDto) {
		Order order = new Order(orderDto.getUserId(), "PENDING_PAYMENT");
		Double totalPrice = 0D;
		for (ProductDTO prod : orderDto.getProducts()) {
			Double price = prod.isFreeWithBuntle() ? 0D : prod.getMrp() * (1 - prod.getDiscount() / 100);
			Optional<User> user = userRepository.findById(orderDto.getUserId());
			if (user.isPresent()) {
				price = user.get().getCreditCardType() == "Sapphiro" ? price * 0.95 : price;
			}
			Product p = new Product(prod.getId(), prod.getName(), price);
			OrderItem item = new OrderItem(null, p, prod.getQty());
			order.addOrderItem(item);
			totalPrice += price * prod.getQty();
		}
		order.setTotalAmount(totalPrice);
		Order savedOrder = orderRepository.save(order);

		kafkaProducerService
				.sendOrderEvent(new OrderEvent(savedOrder.getId(), savedOrder.getUserId(), "ORDER_CREATED"));

		return savedOrder;
	}

	public void cancelOrder(Long orderId, String message) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(message);
		orderRepository.save(order);
	}

	public void shipOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus("SHIPPED");
		orderRepository.save(order);
	}
}
