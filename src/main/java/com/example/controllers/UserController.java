package com.example.controllers;

import com.example.exceptions.NotFoundException;
import com.example.exceptions.UsernameAlreadyExists;
import com.example.models.DTOs.UserDTO;
import com.example.repositories.UserRepository;
import com.example.services.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    @GetMapping("/user")
    public Integer test() throws NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return 1;
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

}
