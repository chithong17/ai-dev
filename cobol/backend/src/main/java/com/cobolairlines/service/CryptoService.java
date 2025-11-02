package com.cobolairlines.service;

import java.util.Date;

/**
 * Interface for the legacy COBOL crypto algorithm.
 * Methods are designed to work with byte arrays, as the
 * original hash is EBCDIC binary data, not text.
 */
public interface CryptoService {

    /**
     * Generates the 8-byte legacy EBCDIC hash.
     * Ported from CICS/LOGIN/CRYPTO-VERIFICATION.
     *
     * @param password      Plaintext password
     * @param empid         Employee ID
     * @param admissionDate Employee admission date (used as seed)
     * @return 8-byte EBCDIC hash
     */
    byte[] generateLegacyHash(String password, String empid, Date admissionDate);

    /**
     * Verifies a plaintext password against a stored 8-byte EBCDIC hash.
     *
     * @param password      Plaintext password
     * @param empid         Employee ID
     * @param admissionDate Employee admission date
     * @param storedHash    The 8-byte hash from the legacy PASSDOC file
     * @return true if match, false otherwise
     */
    boolean verifyPassword(String password, String empid, Date admissionDate, byte[] storedHash);
}
