package com.cobolairlines.service;

public interface JwtService {
    String generateToken(String empid, Integer deptid);
}
