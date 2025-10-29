package com.mylaptop.org.repository;

import com.mylaptop.org.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {

    List<Laptop> findByAvailableTrueAndBlockedFalse();
    List<Laptop> findByBrandContainingIgnoreCase(String brand);
}
