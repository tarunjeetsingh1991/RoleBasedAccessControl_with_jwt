package com.fdmgroup.authservice.security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

// new file

@Service
public class CookieService {

    private static final String COOKIE_NAME = "jwt";

    public String buildJwtCookie(String token, long maxAgeSeconds, boolean secure) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds)
                .build();

        return cookie.toString();
    }

    public String clearJwtCookie(boolean secure) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        return cookie.toString();
    }
}