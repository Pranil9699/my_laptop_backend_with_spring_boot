package com.mylaptop.org.repository;

import com.mylaptop.org.model.Rental;
import com.mylaptop.org.model.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

	// @EntityGraph(attributePaths = {"laptop", "payment"})
	// List<Rental> findByUserId(Long userId);
	// @Query("SELECT r FROM Rental r " +
	// 		"JOIN FETCH r.laptop " +
	// 		"JOIN FETCH r.payment " +
	// 		"WHERE r.user.id = :userId")
	// List<Rental> findRentalsWithDetailsByUserId(@Param("userId") Long userId);
	@Query("""
        select r
        from Rental r
        left join fetch r.laptop l
        left join fetch r.payment p
        where r.user.id = :userId
        order by r.startDate desc
        """)
    List<Rental> findRentalsWithDetailsByUserId(@Param("userId") Long userId);

	List<Rental> findByStatus(String status);

	// 1) Unpaid rentals for a specific user (NOT expired). Fetch laptop for UI convenience.
    @Query("""
        select r
        from Rental r
        left join r.payment p            
        join fetch r.laptop l             
        where r.user.id = :userId
          and p.id is null                
          and r.status = 'ONGOING'
          and (r.expiryTime is null or r.expiryTime > :now)
        order by r.startDate desc
        """)
    List<Rental> findUnpaidRentalsByUserId(@Param("userId") Long userId,
                                          @Param("now") LocalDateTime now);


    // 2) Expired unpaid rentals (for scheduler): rentals whose expiryTime <= now and have no payment
    @Query("""
        select r
        from Rental r
        left join r.payment p
        where p.id is null
          and r.expiryTime <= :now
          and r.status = 'ONGOING'
        """)
    List<Rental> findExpiredUnpaidRentals(@Param("now") LocalDateTime now);

	// find rentals with payment and laptop fetched (for history)
	@Query("select r from Rental r left join fetch r.laptop l left join fetch r.payment p " +
			"where r.user.id = :userId and r.payment is not null")
	List<Rental> findPaidRentalsByUserId(@Param("userId") Long userId);

	// find expired unpaid rentals (expiryTime <= now) to cancel
	// @Query("select r from Rental r where r.expiryTime <= :now and r.payment is null and r.status = 'ONGOING'")
	// List<Rental> findExpiredUnpaidRentals(@Param("now") LocalDateTime now);

}
