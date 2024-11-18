package com.example.services.AuthService;

import com.example.config.JwtService;
import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.LoginDTO;
import com.example.models.entities.JwtResponse;
import com.example.repositories.UserRepository;
import com.example.services.UserService.UserService;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService;
    private final UserRepository userRepository;

//    public JwtResponse login(LoginDTO request) throws NotFoundException {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
//        }
//        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
//        var jwtToken = jwtService.generateToken(user);
//        return JwtResponse.builder().jwtToken(jwtToken).build();
//    }

    public JwtResponse login(LoginDTO request) throws NotFoundException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }

        // Fetch the user from the repository
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(NotFoundException::new);

        // Check user's role and add it as a claim in the JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole()); // Assuming user.getRole() returns "admin", "user", etc.

        // Generate the token with additional claims
        var jwtToken = jwtService.generateTokenWithClaims(user, claims);
        return JwtResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }

}
