package com.mylaptop.org.security;

import com.mylaptop.org.model.User;
import com.mylaptop.org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new CustomUserDetails(u);
    }
}
