package com.example.services.AuthService;

import com.example.config.JwtService;
import com.example.exceptions.NotFoundException;
import com.example.models.DTOs.LoginDTO;
import com.example.models.entities.JwtResponse;
import com.example.services.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userServiceImpl;

    public AuthService(JwtService jwtService, AuthenticationManager authenticationManager, UserService userServiceImpl) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userServiceImpl = userServiceImpl;
    }

    public JwtResponse login(LoginDTO request) throws NotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );
        UserDetails user = userServiceImpl.loadUserByUsername(request.getUsername());
        boolean userBln = user !=null;
        return (
                userBln ?
                        JwtResponse.builder().jwtToken(jwtService.generateToken(user)).build()
                        :
                        JwtResponse.builder().build()
        );
    }
}
