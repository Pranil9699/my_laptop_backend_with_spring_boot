// com.mylaptop.org.dto.RentalResponse.java
package com.mylaptop.org.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalResponse {
    private Long rentalId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String userName;
    private String userEmail;
    private String laptopModel;
    private BigDecimal paymentAmount;
    private String paymentStatus;
    private String transactionId;
	public Long getRentalId() {
		return rentalId;
	}
	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getLaptopModel() {
		return laptopModel;
	}
	public void setLaptopModel(String laptopModel) {
		this.laptopModel = laptopModel;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

    // getters & setters
    
}
