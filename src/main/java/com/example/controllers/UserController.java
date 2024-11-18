package com.example.controllers;

import com.example.exceptions.NotFoundException;
import com.example.exceptions.UsernameAlreadyExists;
import com.example.models.DTOs.UserDTO;
import com.example.services.EmailService.EmailService;
import com.example.services.UserService.UserService;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/get-current-user")
    public ResponseEntity<UserDTO> getCurrentUser() throws NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.findByUsername(userDetails.getUsername()));
    }
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO user) throws NotFoundException, UsernameAlreadyExists {
       return ResponseEntity.ok(userService.insert(user));
    }

    @GetMapping("/get-permission")
    public ResponseEntity<Map<String,String>> getPermission(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO u = userService.findByUsername(userDetails.getUsername());
        System.out.println(u.getPermission());
        return ResponseEntity.ok(Map.of("permission", u.getPermission()));
    }
    @GetMapping("/get-unregistered-users")
    public ResponseEntity<List<UserDTO>> getAllUnregisteredUsers(){
        return ResponseEntity.ok(userService.getUnregisteredUsers());
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        // Validate email and user DTO
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }
        String updateMessage = userService.updateUser(userDTO);
        sendRegistrationEmail(userDTO.getEmail());

        return ResponseEntity.ok(Map.of("message", updateMessage));
    }
    private void sendRegistrationEmail(String email) {
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
    }

}
