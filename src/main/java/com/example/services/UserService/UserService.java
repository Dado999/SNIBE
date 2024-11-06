package com.example.services.UserService;

import com.example.base.CrudService;
import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.LoginDTO;
import com.example.models.DTOs.UserDTO;
import com.example.models.entities.JwtResponse;
import com.example.models.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends CrudService<Integer>, UserDetailsService {

    UserDTO findByUsername(String username) throws NotFoundException;
}
