package com.mylaptop.org.service;

import com.mylaptop.org.model.Rental;
import com.mylaptop.org.model.Laptop;
import com.mylaptop.org.repository.RentalRepository;
import com.mylaptop.org.repository.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalExpiryService {

    @Autowired private RentalRepository rentalRepository;
    @Autowired private LaptopRepository laptopRepository;

    // runs every minute
    @Transactional
    // @Scheduled(fixedRate = 900_000)
    @Scheduled(cron = "0 */15 * * * *")
    // @Scheduled(fixedRate = 60_000)
    public void cancelExpiredUnpaidRentals() {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> expired = rentalRepository.findExpiredUnpaidRentals(now);
        // System.out.println("Hi"+expired.size());
        for (Rental r : expired) {
            try {
                // mark laptop available again
                Laptop laptop = r.getLaptop();
                laptop.setAvailable(true);
                laptopRepository.save(laptop);
                // System.out.println("CANCLEED");
                // mark rental cancelled (or delete if preferred)
                r.setStatus("CANCELLED");
                rentalRepository.save(r);
            } catch (Exception ex) {
                // log and continue (do not stop the loop)
                ex.printStackTrace();
            }
        }
    }
}
