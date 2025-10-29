package com.mylaptop.org.model;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")

public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status; // ACTIVE / COMPLETED / CANCELLED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptop_id")
    @JsonBackReference
    private Laptop laptop;

    @OneToOne(mappedBy = "rental", cascade = CascadeType.ALL)
    @JsonBackReference
    private Payment payment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Laptop getLaptop() {
		return laptop;
	}

	public void setLaptop(Laptop laptop) {
		this.laptop = laptop;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Rental() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Rental(Long id, LocalDate startDate, LocalDate endDate, String status, User user, Laptop laptop,
			Payment payment) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.user = user;
		this.laptop = laptop;
		this.payment = payment;
	}

	@Override
	public String toString() {
		return "Rental [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status
				+ ", user=" + user + ", laptop=" + laptop + ", payment=" + payment + "]";
	}
    
}
