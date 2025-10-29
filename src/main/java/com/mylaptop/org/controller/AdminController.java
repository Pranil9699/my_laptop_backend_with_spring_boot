package com.mylaptop.org.controller;

import com.mylaptop.org.model.*;
import com.mylaptop.org.repository.LaptopRepository;
import com.mylaptop.org.repository.UserRepository;
import com.mylaptop.org.repository.RentalRepository;
import com.mylaptop.org.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private LaptopRepository laptopRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RentalRepository rentalRepository;
    @Autowired private PaymentRepository paymentRepository;

    // ========================= 1️⃣ INVENTORY MANAGEMENT =========================
    // ✅ GET all laptops
    @GetMapping("/laptops")
    public ResponseEntity<List<Laptop>> getAllLaptops() {
        return ResponseEntity.ok(laptopRepository.findAll());
    }

    // ✅ GET one laptop by ID
    @GetMapping("/laptops/{id}")
    public ResponseEntity<?> getLaptopById(@PathVariable Long id) {
        return laptopRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Laptop not found")));
    }

    // ✅ SEARCH laptops by brand or model
    @GetMapping("/laptops/search")
    public ResponseEntity<List<Laptop>> searchLaptops(@RequestParam String keyword) {
        List<Laptop> all = laptopRepository.findAll();
        List<Laptop> result = new ArrayList<>();
        for (Laptop l : all) {
            if (l.getBrand().toLowerCase().contains(keyword.toLowerCase())
                    || l.getModel().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(l);
            }
        }
        return ResponseEntity.ok(result);
    }

    // ✅ POST - Add single or multiple laptops
    @PostMapping("/laptops")
    public ResponseEntity<?> addLaptops(@RequestBody List<Laptop> laptops) {
        List<Laptop> saved = laptopRepository.saveAll(laptops);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", saved.size() + " laptops added successfully"));
    }

 // ✅ UPDATE - Update laptop availability and rent
    @PutMapping("/laptops/{id}/availableAndRentperDay")
    public ResponseEntity<Map<String, String>> updateLaptopAvailableAndRentPerDay(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updateData) {

        Optional<Laptop> optional = laptopRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Laptop not found"));
        }

        Laptop laptop = optional.get();

        // ✅ Update availability if present
        if (updateData.containsKey("available")) {
            boolean available = Boolean.parseBoolean(updateData.get("available").toString());
            laptop.setAvailable(available);
        }

        // ✅ Update rentPerDay if present
        if (updateData.containsKey("rentPerDay")) {
            try {
                BigDecimal rentPerDay = new BigDecimal(updateData.get("rentPerDay").toString());
                laptop.setRentPerDay(rentPerDay);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid rentPerDay format"));
            }
        }

        laptopRepository.save(laptop);

        return ResponseEntity.ok(Map.of(
                "message", "Laptop updated successfully",
                "laptopId", String.valueOf(laptop.getId()),
                "available", String.valueOf(laptop.getAvailable()),
                "rentPerDay", String.valueOf(laptop.getRentPerDay())
        ));
    }


    // ✅ DELETE - Delete laptop by ID
    @DeleteMapping("/laptops/{id}")
    public ResponseEntity<Map<String, String>> deleteLaptop(@PathVariable Long id) {
        if (!laptopRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Laptop not found"));
        }
        laptopRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Laptop deleted successfully"));
    }


    // ========================= 2️⃣ USER MANAGEMENT =========================
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAllByRoleName("ROLE_USER");

        List<Map<String, Object>> safe = users.stream().map(u -> {
            Map<String, Object> m = new java.util.HashMap<>();
            m.put("id", u.getId());
            m.put("fullName", u.getFullName());
            m.put("email", u.getEmail());
            m.put("phone", u.getPhone());
            m.put("address", u.getAddress());
            m.put("active", u.getActive());
            m.put("role", u.getRole() != null ? u.getRole().getName() : null);
            return m;
        }).toList();

        return ResponseEntity.ok(safe);
    }



 // ✅ Block or Unblock User (Toggle Active Status)
    @PutMapping("/users/{id}/block")
    public ResponseEntity<Map<String, String>> toggleUserBlockStatus(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = userOpt.get();

        // Toggle active status
        boolean newStatus = !user.getActive();
        user.setActive(newStatus);
        userRepository.save(user);

        String statusMessage = newStatus ? "User unblocked successfully" : "User blocked successfully";

        return ResponseEntity.ok(Map.of(
                "message", statusMessage,
                "userId", String.valueOf(user.getId()),
                "userName", user.getFullName() != null ? user.getFullName() : "Unknown",
                "active", String.valueOf(user.getActive())
        ));
    }


    // ✅ Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }


    // ========================= 3️⃣ RENTAL MANAGEMENT =========================
    // ✅ Get all rentals
    @GetMapping("/rentals")
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalRepository.findAll());
    }

    // ✅ Update rental status (e.g., returned or ongoing)
    @PutMapping("/rentals/{id}/status")
    public ResponseEntity<Map<String, String>> updateRentalStatus(
            @PathVariable Long id, @RequestParam String status) {

        Optional<Rental> rentalOpt = rentalRepository.findById(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Rental not found"));
        }

        Rental rental = rentalOpt.get();
        rental.setStatus(status);
        rentalRepository.save(rental);

        return ResponseEntity.ok(Map.of("message", "Rental status updated successfully"));
    }


    // ========================= 4️⃣ PAYMENT MANAGEMENT =========================
    // ✅ Get all payments
    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    // ✅ Verify payment (if cash)
    @PutMapping("/payments/{id}/verify")
    public ResponseEntity<Map<String, String>> verifyPayment(@PathVariable Long id) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Payment not found"));
        }

        Payment payment = paymentOpt.get();
        if ("CASH".equalsIgnoreCase(payment.getPaymentMethod())) {
            payment.setPaymentVerification("VERIFIED");
            paymentRepository.save(payment);
            return ResponseEntity.ok(Map.of("message", "Cash payment verified successfully"));
        } else {
            return ResponseEntity.ok(Map.of("message", "UPI payment is auto-verified"));
        }
    }
}
