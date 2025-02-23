package com.example.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ordermanagement.entity.OrderItem;

interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
