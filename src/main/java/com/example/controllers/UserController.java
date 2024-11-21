package com.example.controllers;

import com.example.exceptions.NotFoundException;
import com.example.exceptions.UsernameAlreadyExists;
import com.example.models.DTOs.UserDTO;
import com.example.services.EmailService.EmailService;
import com.example.services.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get-current-user")
    public ResponseEntity<UserDTO> getCurrentUser() throws NotFoundException {
        logger.info("Fetching the current authenticated user.");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        logger.debug("Authenticated user: {}", username);
        UserDTO userDTO = userService.findByUsername(username);
        logger.info("Successfully retrieved current user: {}", username);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO user) throws NotFoundException, UsernameAlreadyExists {
        logger.info("Attempting to register a new user with username: {}", user.getUsername());
        UserDTO registeredUser = userService.insert(user);
        logger.info("User registered successfully with username: {}", registeredUser.getUsername());
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/get-permission")
    public ResponseEntity<Map<String, String>> getPermission() {
        logger.info("Fetching permissions for the current authenticated user.");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        logger.debug("Authenticated user: {}", username);
        UserDTO user = userService.findByUsername(username);
        String permission = user.getPermission();
        logger.info("Permission retrieved for user {}: {}", username, permission);
        return ResponseEntity.ok(Map.of("permission", permission));
    }

    @GetMapping("/get-unregistered-users")
    public ResponseEntity<List<UserDTO>> getAllUnregisteredUsers() {
        logger.info("Fetching all unregistered users.");
        List<UserDTO> unregisteredUsers = userService.getUnregisteredUsers();
        logger.info("Successfully retrieved {} unregistered users.", unregisteredUsers.size());
        return ResponseEntity.ok(unregisteredUsers);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        logger.info("Attempting to update user with ID: {}", id);

        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            logger.warn("Update request for user ID {} failed: Email is missing.", id);
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        logger.debug("Updating user: {}", userDTO);
        String updateMessage = userService.updateUser(userDTO);
        logger.info("User with ID {} updated successfully.", id);

        sendRegistrationEmail(userDTO.getEmail());
        logger.info("Registration email sent to: {}", userDTO.getEmail());

        return ResponseEntity.ok(Map.of("message", updateMessage));
    }

    private void sendRegistrationEmail(String email) {
        logger.info("Preparing to send registration email to: {}", email);

        String htmlContent = """
        <html>
        <body style='font-family: Arial, sans-serif; line-height: 1.6;'>
            <p>Dear User,</p>
            <p>We are excited to inform you that your account has been successfully registered!</p>
            <p>You can now log in and start using your account freely. Explore the features, interact with others, and make the most of our platform.</p>
            <p>If you did not register this account or believe this is a mistake, please contact our support team immediately.</p>
            <p style='margin-top: 20px;'>Best regards,</p>
            <p><strong>The Team</strong></p>
        </body>
        </html>
        """;

        emailService.sendSimpleEmail(email, "Account Registration Confirmation", htmlContent);
        logger.info("Email sent to: {}", email);
    }
}

