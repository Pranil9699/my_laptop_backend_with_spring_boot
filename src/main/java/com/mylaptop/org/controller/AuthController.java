package com.mylaptop.org.controller;

import com.mylaptop.org.model.User;
import com.mylaptop.org.model.Role;
import com.mylaptop.org.repository.UserRepository;
import com.mylaptop.org.repository.RoleRepository;
import com.mylaptop.org.security.JwtTokenHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenHelper jwtTokenHelper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    // ✅ REGISTER Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User userRequest) {
        if (userRequest == null || userRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request"));
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }

        // find ROLE_USER or create if absent
//        Role userRole = roleRepository.findByName("ROLE_ADMIN")
//                .orElseGet(() -> {
//                    Role r = new Role();
//                    r.setName("ROLE_ADMIN");
//                    return roleRepository.save(r);
//                });
        Role userRole = roleRepository.findByName("ROLE_USER")
        		.orElseGet(() -> {
        			Role r = new Role();
        			r.setName("ROLE_USER");
        			return roleRepository.save(r);
        		});

        String fullName = (userRequest.getFullName() == null || userRequest.getFullName().isBlank())
                ? "User" : userRequest.getFullName();
        String phone = (userRequest.getPhone() == null) ? "Not provided" : userRequest.getPhone();
        String address = (userRequest.getAddress() == null) ? "Not provided" : userRequest.getAddress();

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhone(phone);
        user.setAddress(address);
        user.setActive(true);
        user.setRoles(Set.of(userRole)); // ✅ Corrected

        User saved = userRepository.save(user);

        // ✅ generate JWT token using JwtTokenHelper
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtTokenHelper.generateToken(userDetails);

        Map<String, Object> userSafe = Map.of(
                "id", saved.getId(),
                "fullName", saved.getFullName(),
                "email", saved.getEmail(),
                "phone", saved.getPhone(),
                "address", saved.getAddress(),
                "roles", saved.getRoles()
        );

        return ResponseEntity.status(201).body(Map.of(
                "message", "User registered successfully",
                "token", token,
                "user", userSafe
        ));
    }

    // ✅ LOGIN Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ use JwtTokenHelper instead of JwtService
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtTokenHelper.generateToken(userDetails);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "fullName", user.getFullName(),
                        "email", user.getEmail(),
                        "roles", user.getRoles()
                )
        ));
    }
}
