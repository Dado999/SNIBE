package com.example.controllers;


import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.LoginDTO;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.JwtResponse;
import com.example.models.entities.User;
import com.example.repositories.UserRepository;
import com.example.services.AuthService.AuthService;
import com.example.services.EmailService.EmailService;
import com.example.services.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")
    protected ResponseEntity<JwtResponse> loginAuth(@RequestBody LoginDTO request) throws NotFoundException {
        logger.info("Authentication request intercepted");
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/2fa")
    public Integer sendVerificationCode(@RequestBody Map<String, String> request) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new NotFoundException();
        }
        UserDTO user = userService.findByUsername(userDetails.getUsername());
        String email = user.getEmail();


        Integer randomCode = new Random().nextInt(1000, 9999);
        String htmlContent = "<html><body>"
                + "<p>Hello,</p>"
                + "<p>Your two-factor verification code is:</p>"
                + "<p style='font-weight: bold;'>" + randomCode + "</p>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "</body></html>";
        logger.info("Sending an 2FA email to the user {}, at mail: {}", user.getUsername(), user.getEmail());
        emailService.sendSimpleEmail(email, "Your Verification Code", htmlContent);
        return randomCode;
    }

}
