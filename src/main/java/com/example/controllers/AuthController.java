package com.example.controllers;


import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.LoginDTO;
import com.example.models.entities.JwtResponse;
import com.example.services.AuthService.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }
    @PostMapping("/login")
    protected ResponseEntity<JwtResponse> loginAuth(@RequestBody LoginDTO request) throws NotFoundException {
        return ResponseEntity.ok(authService.login(request));
    }
}
