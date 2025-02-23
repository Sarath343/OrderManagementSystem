package com.example.ordermanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
	Long userId;
	List<ProductDTO>products;
	LocalDate date ;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<ProductDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
	public LocalDate getTime() {
		return date;
	}
	public void setTime(LocalDate time) {
		this.date = time;
	}
	
}
