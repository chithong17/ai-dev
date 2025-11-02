package com.cobolairlines.service.impl;

import com.cobolairlines.service.CryptoService;
import org.springframework.stereotype.Service;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Dịch vụ triển khai logic mã hóa độc quyền từ chương trình COBOL CRYPTO-VERIFICATION.
 * CẢNH BÁO: Thuật toán này KHÔNG an toàn về mặt mã hóa.
 * Nó chỉ được port (chuyển đổi) để đảm bảo tính tương thích ngược (backward compatibility)
 * với tệp mật khẩu (PASSDOC) đã có.
 *
 * Logic được dịch ngược từ: CICS/LOGIN/CRYPTO-VERIFICATION
 */
@Service
public class CryptoServiceImpl implements CryptoService {

    // Bảng mã EBCDIC (IBM-037) được sử dụng trên Mainframe
    private static final Charset EBCDIC_CHARSET = Charset.forName("Cp037");
    private static final byte EBCDIC_SPACE = (byte) 0x40;

    /**
     * Lớp nội bộ để mô phỏng chính xác hàm FUNCTION RANDOM(seed) của COBOL.
     * Đây là một trình tạo số đồng dư tuyến tính (Linear Congruential Generator - LCG)
     * với các hằng số được sử dụng bởi Micro Focus COBOL / IBM COBOL.
     *
     * Công thức: X(n+1) = (A * X(n) + C) mod M
     */
    private static class CobolRandom {
        private static final long A = 397204094L;
        private static final long M = 2147483647L; // (2^31 - 1)
        private final AtomicLong seed;

        CobolRandom(long seed) {
            // Seed không bao giờ được là 0, COBOL xử lý việc này
            if (seed == 0) {
                seed = 1; 
            }
            this.seed = new AtomicLong(seed);
        }

        /**
         * Trả về giá trị double [0, 1.0) giống hệt COBOL
         */
        public double nextDouble() {
            long nextSeed;
            long currentSeed;
            do {
                currentSeed = this.seed.get();
                nextSeed = (A * currentSeed) % M;
                // Xử lý giá trị âm (do tràn số)
                if (nextSeed < 0) {
                    nextSeed += M;
                }
            } while (!this.seed.compareAndSet(currentSeed, nextSeed));
            
            return (double) nextSeed / M;
        }
    }

    /**
     * Dịch ngược logic từ CICS/LOGIN/CRYPTO-VERIFICATION (Dòng 130-180)
     */
    @Override
    public byte[] generateLegacyHash(String password, String empid, Date admissionDate) {
        
        // 1. Chuẩn bị đầu vào (Pad sang 8 byte bằng ký tự EBCDIC space)
        byte[] passBytes = padRightEbcdic(password, 8);
        byte[] userBytes = padRightEbcdic(empid, 8);
        byte[] hashResult = new byte[8];

        try {
            // 2. Tạo Seed từ Ngày tuyển dụng (Giống Dòng 135-137: MOVE LS-ADDATE...)
            // COBOL sử dụng PIC 9(9) COMP, là một số nguyên nhị phân.
            // Chúng ta chuyển YYYY-MM-DD (java.util.Date) thành YYYYMMDD
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            
            // Xử lý nếu admissionDate là null
            long seed = (admissionDate != null) ? Long.parseLong(sdf.format(admissionDate)) : 19700101L;

            // 3. Khởi tạo Trình tạo số ngẫu nhiên của COBOL (Dòng 144)
            CobolRandom random = new CobolRandom(seed);
            
            // 4. Lấy các khóa "ngẫu nhiên"
            // COMPUTE WS-KEY1 = FUNCTION RANDOM (WS-DATE-SEED-1) * 100.
            int wsKey1 = (int) (random.nextDouble() * 100);

            // 5. Vòng lặp băm chính (Dòng 150-163: PERFORM VARYING WS-I...)
            for (int i = 0; i < 8; i++) {
                int wsI = i + 1; // COBOL là 1-indexed (WS-I)

                // COMPUTE WS-KEY2 = WS-KEY1 / (WS-I + 1).
                // Logic gốc của COBOL là (WS-I + 1)
                int wsKey2 = wsKey1 / (wsI + 1); 

                // Lấy giá trị byte *không dấu* (unsigned)
                int passVal = passBytes[i] & 0xFF;
                int userVal = userBytes[i] & 0xFF;
                
                // Dòng 160-162: Phép toán cốt lõi
                // COMPUTE WS-CRYPTPASS-1 (WS-I) =
                //   (WS-PASS-VAL * WS-I) + (WS-USER-VAL * (8 - WS-I))
                //   + WS-KEY1 + WS-KEY2
                int hashVal = (passVal * wsI) + (userVal * (8 - wsI)) + wsKey1 + wsKey2;

                // COBOL xử lý tràn số (overflow) bằng cách chỉ lấy byte cuối
                hashResult[i] = (byte) (hashVal & 0xFF);
            }

            return hashResult;

        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo hash legacy", e);
        }
    }

    /**
     * Triển khai phương thức verifyPassword
     */
    @Override
    public boolean verifyPassword(String password, String empid, Date admissionDate, byte[] storedHash) {
        if (storedHash == null || storedHash.length != 8) {
            return false;
        }
        byte[] generatedHash = generateLegacyHash(password, empid, admissionDate);
        return Arrays.equals(generatedHash, storedHash);
    }

    /**
     * Pad một chuỗi sang bên phải bằng ký tự EBCDIC space (0x40).
     */
    private byte[] padRightEbcdic(String text, int length) {
        if (text == null) text = "";
        byte[] textBytes = text.getBytes(EBCDIC_CHARSET);
        if (textBytes.length >= length) {
            return Arrays.copyOf(textBytes, length);
        }
        
        byte[] paddedBytes = new byte[length];
        System.arraycopy(textBytes, 0, paddedBytes, 0, textBytes.length);
        Arrays.fill(paddedBytes, textBytes.length, length, EBCDIC_SPACE);
        
        return paddedBytes;
    }
}
