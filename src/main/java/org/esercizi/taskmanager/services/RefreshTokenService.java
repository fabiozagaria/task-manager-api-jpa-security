package org.esercizi.taskmanager.services;

import org.esercizi.taskmanager.dto.RefreshResponse;
import org.esercizi.taskmanager.exceptions.InvalidRefreshTokenException;
import org.esercizi.taskmanager.models.RefreshToken;
import org.esercizi.taskmanager.models.User;
import org.esercizi.taskmanager.repository.RefreshTokenRepository;
import org.esercizi.taskmanager.security.JwtService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;

    }

    public String generateRefreshToken() {
        byte[] bytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);

        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);

    }

    public String hashToken(String rawToken) throws NoSuchAlgorithmException {
        byte[] tokenBytes = rawToken.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = messageDigest.digest(tokenBytes);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(hashBytes);
    }

    public String createRefreshToken(User user) throws NoSuchAlgorithmException {
        Instant now = Instant.now();

        String rawToken = generateRefreshToken();
        String rawTokenHash = hashToken(rawToken);

        RefreshToken refreshToken = new RefreshToken(
                null,
                user,
                rawTokenHash,
                now.plusSeconds(60000),
                now,
                null
        );
        refreshTokenRepository.save(refreshToken);
        return rawToken;

    }

    public RefreshToken getValidRefreshToken(String rawToken) throws NoSuchAlgorithmException {
        Instant now = Instant.now();

        String hashToken = hashToken(rawToken);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(hashToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Token not found"));
        if (refreshToken.getRevokedAt() != null) {
            throw new InvalidRefreshTokenException("Token revoked at " + refreshToken.getRevokedAt().toString());
        }

        if (!refreshToken.getExpiresAt().isAfter(now)) {
            throw new InvalidRefreshTokenException("Token expired");
        }


        return refreshToken;
    }


    public RefreshResponse refresh(String rawToken) throws NoSuchAlgorithmException {
        RefreshToken refreshToken = getValidRefreshToken(rawToken);
        User user = refreshToken.getUser();
        refreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshToken);


        String access = jwtService.generateToken(user.getUsername());
        String refresh = createRefreshToken(user);
        return new RefreshResponse(
                access,
                refresh
        );
    }

}
