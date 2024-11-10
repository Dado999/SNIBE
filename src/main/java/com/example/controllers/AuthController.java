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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;
    @PostMapping("/login")
    protected ResponseEntity<JwtResponse> loginAuth(@RequestBody LoginDTO request) throws NotFoundException {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/2fa")
    protected Integer sendVerificationCode() throws NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.findByUsername(userDetails.getUsername());
        String mail = user.getEmail();

        Integer random = new Random().nextInt(1000,9999);
        String htmlContent = "<html><body>"
                + "<p>Hello,</p>"
                + "<p>Thank you for verifying your account. To complete the process, please use the following two-factor verification code:</p>"
                + "<p style='text-align: center; font-size: 20px; font-weight: bold; color: #4CAF50;'>"
                + random
                + "</p>"
                + "<p>If you did not request this, please ignore this email or contact support.</p>"
                + "<br>"
                + "<p>Best regards,<br>Tvoja staraaaaaaaaa</p>"
                + "</body></html>";

        emailService.sendSimpleEmail(mail, "Verification Email", htmlContent);


        return random;
    }
}
