package com.smartoffice.controller;

import com.smartoffice.dto.AuthResponse;
import com.smartoffice.dto.LoginRequest;
import com.smartoffice.dto.ProfileResponse;
import com.smartoffice.dto.RegisterRequest;
import com.smartoffice.dto.ProfileUpdateRequest;
import com.smartoffice.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResponse auth = authService.register(request);
        writeCookie(response, auth.getToken());
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            AuthResponse auth = authService.login(request);
            writeCookie(response, auth.getToken());
            return ResponseEntity.ok(auth);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of("status", 401, "message", "Invalid email or password"));
        }
    }

    @GetMapping("/me")
    public ProfileResponse me(Authentication authentication) {
        return authService.profile(authentication.getName());
    }

    @PutMapping("/me/profile")
    public ProfileResponse updateProfile(@Valid @RequestBody ProfileUpdateRequest request, Authentication authentication) {
        return authService.updateProfile(authentication.getName(), request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", "");
        cookie.setHttpOnly(true); cookie.setSecure(false); cookie.setPath("/"); cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    private void writeCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set true when deployed behind HTTPS.
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }
}
