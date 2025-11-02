package com.cobolairlines.service;

import com.cobolairlines.dto.AuthRequest;
import com.cobolairlines.dto.AuthResponse;
import com.cobolairlines.model.Employee;
import com.cobolairlines.repo.EmployeeRepository;
import com.cobolairlines.service.impl.AuthServiceImpl;
import com.cobolairlines.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void authenticateHappyPath() throws Exception {
        // Mocks
        EmployeeRepository repo = Mockito.mock(EmployeeRepository.class);
        CryptoService crypto = Mockito.mock(CryptoService.class);
        JwtService jwt = new JwtServiceImpl("change-me-in-production", 3600);

        // Build Employee (ensure admidate is java.util.Date)
        Employee e = new Employee();
        e.setEmpid("E1");
        e.setDeptid(7);
        LocalDate ld = LocalDate.of(2000,1,1);
        LocalDate admDate = LocalDate.from(ld.atStartOfDay(ZoneId.of("UTC")).toInstant());
        e.setAdmidate(admDate);
        // repo mock
        Mockito.when(repo.findByEmpid("E1")).thenReturn(Optional.of(e));

        // Prepare legacy password map: key empid -> 8-byte hash
        byte[] storedHash = new byte[8];
        // fill with sample bytes
        for (int i = 0; i < 8; i++) storedHash[i] = (byte)i;
        Map<String, byte[]> legacyMap = Map.of("E1", storedHash);

        // CryptoService mock should return true for verifyPassword
        Mockito.when(crypto.verifyPassword(Mockito.eq("pw"), Mockito.eq("E1"), Mockito.eq(admDate), Mockito.eq(storedHash))).thenReturn(true);

        // Use test-friendly constructor with injected legacyMap
        AuthServiceImpl svc = new AuthServiceImpl(repo, crypto, jwt, legacyMap);

        // Build auth request (use real DTO)
        AuthRequest req = new AuthRequest();
        req.setEmpid("E1");
        req.setPassword("pw");

        AuthResponse resp = svc.authenticate(req);

        assertNotNull(resp.getToken());
        assertEquals("E1", resp.getEmpid());
        assertEquals(7, resp.getDeptid());
    }
}
