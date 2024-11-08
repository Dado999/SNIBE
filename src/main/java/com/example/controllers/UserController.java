package com.example.controllers;

import com.example.models.DTOs.LoginDTO;
import com.example.repositories.UserRepository;
import com.example.services.UserService.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    protected UserController(UserService service, UserRepository userRepository, UserService userServiceImpl, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/user")
    public void test(@RequestBody LoginDTO request){
        System.out.println("success");
    }
}
