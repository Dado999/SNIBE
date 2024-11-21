package com.example.services.AuthService;

import com.example.config.JwtService;
import com.example.controllers.CommentController;
import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.LoginDTO;
import com.example.models.entities.JwtResponse;
import com.example.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public JwtResponse login(LoginDTO request) throws NotFoundException {
        logger.info("Login attempt for username: {}", request.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            logger.info("Authentication successful for username: {}", request.getUsername());
        } catch (Exception e) {
            logger.error("Authentication failed for username: {}. Error: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", request.getUsername());
                    return new NotFoundException();
                });

        logger.debug("User found in database: {}", user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        logger.debug("Added claims for JWT: {}", claims);

        var jwtToken = jwtService.generateTokenWithClaims(user, claims);
        logger.info("Generated JWT for username: {}", request.getUsername());

        return JwtResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }
}

