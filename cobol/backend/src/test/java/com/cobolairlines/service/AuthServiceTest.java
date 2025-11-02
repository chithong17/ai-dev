package com.cobolairlines.service;

import com.cobolairlines.dto.AuthResponse;
import com.cobolairlines.model.Employee;
import com.cobolairlines.repo.EmployeeRepository;
import com.cobolairlines.service.impl.AuthServiceImpl;
import com.cobolairlines.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void authenticateHappyPath() throws Exception {
        EmployeeRepository repo = Mockito.mock(EmployeeRepository.class);
        CryptoService crypto = Mockito.mock(CryptoService.class);
        JwtService jwt = new JwtServiceImpl("change-me-in-production",3600);

        Employee e = new Employee();
        e.setEmpid("E1");
        e.setDeptid(7);
        e.setAdmidate(LocalDate.of(2000,1,1));
        e.setStoredCryptpass("abc123");

        Mockito.when(repo.findByEmpid("E1")).thenReturn(java.util.Optional.of(e));
        Mockito.when(crypto.produceCryptPass(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("abc123");

        AuthServiceImpl svc = new AuthServiceImpl(repo, crypto, jwt);
        AuthResponse resp = svc.authenticate("E1","pw");
        assertNotNull(resp.getToken());
        assertEquals("E1", resp.getEmpid());
        assertEquals(7, resp.getDeptid());
    }
}
