package com.mylaptop.org.repository;

import com.mylaptop.org.model.Rental;
import com.mylaptop.org.model.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

//	@EntityGraph(attributePaths = {"laptop", "payment"})
//	List<Rental> findByUserId(Long userId);
	@Query("SELECT r FROM Rental r " +
		       "JOIN FETCH r.laptop " +
		       "JOIN FETCH r.payment " +
		       "WHERE r.user.id = :userId")
		List<Rental> findRentalsWithDetailsByUserId(@Param("userId") Long userId);
    List<Rental> findByStatus(String status);
}
