package com.mylaptop.org.repository;

import com.mylaptop.org.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {

    List<Laptop> findByAvailableTrueAndBlockedFalse();
    // List<Laptop> findByBrandContainingIgnoreCase(String brand);
     // ✅ This derived query searches in multiple fields
    @Query("SELECT l FROM Laptop l WHERE " +
           "LOWER(l.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.processor) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Laptop> searchLaptops(@Param("keyword") String keyword);
}
