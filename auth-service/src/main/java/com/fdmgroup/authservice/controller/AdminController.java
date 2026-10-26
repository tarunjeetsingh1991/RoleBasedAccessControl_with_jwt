package com.fdmgroup.authservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.authservice.dto.CreateUserRequest;
import com.fdmgroup.authservice.dto.UserResponse;
import com.fdmgroup.authservice.model.AppUser;
import com.fdmgroup.authservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        AppUser saved = userService.createUser(request);

        UserResponse response = new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole().name(),
                saved.isEnabled()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}