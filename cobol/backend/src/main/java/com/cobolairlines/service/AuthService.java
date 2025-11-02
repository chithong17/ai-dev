package com.cobolairlines.service;

import com.cobolairlines.dto.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(String empid, String password) throws AuthenticationException;

    class AuthenticationException extends Exception {
        public AuthenticationException(String message) { super(message); }
    }
}
