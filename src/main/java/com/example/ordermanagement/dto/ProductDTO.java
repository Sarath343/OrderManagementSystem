package com.example.ordermanagement.dto;

public class ProductDTO {
	 private Long id;
	    private String name;
	    private double mrp;
	    private int qty;
	    private double discount;
	    private boolean freeWithBuntle;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getMrp() {
			return mrp;
		}
		public void setMrp(double mrp) {
			this.mrp = mrp;
		}
		public int getQty() {
			return qty;
		}
		public void setQty(int qty) {
			this.qty = qty;
		}
		public double getDiscount() {
			return discount;
		}
		public void setDiscount(double discount) {
			this.discount = discount;
		}
		public boolean isFreeWithBuntle() {
			return freeWithBuntle;
		}
		public void setFreeWithBuntle(boolean freeWithBuntle) {
			this.freeWithBuntle = freeWithBuntle;
		}
	    
	    
	    
}
