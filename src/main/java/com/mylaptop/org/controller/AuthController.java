package com.mylaptop.org.controller;

import com.mylaptop.org.model.User;
import com.mylaptop.org.model.Role;
import com.mylaptop.org.repository.UserRepository;
import com.mylaptop.org.repository.RoleRepository;
import com.mylaptop.org.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

 // imports omitted for brevity
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User userRequest) {
        if (userRequest == null || userRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request"));
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }

        // find ROLE_USER or create if absent
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_USER");
                    r.setCode(501);
                    return roleRepository.save(r);
                });

        // Fill defaults when fields are missing
        String fullName = (userRequest.getFullName() == null || userRequest.getFullName().isBlank())
                ? "User" : userRequest.getFullName();
        String phone = (userRequest.getPhone() == null) ? "Not provided" : userRequest.getPhone();
        String address = (userRequest.getAddress() == null) ? "Not provided" : userRequest.getAddress();

        // create user entity (don't reuse userRequest instance to avoid unexpected fields)
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhone(phone);
        user.setAddress(address);
        user.setActive(true);
        user.setRole(userRole);

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getEmail());

        // build safe response (do not include password)
        Map<String, Object> userSafe = Map.of(
                "id", saved.getId(),
                "fullName", saved.getFullName(),
                "email", saved.getEmail(),
                "phone", saved.getPhone(),
                "address", saved.getAddress(),
                "role", saved.getRole() != null ? saved.getRole().getName() : null
        );

        return ResponseEntity.status(201).body(Map.of(
                "message", "User registered successfully",
                "token", token,
                "user", userSafe
        ));
    }


    // ✅ Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "fullName", user.getFullName(),
                        "email", user.getEmail(),
                        "role", user.getRole().getName()
                )
        ));
    }
}
