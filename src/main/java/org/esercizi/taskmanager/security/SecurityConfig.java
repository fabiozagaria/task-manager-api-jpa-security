package org.esercizi.taskmanager.security;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                authorize -> {
                    authorize.requestMatchers(HttpMethod.POST, "/users/register",
                                    "/auth/refresh",
                                    "/auth/login")
                            .permitAll();

                    authorize.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll();
                    authorize.anyRequest().authenticated();
                }

        );
        httpSecurity.csrf(csrf ->
                csrf.ignoringRequestMatchers(
                        "/users/register",
                        "/auth/login",
                        "/auth/refresh"
                        )


        );
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults()));

        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return httpSecurity.build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
       return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    JwtEncoder jwtEncoder(
            @Value("${security.jwt.secret}") String jwtSecret
    ) {

        return NimbusJwtEncoder
                .withSecretKey(getSecretKey(jwtSecret))
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder(
            @Value("${security.jwt.secret}") String jwtSecret
    ) {
        SecretKey secretKey = getSecretKey(jwtSecret);
        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .build();
    }

    private SecretKey getSecretKey(String jwtSecret) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(
                keyBytes,
                "HmacSHA256");
    }
}
