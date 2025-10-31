package com.mylaptop.org.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "laptops")
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String processor;
    private String ram;
    private String storage;
    private String graphicsCard;
    private String conditionStatus;
    private BigDecimal rentPerDay;

    @Column(length = 500)
    private String description;
    private String imageName;
    private Boolean available = true;
    private Boolean blocked = false;

    @OneToMany(mappedBy = "laptop", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("laptop-rentals")
    private List<Rental> rentals = new ArrayList<>();

    // --- Getters / Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getProcessor() { return processor; }
    public void setProcessor(String processor) { this.processor = processor; }
    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }
    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }
    public String getGraphicsCard() { return graphicsCard; }
    public void setGraphicsCard(String graphicsCard) { this.graphicsCard = graphicsCard; }
    public String getConditionStatus() { return conditionStatus; }
    public void setConditionStatus(String conditionStatus) { this.conditionStatus = conditionStatus; }
    public BigDecimal getRentPerDay() { return rentPerDay; }
    public void setRentPerDay(BigDecimal rentPerDay) { this.rentPerDay = rentPerDay; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public Boolean getBlocked() { return blocked; }
    public void setBlocked(Boolean blocked) { this.blocked = blocked; }
    public List<Rental> getRentals() { return rentals; }
    public void setRentals(List<Rental> rentals) { this.rentals = rentals; }

	public Laptop(Long id, String brand, String model, String processor, String ram, String storage,
			String graphicsCard, String conditionStatus, BigDecimal rentPerDay, String description, String imageName,
			Boolean available, Boolean blocked, List<Rental> rentals) {
		super();
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.processor = processor;
		this.ram = ram;
		this.storage = storage;
		this.graphicsCard = graphicsCard;
		this.conditionStatus = conditionStatus;
		this.rentPerDay = rentPerDay;
		this.description = description;
		this.imageName = imageName;
		this.available = available;
		this.blocked = blocked;
		this.rentals = rentals;
	}

	@Override
	public String toString() {
		return "Laptop [id=" + id + ", brand=" + brand + ", model=" + model + ", processor=" + processor + ", ram="
				+ ram + ", storage=" + storage + ", graphicsCard=" + graphicsCard + ", conditionStatus="
				+ conditionStatus + ", rentPerDay=" + rentPerDay + ", description=" + description + ", imageName="
				+ imageName + ", available=" + available + ", blocked=" + blocked + ", rentals=" + rentals + "]";
	}

	public Laptop() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
