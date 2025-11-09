package com.mylaptop.org.dto;

import java.math.BigDecimal;


public class PaymentResponse {
    private Long id;
    private String brand;
    private String model;
    private String paymentMethod;
    private BigDecimal amount;
    private String transactionId;
    private String status;
    private String paymentVerification;
    public PaymentResponse() {
    }
    public String getPaymentMethod(){
    return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod){
    this.paymentMethod=paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentVerification() {
        return paymentVerification;
    }

    public void setPaymentVerification(String paymentVerification) {
        this.paymentVerification = paymentVerification;
    }
}

/*
 * <td className="p-2">{p.rental?.laptop?.model || "-"}</td>
                <td className="p-2">{p.rental?.laptop?.model || "-"}</td>
                <td className="p-2">{p.rental?.laptop?.brand || "-"}</td>
                <td className="p-2">₹{p.amount ?? "-"}</td>
                <td className="p-2">{p.paymentMethod || "-"}</td>
                <td className="p-2 font-mono">{p.transactionId || "-"}</td>
                <td className="p-2">
                  {p.status} 
                </td>
                <td className="p-2">
                  {p.paymentVerification ? `${p.paymentVerification}` : ""} 
                </td>
 */