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
}
