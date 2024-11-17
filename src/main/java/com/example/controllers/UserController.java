package com.example.controllers;

import com.example.exceptions.NotFoundException;
import com.example.exceptions.UsernameAlreadyExists;
import com.example.models.DTOs.UserDTO;
import com.example.services.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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
}
