package com.chidicivok.authservice.services;

import com.chidicivok.authservice.clients.dto.AdminAuthenticationResponse;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public record TokenResult(String token, Instant expiresAt) {
    }

    public TokenResult generateAdminToken(AdminAuthenticationResponse admin) {

        Instant issuedAt = Instant.now();

        Instant expiresAt = issuedAt.plus(60, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("civok-auth-service")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(admin.getEmail())
                .claim("adminId", admin.getAdminId())
                .claim("principalType", "ADMIN")
                .claim("role", admin.getAdminRole().name())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new TokenResult(token, expiresAt);
    }


}
