package com.mylaptop.org.dto;

import java.math.BigDecimal;

public class UnpaidRentalDto {
    private Long rentalId;
    private Long laptopId;
    private String laptopName;
    private String brand;
    private String model;
    private BigDecimal rentPerDay;
    private String startDate;
    private String endDate;
    private long remainingMinutes;

    // getters / setters
    // ...
    public UnpaidRentalDto() {
    }

    public UnpaidRentalDto(Long rentalId, Long laptopId, String laptopName, String brand, String model, BigDecimal rentPerDay, String startDate, String endDate, long remainingMinutes) {
        this.rentalId = rentalId;
        this.laptopId = laptopId;
        this.laptopName = laptopName;
        this.brand = brand;
        this.model = model;
        this.rentPerDay = rentPerDay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingMinutes = remainingMinutes;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Long getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(Long laptopId) {
        this.laptopId = laptopId;
    }

    public String getLaptopName() {
        return laptopName;
    }

    public void setLaptopName(String laptopName) {
        this.laptopName = laptopName;
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

    public BigDecimal getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(BigDecimal rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getRemainingMinutes() {
        return remainingMinutes;
    }

    public void setRemainingMinutes(long remainingMinutes) {
        this.remainingMinutes = remainingMinutes;
    }
}
