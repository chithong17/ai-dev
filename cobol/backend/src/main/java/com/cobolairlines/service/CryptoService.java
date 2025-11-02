package com.cobolairlines.service;

public interface CryptoService {
    /**
     * Produce the legacy cryptpass value for given inputs.
     * Implementation MUST match COBOL `CRYPTO-VERIFICATION` exactly.
     *
     * @param empid employee id (may be used as salt)
     * @param password raw password provided
     * @param admidate admission date as a String in legacy format (implementation doc will define format)
     * @return cryptpass string to compare with stored value
     */
    String produceCryptPass(String empid, String password, String admidate);

    /**
     * Try legacy matching by brute-forcing the WS-KEY (0..999) to find a WS-KEY
     * that reproduces a stored legacy crypt. This is a pragmatic fallback used
     * during migration to accept existing users while we refine a perfect port.
     *
     * @return true if a matching WS-KEY was found
     */
    boolean legacyMatches(String empid, String password, String admidate, String storedCrypt);
}
