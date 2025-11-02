package com.cobolairlines.service.impl;

import com.cobolairlines.dto.AuthRequest;
import com.cobolairlines.dto.AuthResponse;
import com.cobolairlines.mapper.AuthMapper;
import com.cobolairlines.model.Employee;
import com.cobolairlines.repo.EmployeeRepository;
import com.cobolairlines.service.AuthService;
import com.cobolairlines.service.CryptoService;
import com.cobolairlines.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.security.auth.login.LoginException; // Import đúng
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final CryptoService cryptoService; // Interface đã được cập nhật
    private final JwtService jwtService;

    // Map để giữ hash gốc (key: empid, value: 8-byte hash)
    private final Map<String, byte[]> legacyPasswordMap = new HashMap<>();

    // Tải tệp nhị phân EBCDIC từ resources
    @Value("classpath:EMPLO-OUTPUT-PASS-CRYPT")
    private Resource legacyPasswordFile;

    // Bỏ AuthMapper khỏi constructor vì chúng ta sẽ gọi nó tĩnh (static)
    public AuthServiceImpl(EmployeeRepository employeeRepository, CryptoService cryptoService, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.cryptoService = cryptoService;
        this.jwtService = jwtService;
    }

    /**
     * Tải tệp hash EBCDIC gốc vào bộ nhớ khi khởi động.
     * Yêu cầu: Đặt tệp 'EMPLO-OUTPUT-PASS-CRYPT' (từ kho dự án gốc)
     * vào 'backend/src/main/resources/'
     */
    @PostConstruct
    public void init() {
        try (InputStream is = legacyPasswordFile.getInputStream()) {
            byte[] record = new byte[16]; // 8 byte ID + 8 byte HASH (từ file gốc)
            while (is.read(record) != -1) {
                // Đọc 8 byte đầu tiên làm ID, chuyển đổi từ EBCDIC "Cp037"
                String empid = new String(record, 0, 8, "Cp037").trim();
                byte[] hash = new byte[8];
                // 8 byte tiếp theo là hash nhị phân
                System.arraycopy(record, 8, hash, 0, 8);
                legacyPasswordMap.put(empid, hash);
            }
            log.info("Đã tải thành công {} hash mật khẩu EBCDIC vào bộ nhớ.", legacyPasswordMap.size());
        } catch (Exception e) {
            log.error("LỖI NGHIÊM TRỌNG: Không thể tải tệp hash 'EMPLO-OUTPUT-PASS-CRYPT'. Đăng nhập sẽ thất bại.", e);
            log.error("VUI LÒNG: Sao chép tệp 'EMPLO-OUTPUT-PASS-CRYPT' từ kho dự án gốc [kerestes/cobol-airlines] vào [backend/src/main/resources/]");
        }
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) throws LoginException {
        String empid = authRequest.getEmpid();
        String password = authRequest.getPassword();

        // 1. Lấy thông tin nhân viên (Giống LOGIN-COB)
        Employee employee = employeeRepository.findByEmpid(empid)
                .orElseThrow(() -> new LoginException("PASSWORD OR USERID INCORRECT."));

        // 2. Lấy hash gốc đã lưu từ tệp COBOL
        byte[] storedHash = legacyPasswordMap.get(empid);
        if (storedHash == null) {
            log.error("Đăng nhập thất bại: Nhân viên '{}' có tồn tại nhưng không có hash trong tệp PASSDOC.", empid);
            throw new LoginException("Authentication failed: User has no password");
        }

        // 3. Gọi CryptoService ĐÃ SỬA (Giống LOGIN-COB, Dòng 223: CALL 'CRYPTVE')
        // Chúng ta truyền java.util.Date từ entity Employee
        boolean isValid = cryptoService.verifyPassword(
                password,
                employee.getEmpid(),
                employee.getAdmidate(), // Đây là java.sql.Date, là một lớp con của java.util.Date
                storedHash
        );

        // 4. Kiểm tra cờ (Giống LOGIN-COB, Dòng 227: IF WS-FLAG-RETURN = 0)
        if (isValid) {
            log.info("Xác thực thành công cho: {}", empid);
            // Tạo JWT
            String token = jwtService.generateToken(employee.getEmpid(), employee.getDeptid());
            // Trả về DTO (Bao gồm DEPTID, rất quan trọng cho logic điều hướng)
            return AuthMapper.toAuthResponse(employee, token);
        } else {
            log.warn("Đăng nhập thất bại: Mật khẩu không khớp cho '{}'", empid);
            throw new LoginException("PASSWORD OR USERID INCORRECT.");
        }
    }
}
