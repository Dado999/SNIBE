package com.example.controllers;

import com.example.base.CrudController;
import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.UserDTO;
import com.example.models.DTOs.LoginDTO;
import com.example.models.entities.JwtResponse;
import com.example.repositories.UserRepository;
import com.example.services.UserService.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController extends CrudController<Integer, UserDTO> {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    protected UserController(UserService service, UserRepository userRepository, UserService userServiceImpl, ModelMapper modelMapper) {
        super(UserDTO.class, service);
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

}
