package com.fdmgroup.authservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.authservice.dto.AuthResponse;
import com.fdmgroup.authservice.dto.CreateAdminRequest;
import com.fdmgroup.authservice.dto.LoginRequest;
import com.fdmgroup.authservice.model.AppUser;
import com.fdmgroup.authservice.model.Role;
import com.fdmgroup.authservice.repository.UserRepository;
import com.fdmgroup.authservice.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER FIRST ADMIN
    // =========================
    public AuthResponse registerAdmin(CreateAdminRequest request) {

        // allow only one admin to be created publicly
        if (userRepository.existsByRole(Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException(
                    "Admin already exists. Use admin APIs to create users.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists");
        }

        AppUser admin = new AppUser();
        admin.setUsername(request.getUsername());

        // hash password
        admin.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        admin.setRole(Role.ROLE_ADMIN);
        admin.setEnabled(true);

        AppUser saved = userRepository.save(admin);

        // generate JWT
        String token = jwtService.generateToken(saved);

        return new AuthResponse(
                token,
                saved.getUsername(),
                saved.getRole().name()
        );
    }

    // =========================
    // LOGIN
    // =========================
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        AppUser user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid username or password"));

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}