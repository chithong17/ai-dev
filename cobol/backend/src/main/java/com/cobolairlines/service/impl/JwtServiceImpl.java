package com.cobolairlines.service.impl;

import com.cobolairlines.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey key;
    private final long ttlSeconds;

    public JwtServiceImpl(@Value("${app.jwt.secret}") String secret,
                          @Value("${app.jwt.ttl-seconds}") long ttlSeconds) {
        // NOTE: For demo, derive a 256-bit key from the configured secret using SHA-256
        // This ensures the key length meets JWA requirements for HMAC-SHA algorithms.
        SecretKey tmpKey;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] kb = md.digest(secret.getBytes(StandardCharsets.UTF_8));
            tmpKey = Keys.hmacShaKeyFor(kb);
        } catch (Exception ex) {
            // fallback: use raw bytes (may cause WeakKeyException in tests if too short)
            tmpKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        this.key = tmpKey;
        this.ttlSeconds = ttlSeconds;
    }

    @Override
    public String generateToken(String empid, Integer deptid) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date exp = Date.from(now.plusSeconds(ttlSeconds));

        return Jwts.builder()
                .setSubject(empid)
                .claim("deptid", deptid)
                .setIssuedAt(issuedAt)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
