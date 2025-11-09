package com.mylaptop.org.controller;

import com.mylaptop.org.model.Laptop;
import com.mylaptop.org.model.Rental;
import com.mylaptop.org.repository.RentalRepository;
import com.mylaptop.org.repository.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*") // allow frontend access
public class HomeController {

    @Autowired
    private LaptopRepository laptopRepository;

     @Autowired private RentalRepository rentalRepository;
    // 💻 Get all available laptops (public view)
    @GetMapping("/laptops")
    public List<Laptop> getAllLaptops() {
        // Return only laptops that are available and not blocked
        return laptopRepository.findAll()
                .stream()
                .filter(l -> Boolean.TRUE.equals(l.getAvailable()) && Boolean.FALSE.equals(l.getBlocked()))
                .collect(Collectors.toList());
    }

    // 💻 Get single laptop details (only if available)
    @GetMapping("/laptops/{id}")
    public Object getLaptopById(@PathVariable Long id) {
        Optional<Laptop> laptopOpt = laptopRepository.findById(id);
        if (laptopOpt.isPresent()) {
            Laptop laptop = laptopOpt.get();
            if (Boolean.TRUE.equals(laptop.getAvailable()) && Boolean.FALSE.equals(laptop.getBlocked())) {
                return laptop;
            } else {
                return Map.of("error", "This laptop is currently rented or blocked.");
            }
        } else {
            return Map.of("error", "Laptop not found.");
        }
    }

    @GetMapping("/laptops/search")
    public List<Laptop> searchLaptops(@RequestParam("query") String query) {
        if (query == null || query.trim().isEmpty()) {
            return laptopRepository.findByAvailableTrueAndBlockedFalse();
        }
        return laptopRepository.searchLaptops(query.trim());
    }

    // 🏫 About Us
    @GetMapping("/about")
    public Map<String, String> getAboutInfo() {
        return Map.of(
                "title", "About My Laptop Rental System",
                "description",
                "My Laptop is a trusted rental platform offering affordable and flexible laptop rentals for students, professionals, and businesses. We focus on reliability, performance, and convenience.");
    }

    // 📞 Contact Info
    @GetMapping("/contact")
    public Map<String, String> getContactInfo() {
        return Map.of(
                "email", "support@mylaptop.com",
                "phone", "+91 9699532910",
                "address", "Pune, Maharashtra, India",
                "workingHours", "Mon - Sat (9:00 AM - 6:00 PM)");
    }

    @GetMapping("/debug/unpaid/{userId}")
    public ResponseEntity<?> debugUnpaid(@PathVariable Long userId) {
        List<Rental> unpaid = rentalRepository.findUnpaidRentalsByUserId(userId, LocalDateTime.now());
        return ResponseEntity.ok(unpaid);
    }
}
