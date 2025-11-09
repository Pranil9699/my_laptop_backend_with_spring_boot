package com.mylaptop.org.controller;

import com.mylaptop.org.dto.PaymentResponse;
import com.mylaptop.org.dto.RentalResponse;
import com.mylaptop.org.dto.UnpaidRentalDto;
import com.mylaptop.org.model.*;
import com.mylaptop.org.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LaptopRepository laptopRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private PaymentRepository paymentRepository;

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
                            "user", user.getFullName()));
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
        // set expiry 15 minutes from now (for unpaid payment)
        rental.setExpiryTime(LocalDateTime.now().plusMinutes(15));
        rentalRepository.save(rental);

        // Mark laptop unavailable
        laptop.setAvailable(false);
        laptopRepository.save(laptop);

        // ✅ Assuming Laptop entity has field `rentPrice`
        return ResponseEntity.ok(Map.of(
                "message", "Laptop rented successfully",
                "rentalId", rental.getId(),
                "laptop", laptop.getModel(),
                "rentAmount", laptop.getRentPerDay()));
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

        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setStatus("SUCCESS");
        payment.setPaymentVerification(
                method.equalsIgnoreCase("CASH") ? "NOT_VERIFIED" : "VERIFIED");
        payment.setPaymentDate(LocalDateTime.now());

        // No need to set transactionId manually — @PrePersist handles it

        paymentRepository.save(payment);

        return ResponseEntity.ok(Map.of(
                "message", "Payment recorded successfully",
                "paymentId", payment.getId(),
                "transactionId", payment.getTransactionId(),
                "status", payment.getStatus(),
                "verification", payment.getPaymentVerification()));
    }

    @GetMapping("/rentals/{userId}")
    public ResponseEntity<?> getUserRentals(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        System.out.println("Hi-1");
        if (userOpt.isEmpty()) {
            System.out.println("Hi-2");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        System.out.println("Hi-3");
        List<Rental> rentals = rentalRepository.findRentalsWithDetailsByUserId(userOpt.get().getId());
        List<RentalResponse> response = rentals.stream()
                .map(rental -> {
                    System.out.println("Hi-4");
                    RentalResponse dto = new RentalResponse();
                    dto.setRentalId(rental.getId());
                    dto.setStartDate(rental.getStartDate());
                    dto.setEndDate(rental.getEndDate());
                    dto.setStatus(rental.getStatus());
                    dto.setUserName(rental.getUser().getFullName());
                    dto.setUserEmail(rental.getUser().getEmail());
                    dto.setLaptopModel(rental.getLaptop().getModel());
                    dto.setPaymentAmount(rental.getLaptop().getRentPerDay());
                    if (rental.getPayment() != null) {
                        // System.out.println("The rent amount "+rental.getLaptop().getRentPerDay());
                        dto.setPaymentStatus(rental.getPayment().getStatus());
                        dto.setTransactionId(rental.getPayment().getTransactionId());
                    }
                    // System.out.println("Amount"+dto.getPaymentAmount());
                    return dto;
                })
                .toList(); // ✅ works on Java 16+

        System.out.println("Hi-5");

        return ResponseEntity.ok(response);
    }

    // 💳 7️⃣ View payment history by user
    // @GetMapping("/payments/{userId}")
    // public ResponseEntity<?> getUserPayments(@PathVariable Long userId) {
    // return userRepository.findById(userId)
    // .<ResponseEntity<?>>map(user ->
    // ResponseEntity.ok(paymentRepository.findByRentalUser(user)))
    // .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
    // .body(Map.of("error", "User not found")));
    // }

    // GET unpaid rentals that need payment (with remaining minutes)

    @GetMapping("/unpaid-rentals/{userId}")
    public ResponseEntity<?> getUnpaidRentals(@PathVariable Long userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        List<Rental> rentals = rentalRepository.findUnpaidRentalsByUserId(userId, LocalDateTime.now());
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;

        List<UnpaidRentalDto> dtoList = rentals.stream().map(r -> {
            UnpaidRentalDto dto = new UnpaidRentalDto();
            dto.setRentalId(r.getId());
            dto.setLaptopId(r.getLaptop().getId());
            dto.setLaptopName(r.getLaptop().getModel());
            dto.setBrand(r.getLaptop().getBrand());
            dto.setModel(r.getLaptop().getModel());
            dto.setRentPerDay(r.getLaptop().getRentPerDay());
            dto.setStartDate(r.getStartDate() != null ? r.getStartDate().format(dtf) : null);
            dto.setEndDate(r.getEndDate() != null ? r.getEndDate().format(dtf) : null);

            LocalDateTime expiry = r.getExpiryTime();
            long remaining = expiry != null ? Duration.between(LocalDateTime.now(), expiry).toMinutes()
                    : Long.MAX_VALUE;
            dto.setRemainingMinutes(Math.max(0, remaining));
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/payments/{userId}")
    public ResponseEntity<?> getPaymentsByUser(@PathVariable Long userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        List<Payment> payments = paymentRepository.findPaymentsByUserId(userId);

       List<PaymentResponse> paymentResponses = payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentResponses);
    }

     private PaymentResponse convertToDto(Payment payment) {
        PaymentResponse dto = new PaymentResponse();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTransactionId(payment.getTransactionId());
        dto.setStatus(payment.getStatus());
        dto.setPaymentVerification(payment.getPaymentVerification());

        // Access the laptop data through the rental relationship
        if (payment.getRental() != null && payment.getRental().getLaptop() != null) {
            dto.setBrand(payment.getRental().getLaptop().getBrand());
            dto.setModel(payment.getRental().getLaptop().getModel());
        } else {
            // Handle cases where rental or laptop might be null
            dto.setBrand("-"); 
            dto.setModel("-");
        }

        return dto;
    }
}
