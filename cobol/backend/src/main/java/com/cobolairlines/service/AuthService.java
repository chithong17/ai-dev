package com.cobolairlines.service;

import com.cobolairlines.dto.AuthRequest; // Import DTO
import com.cobolairlines.dto.AuthResponse;

public interface AuthService {
    /**
     * Xác thực người dùng bằng logic mã hóa legacy.
     * @param authRequest Chứa empid và password
     * @return AuthResponse chứa JWT token
     * @throws AuthenticationException nếu xác thực thất bại
     */
    AuthResponse authenticate(AuthRequest authRequest) throws AuthenticationException;

    class AuthenticationException extends Exception {
        public AuthenticationException(String message) { super(message); }
    }
}
