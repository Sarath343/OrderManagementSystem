package com.example.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ordermanagement.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}
