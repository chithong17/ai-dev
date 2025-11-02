package com.cobolairlines.service.impl;

import com.cobolairlines.dto.AuthResponse;
import com.cobolairlines.model.Employee;
import com.cobolairlines.repo.EmployeeRepository;
import com.cobolairlines.service.AuthService;
import com.cobolairlines.service.CryptoService;
import com.cobolairlines.service.JwtService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final CryptoService cryptoService;
    private final JwtService jwtService;

    public AuthServiceImpl(EmployeeRepository employeeRepository, CryptoService cryptoService, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.cryptoService = cryptoService;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse authenticate(String empid, String password) throws AuthenticationException {
        Employee emp = employeeRepository.findByEmpid(empid).orElse(null);
        if (emp == null) throw new AuthenticationException("PASSWORD OR USERID INCORRECT.");

        String admidateStr = emp.getAdmidate() == null ? "" : emp.getAdmidate().format(DateTimeFormatter.BASIC_ISO_DATE);
        String crypt = cryptoService.produceCryptPass(empid, password, admidateStr);
        String stored = emp.getStoredCryptpass();
        if (stored == null || !stored.equals(crypt)) {
            throw new AuthenticationException("PASSWORD OR USERID INCORRECT.");
        }

        String token = jwtService.generateToken(emp.getEmpid(), emp.getDeptid());
        return new AuthResponse(token, emp.getEmpid(), emp.getDeptid());
    }
}
