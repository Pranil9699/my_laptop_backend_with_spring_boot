package com.mylaptop.org.model;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "payments")

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentMethod; // "UPI" or "CASH"

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status; // "SUCCESS", "FAILED", "PENDING"

    private LocalDateTime paymentDate = LocalDateTime.now();

    /**
     * Indicates whether payment was verified or not.
     * For UPI -> "VERIFIED"
     * For CASH -> "NOT_VERIFIED"
     */
    @Column(nullable = false)
    private String paymentVerification;
    @Column(nullable = true, unique = true)
    private String transactionId;
    

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentVerification() {
		return paymentVerification;
	}

	public void setPaymentVerification(String paymentVerification) {
		this.paymentVerification = paymentVerification;
	}

	public Rental getRental() {
		return rental;
	}

	public void setRental(Rental rental) {
		this.rental = rental;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", paymentMethod=" + paymentMethod + ", amount=" + amount + ", status=" + status
				+ ", paymentDate=" + paymentDate + ", paymentVerification=" + paymentVerification + ", rental=" + rental
				+ "]";
	}

	public Payment(Long id, String paymentMethod, BigDecimal amount, String status, LocalDateTime paymentDate,
			String paymentVerification,String transactionId, Rental rental) {
		super();
		this.id = id;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.status = status;
		this.paymentDate = paymentDate;
		this.paymentVerification = paymentVerification;
		this.transactionId=transactionId;
		this.rental = rental;
	}

	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
