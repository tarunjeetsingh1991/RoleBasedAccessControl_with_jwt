/*
package com.fdmgroup.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.authservice.dto.AuthResponse;
import com.fdmgroup.authservice.dto.CreateAdminRequest;
import com.fdmgroup.authservice.dto.LoginRequest;
import com.fdmgroup.authservice.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(
            @Valid @RequestBody CreateAdminRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Auth service is running");
    }
}
*/

package com.fdmgroup.authservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.authservice.dto.AuthResponse;
import com.fdmgroup.authservice.dto.CreateAdminRequest;
import com.fdmgroup.authservice.dto.LoginRequest;
import com.fdmgroup.authservice.security.CookieService;
import com.fdmgroup.authservice.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @Value("${app.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${app.cookie.max-age-seconds:3600}")
    private long cookieMaxAgeSeconds;

    public AuthController(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(
            @Valid @RequestBody CreateAdminRequest request) {

        AuthResponse response = authService.registerAdmin(request);

        String setCookie = cookieService.buildJwtCookie(
                response.getToken(),
                cookieMaxAgeSeconds,
                secureCookie
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, setCookie)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        String setCookie = cookieService.buildJwtCookie(
                response.getToken(),
                cookieMaxAgeSeconds,
                secureCookie
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, setCookie)
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        String clearCookie = cookieService.clearJwtCookie(secureCookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie)
                .body("Logged out successfully");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Auth service is running");
    }
}