package org.esercizi.taskmanager.controllers;

import jakarta.validation.Valid;
import org.esercizi.taskmanager.dto.LoginRequest;
import org.esercizi.taskmanager.dto.LoginResponse;
import org.esercizi.taskmanager.models.User;
import org.esercizi.taskmanager.security.JwtService;
import org.esercizi.taskmanager.services.RefreshTokenService;
import org.esercizi.taskmanager.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse postLogin(
            @Valid @RequestBody LoginRequest request
    ) throws NoSuchAlgorithmException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = userService.getByUsername(request.username());
        String refreshToken = refreshTokenService.createRefreshToken(user);
        String jwtAccessToken = jwtService.generateToken(authentication);
        return new LoginResponse(jwtAccessToken, refreshToken);
    }

}
