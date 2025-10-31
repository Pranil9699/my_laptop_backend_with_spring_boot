package com.mylaptop.org.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mylaptop.org.model.Laptop;
import com.mylaptop.org.model.Rental;
import com.mylaptop.org.repository.LaptopRepository;
import com.mylaptop.org.repository.RentalRepository;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    @Transactional
    @Scheduled(fixedRate = 86400000) // runs every 24 hours
    public void cancelUnpaidRentals() {
        List<Rental> unpaidRentals = rentalRepository.findAll().stream()
                .filter(r -> r.getPayment() == null ||
                        !"COMPLETED".equalsIgnoreCase(r.getPayment().getStatus()))
                .filter(r -> r.getStartDate().isBefore(LocalDate.now())) // more than 1 day old
                .toList();

        for (Rental r : unpaidRentals) {
            Laptop laptop = r.getLaptop();
            laptop.setAvailable(true);
            laptopRepository.save(laptop);
            rentalRepository.delete(r);
        }
    }
}
