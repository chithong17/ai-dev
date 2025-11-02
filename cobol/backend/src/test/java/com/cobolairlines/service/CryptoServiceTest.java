package com.cobolairlines.service;

import com.cobolairlines.service.impl.CryptoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

// Import HÀNG ĐẦU
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CryptoServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CryptoServiceTest.class);
    private CryptoService cryptoService;

    // Map để giữ hash gốc từ file COBOL
    // Key: empid (String), Value: 8-byte EBCDIC hash (byte[])
    private Map<String, byte[]> legacyHashMap = new HashMap<>();
    
    // Map để giữ dữ liệu đầu vào (pass, date)
    // Key: empid (String), Value: UserInputData
    private Map<String, UserInputData> legacyInputMap = new HashMap<>();

    // Lớp helper để chứa dữ liệu đầu vào
    class UserInputData {
        String password;
        Date admissionDate; // Sử dụng java.util.Date
        String empid;

        UserInputData(String empid, String password, String admissionDateStr) {
            try {
                this.empid = empid;
                this.password = password;
                // COBOL/JSON date là YYYY/MM/DD
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                this.admissionDate = sdf.parse(admissionDateStr); // Trả về java.util.Date
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        cryptoService = new CryptoServiceImpl();
        
        // 1. Tải hash gốc (EMPLO-OUTPUT-PASS-CRYPT)
        // Đây là tệp nhị phân EBCDIC, 16 byte mỗi bản ghi (8 byte ID + 8 byte HASH)
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("EMPLO-OUTPUT-PASS-CRYPT")) {
            assertNotNull(is, "Không thể tìm thấy tệp hash gốc EMPLO-OUTPUT-PASS-CRYPT. Hãy đặt nó vào src/test/resources");
            
            byte[] record = new byte[16];
            while (is.read(record) != -1) {
                String empid = new String(record, 0, 8, "Cp037").trim(); // Đọc 8 byte ID (EBCDIC)
                byte[] hash = new byte[8];
                System.arraycopy(record, 8, hash, 0, 8); // Đọc 8 byte HASH
                legacyHashMap.put(empid, hash);
            }
            log.info("Đã tải {} hash gốc từ tệp EBCDIC.", legacyHashMap.size());
        }

        // 2. Tải dữ liệu đầu vào (Emplo-file)
        // Đây là tệp văn bản, định dạng: EMPID;PASSWORD;ADMIDATE
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Emplo-file")) {
            assertNotNull(is, "Không thể tìm thấy tệp đầu vào Emplo-file. Hãy đặt nó vào src/test/resources");
            
            new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .forEach(line -> {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        legacyInputMap.put(parts[0], new UserInputData(parts[0], parts[1], parts[2]));
                    }
                });
            log.info("Đã tải {} bản ghi đầu vào (mật khẩu).", legacyInputMap.size());
        }
        
        assertTrue(legacyHashMap.size() > 0, "Tệp hash EBCDIC trống rỗng.");
        assertTrue(legacyInputMap.size() > 0, "Tệp Emplo-file trống rỗng.");
    }

    /**
     * Bài test này lặp qua MỌI mật khẩu trong tệp gốc và
     * khẳng định (assert) rằng logic Java của chúng ta tạo ra
     * hash (byte-for-byte) giống hệt.
     */
    @Test
    void testLegacyHashCompatibility() {
        log.info("Bắt đầu kiểm tra tính tương thích của {} hash...", legacyHashMap.size());

        for (String empid : legacyHashMap.keySet()) {
            UserInputData input = legacyInputMap.get(empid);
            byte[] expectedHash = legacyHashMap.get(empid);

            assertNotNull(input, "Không tìm thấy dữ liệu đầu vào cho empid: " + empid);

            // 3. Chạy logic Java (SUT)
            byte[] actualHash = cryptoService.generateLegacyHash(input.password, input.empid, input.admissionDate);

            // 4. KHẲNG ĐỊNH (ASSERT)
            // Đây là bài test. Nó BẮT BUỘC phải khớp.
            assertArrayEquals(expectedHash, actualHash, 
                "Hash không khớp cho nhân viên: " + empid + ". " +
                "Mong đợi (EBCDIC): " + bytesToHex(expectedHash) + " | Nhận được (Java): " + bytesToHex(actualHash));
        }
        
        log.info("THÀNH CÔNG! Logic CryptoServiceImpl khớp với {} bản ghi COBOL gốc.", legacyHashMap.size());
    }

    // Helper để in byte array cho dễ debug
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}

