package org.esercizi.taskmanager.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(900))
                .subject(authentication.getName())
                .build();
        JwtEncoderParameters encoderParameter = JwtEncoderParameters.from(claimsSet);
        return jwtEncoder.encode(encoderParameter).getTokenValue();


    }
}
