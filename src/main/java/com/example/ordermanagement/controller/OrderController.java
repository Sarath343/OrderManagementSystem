package com.example.ordermanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.service.OrderService;

@RestController
@RequestMapping("/orders")
class OrderController {
	private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDTO order) {
         Order savedOrder =  orderService.placeOrder(order);
         return ResponseEntity.ok(savedOrder);
    }

}

