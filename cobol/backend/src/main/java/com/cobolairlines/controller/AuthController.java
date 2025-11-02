package com.cobolairlines.controller;

import com.cobolairlines.dto.AuthRequest;
import com.cobolairlines.dto.AuthResponse;
import com.cobolairlines.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            AuthResponse resp = authService.authenticate(req.getEmpid(), req.getPassword());
            return ResponseEntity.ok(resp);
        } catch (AuthService.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
