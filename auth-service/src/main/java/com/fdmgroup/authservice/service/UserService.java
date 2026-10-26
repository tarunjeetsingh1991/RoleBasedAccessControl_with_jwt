package com.fdmgroup.authservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdmgroup.authservice.dto.CreateUserRequest;
import com.fdmgroup.authservice.dto.UserResponse;
import com.fdmgroup.authservice.model.AppUser;
import com.fdmgroup.authservice.model.Role;
import com.fdmgroup.authservice.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role. Use ROLE_USER or ROLE_ADMIN");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().name(),
                        user.isEnabled()
                ))
                .toList();
    }
}