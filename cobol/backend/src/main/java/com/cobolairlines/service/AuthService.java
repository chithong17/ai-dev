package com.cobolairlines.service;

import com.cobolairlines.dto.AuthRequest;
import com.cobolairlines.dto.AuthResponse;

// Import exception đúng
import javax.security.auth.login.LoginException;

public interface AuthService {
    /**
     * Xác thực người dùng bằng logic mã hóa legacy.
     * @param authRequest Chứa empid và password
     * @return AuthResponse chứa JWT token
     * @throws LoginException nếu xác thực thất bại
     */
    AuthResponse authenticate(AuthRequest authRequest) throws LoginException;
}
