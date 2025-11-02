package com.cobolairlines.service;

import java.time.LocalDate;

public interface CryptoService {

    byte[] generateLegacyHash(String password, String empid, LocalDate admissionDate);

    boolean verifyPassword(String password, String empid, LocalDate admissionDate, byte[] storedHash);
}
