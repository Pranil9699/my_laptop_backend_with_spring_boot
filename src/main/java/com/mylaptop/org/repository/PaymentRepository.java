package com.mylaptop.org.repository;

import com.mylaptop.org.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // ✅ Fetch all payments related to rentals owned by a given user
    @Query("SELECT p FROM Payment p WHERE p.rental.user = :user")
    List<Payment> findByRentalUser(User user);
}
