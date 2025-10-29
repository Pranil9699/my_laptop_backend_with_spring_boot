package com.mylaptop.org.repository;

import com.mylaptop.org.model.Rental;
import com.mylaptop.org.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByUser(User user);
    List<Rental> findByStatus(String status);
}
