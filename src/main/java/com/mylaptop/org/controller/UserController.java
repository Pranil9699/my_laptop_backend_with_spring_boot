package com.mylaptop.org.controller;

import com.mylaptop.org.dto.RentalResponse;
import com.mylaptop.org.model.*;
import com.mylaptop.org.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserRepository userRepository;
    @Autowired private LaptopRepository laptopRepository;
    @Autowired private RentalRepository rentalRepository;
    @Autowired private PaymentRepository paymentRepository;

    // 👤 1️⃣ Get user profile (by userId directly)
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
    	System.out.print("Hi \n their ");
        return userRepository.findById(userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found")));
    }

    // ✏️ 2️⃣ Update user profile (by userId)
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedInfo) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setFullName(updatedInfo.getFullName());
                    user.setPhone(updatedInfo.getPhone());
                    user.setAddress(updatedInfo.getAddress());
                    userRepository.save(user);
                    return ResponseEntity.ok(Map.of(
                            "message", "Profile updated successfully",
                            "user", user.getFullName()
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found")));
    }

    // 💻 3️⃣ Get all available laptops (public)
    @GetMapping("/laptops")
    public ResponseEntity<?> getAvailableLaptops() {
        List<Laptop> laptops = laptopRepository.findByAvailableTrueAndBlockedFalse();
        return ResponseEntity.ok(laptops);
    }

    // 🛒 4️⃣ Rent a laptop (by userId)
    @PostMapping("/rent/{laptopId}/user/{userId}")
    public ResponseEntity<?> rentLaptop(@PathVariable Long laptopId, @PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Laptop> laptopOpt = laptopRepository.findById(laptopId);

        if (userOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        if (laptopOpt.isEmpty() || !laptopOpt.get().getAvailable())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Laptop not available"));

        Laptop laptop = laptopOpt.get();
        User user = userOpt.get();

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setLaptop(laptop);
        rental.setStartDate(LocalDate.now());
        rental.setEndDate(LocalDate.now().plusDays(7));
        rental.setStatus("ONGOING");
        rentalRepository.save(rental);

        laptop.setAvailable(false);
        laptopRepository.save(laptop);

        return ResponseEntity.ok(Map.of(
                "message", "Laptop rented successfully",
                "rentalId", rental.getId(),
                "laptop", laptop.getModel()
        ));
    }

    // 💵 5️⃣ Make payment for rental
    @PostMapping("/payment/{rentalId}/user/{userId}")
    public ResponseEntity<?> makePayment(
            @PathVariable Long rentalId,
            @PathVariable Long userId,
            @RequestBody Map<String, Object> paymentData) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));

        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);
        if (rentalOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Rental not found"));

        Rental rental = rentalOpt.get();

        // ✅ Extract values from request
        String method = paymentData.get("paymentMethod").toString();
        BigDecimal amount = new BigDecimal(paymentData.get("amount").toString());
        String transactionId = paymentData.get("transactionId") != null
                ? paymentData.get("transactionId").toString()
                : "TXN-" + System.currentTimeMillis();

        // ✅ Create payment
        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setTransactionId(transactionId);
        payment.setStatus("SUCCESS");
        payment.setPaymentVerification(method.equalsIgnoreCase("CASH") ? "NOT_VERIFIED" : "VERIFIED");
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        // ✅ Build response
        return ResponseEntity.ok(Map.of(
                "message", "Payment recorded successfully",
                "paymentId", payment.getId(),
                "transactionId", payment.getTransactionId(),
                "status", payment.getStatus(),
                "verification", payment.getPaymentVerification()
        ));
    }

    @GetMapping("/rentals/{userId}")
    public ResponseEntity<?> getUserRentals(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        List<Rental> rentals = rentalRepository.findByUser(userOpt.get());
        List<RentalResponse> response = rentals.stream()
        	    .map(rental -> {
        	        RentalResponse dto = new RentalResponse();
        	        dto.setRentalId(rental.getId());
        	        dto.setStartDate(rental.getStartDate());
        	        dto.setEndDate(rental.getEndDate());
        	        dto.setStatus(rental.getStatus());
        	        dto.setUserName(rental.getUser().getFullName());
        	        dto.setUserEmail(rental.getUser().getEmail());
        	        dto.setLaptopModel(rental.getLaptop().getModel());
        	        if (rental.getPayment() != null) {
        	            dto.setPaymentAmount(rental.getPayment().getAmount());
        	            dto.setPaymentStatus(rental.getPayment().getStatus());
        	            dto.setTransactionId(rental.getPayment().getTransactionId());
        	        }
        	        return dto;
        	    })
        	    .toList(); // ✅ works on Java 16+
        

        return ResponseEntity.ok(response);
    }


    // 💳 7️⃣ View payment history by user
    @GetMapping("/payments/{userId}")
    public ResponseEntity<?> getUserPayments(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(paymentRepository.findByRentalUser(user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found")));
    }
}
