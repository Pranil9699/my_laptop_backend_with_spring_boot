package com.mylaptop.org.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(nullable = false)
    private String paymentVerification;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @OneToOne
    @JoinColumn(name = "rental_id", nullable = false)
    @JsonManagedReference("rental-payment")
    private Rental rental;

    @PrePersist
    public void ensureTransactionId() {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            transactionId = "TXN-" + UUID.randomUUID();
        }
    }

    // --- Getters / Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentVerification() { return paymentVerification; }
    public void setPaymentVerification(String paymentVerification) { this.paymentVerification = paymentVerification; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Rental getRental() { return rental; }
    public void setRental(Rental rental) { this.rental = rental; }
}
